package com.example.sqlite.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.sqlite.data.local.DatabaseHelper
import com.example.sqlite.data.local.PreferencesManager
import com.example.sqlite.data.model.Employee
import com.example.sqlite.data.network.EmployeeApiService
import com.example.sqlite.ui.components.EmployeeDetailDialog
import kotlinx.coroutines.launch

@Composable
fun FindEmployeesScreen(
    dbHelper: DatabaseHelper,
    preferencesManager: PreferencesManager
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var professionInput by remember { mutableStateOf("") }
    var employeeList by remember { mutableStateOf<List<Employee>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var selectedEmployeeForDetail by remember { mutableStateOf<Employee?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Find Employees",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = professionInput,
                onValueChange = { professionInput = it },
                label = { Text("Profession (e.g. Abogado, Ingeniero)") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon"
                    )
                },
                modifier = Modifier.weight(1f),
                singleLine = true
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (professionInput.isNotBlank()) {
                        isLoading = true
                        errorMessage = null
                        coroutineScope.launch {
                            try {
                                val results = EmployeeApiService.instance
                                    .getEmployeesByProfession(professionInput.trim())
                                employeeList = results
                                if (results.isEmpty()) {
                                    errorMessage = "No employees found for '${professionInput.trim()}'."
                                }
                            } catch (e: Exception) {
                                errorMessage = "Error fetching data: ${e.localizedMessage}"
                                employeeList = emptyList()
                            } finally {
                                isLoading = false
                            }
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Please enter a profession to search",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.height(56.dp)
            ) {
                Text("Search")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(employeeList) { employee ->
                    EmployeeSearchCard(
                        employee = employee,
                        onDetailClick = {
                            selectedEmployeeForDetail = employee
                        },
                        onSaveClick = {
                            val success = dbHelper.insertEmployee(employee)
                            if (success) {
                                val totalSalary = dbHelper.calculateTotalSalaries()
                                preferencesManager.saveTotalSalaries(totalSalary)
                                Toast.makeText(
                                    context,
                                    "Saved ${employee.fullName} to favorites!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Error saving employee",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
                }
            }
        }
    }

    selectedEmployeeForDetail?.let { employee ->
        EmployeeDetailDialog(
            employee = employee,
            onDismiss = { selectedEmployeeForDetail = null }
        )
    }
}

@Composable
private fun EmployeeSearchCard(
    employee: Employee,
    onDetailClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = employee.foto,
                contentDescription = "Employee Photo",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "ID: ${employee.id}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "${employee.nombre} ${employee.apellidos}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = employee.profesion,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row {
                IconButton(onClick = onDetailClick) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "View Details",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(onClick = onSaveClick) {
                    Icon(
                        imageVector = Icons.Default.BookmarkAdd,
                        contentDescription = "Save to SQLite",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}
