package com.example.sqlite.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.sqlite.data.model.Employee

@Composable
fun EmployeeDetailDialog(
    employee: Employee,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        },
        title = {
            Text(
                text = "Employee Details",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (employee.foto.isNotBlank()) {
                    AsyncImage(
                        model = employee.foto,
                        contentDescription = "Employee Photo",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Text(
                    text = "${employee.nombre} ${employee.apellidos}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = employee.cargo.ifBlank { employee.profesion },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))

                DetailItem(label = "ID", value = employee.id)
                DetailItem(label = "Nombre", value = employee.nombre)
                DetailItem(label = "Apellidos", value = employee.apellidos)
                DetailItem(label = "Edad", value = "${employee.edad} años")
                DetailItem(label = "Profesión", value = employee.profesion)
                DetailItem(label = "Cargo", value = employee.cargo)
                DetailItem(label = "Experiencia", value = "${employee.experiencia} años")
                DetailItem(label = "Email", value = employee.email)
                DetailItem(label = "Teléfono", value = employee.telefono)
                DetailItem(label = "Dirección", value = employee.direccion)
                DetailItem(label = "Distrito", value = employee.distrito)
                DetailItem(label = "Ciudad", value = employee.ciudad)
                DetailItem(label = "País", value = employee.pais)
                DetailItem(label = "Estado Civil", value = employee.estadoCivil)
                DetailItem(label = "Género", value = employee.genero)
                DetailItem(label = "Salario", value = "S/ ${"%.2f".format(employee.salario)}")
                DetailItem(label = "Departamento", value = employee.departamento)
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
private fun DetailItem(label: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value.ifBlank { "N/A" },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
