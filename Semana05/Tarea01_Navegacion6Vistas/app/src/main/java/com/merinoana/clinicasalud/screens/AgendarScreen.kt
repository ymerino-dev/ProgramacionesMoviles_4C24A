package com.merinoana.clinicasalud.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendarScreen(navController: NavController, medico: String?) {
    val nombreMedico = medico ?: "Médico no especificado"

    var fechaSeleccionada by remember { mutableStateOf("") }
    var horaSeleccionada by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val fechas = listOf("Jue 26", "Vie 27", "Sáb 28")
    val horas = listOf("9:00 am", "10:30 am", "3:00 pm")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Agendar cita con:",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = nombreMedico,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Sección de selección de fecha
        Text(
            text = "Selecciona fecha",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            fechas.forEach { fecha ->
                val isSelected = fechaSeleccionada == fecha
                FilterChip(
                    selected = isSelected,
                    onClick = { fechaSeleccionada = fecha },
                    label = { Text(fecha, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color(0xFFF5F5F5),
                        labelColor = Color.DarkGray,
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = Color.White
                    ),
                    border = null
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Sección de selección de hora
        Text(
            text = "Selecciona hora",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            horas.forEach { hora ->
                val isSelected = horaSeleccionada == hora
                FilterChip(
                    selected = isSelected,
                    onClick = { horaSeleccionada = hora },
                    label = { Text(hora, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color(0xFFF5F5F5),
                        labelColor = Color.DarkGray,
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = Color.White
                    ),
                    border = null
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Botón principal CTA de 56.dp con esquinas redondeadas de 16.dp
        Button(
            onClick = { showDialog = true },
            enabled = fechaSeleccionada.isNotEmpty() && horaSeleccionada.isNotEmpty(),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Confirmar cita",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }

    // AlertDialog de confirmación (requisito solicitado)
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(text = "Confirmar cita médica", fontWeight = FontWeight.Bold)
            },
            text = {
                Text(text = "¿Deseas agendar tu cita con $nombreMedico para el día $fechaSeleccionada a las $horaSeleccionada?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        navController.navigate(Screen.Confirmacion.createRoute(nombreMedico, fechaSeleccionada, horaSeleccionada))
                    }
                ) {
                    Text("Sí, confirmar", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}
