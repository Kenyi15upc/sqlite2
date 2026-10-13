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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.sqlite.ui.components.EditEmployeeDialog

@Composable
fun EmployeesDataScreen(
    dbHelper: DatabaseHelper,
    preferencesManager: PreferencesManager
) {
    val context = LocalContext.current

    var savedEmployees by remember { mutableStateOf<List<Employee>>(emptyList()) }
    var totalSalariesPref by remember { mutableDoubleStateOf(0.0) }
    var employeeToEdit by remember { mutableStateOf<Employee?>(null) }

    fun refreshData() {
        val list = dbHelper.getAllEmployees()
        savedEmployees = list

        val total = dbHelper.calculateTotalSalaries()
        preferencesManager.saveTotalSalaries(total)
        totalSalariesPref = preferencesManager.getTotalSalaries()
    }

    LaunchedEffect(Unit) {
        refreshData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Employees Data (Favorites)",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (savedEmployees.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No saved employees in local database.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(savedEmployees) { employee ->
                    SavedEmployeeCard(
                        employee = employee,
                        onEditClick = {
                            employeeToEdit = employee
                        },
                        onDeleteClick = {
                            val deleted = dbHelper.deleteEmployee(employee.id)
                            if (deleted) {
                                refreshData()
                                Toast.makeText(
                                    context,
                                    "Removed ${employee.fullName} from local DB",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    TotalSalariesSummaryCard(totalSalaries = totalSalariesPref)
                }
            }
        }
    }

    employeeToEdit?.let { employee ->
        EditEmployeeDialog(
            employee = employee,
            onDismiss = { employeeToEdit = null },
            onSave = { updatedEmployee ->
                val updated = dbHelper.updateEmployee(updatedEmployee)
                if (updated) {
                    refreshData()
                    Toast.makeText(
                        context,
                        "Employee updated successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Error updating employee",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                employeeToEdit = null
            }
        )
    }
}

@Composable
private fun SavedEmployeeCard(
    employee: Employee,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = employee.foto,
                    contentDescription = "Employee Photo",
                    modifier = Modifier
                        .size(70.dp)
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
                        text = "${employee.cargo} (${employee.profesion})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Row {
                    IconButton(onClick = onEditClick) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Employee",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Employee",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "Edad: ${employee.edad} años | Experiencia: ${employee.experiencia} años",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Email: ${employee.email} | Tel: ${employee.telefono}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Dirección: ${employee.direccion}, ${employee.pais}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Departamento: ${employee.departamento}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Salario: S/ ${"%.2f".format(employee.salario)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
private fun TotalSalariesSummaryCard(totalSalaries: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total Salaries (SharedPreferences):",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = "S/ ${"%.2f".format(totalSalaries)}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
