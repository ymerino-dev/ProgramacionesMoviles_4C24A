package com.merinoana.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendarScreen(navController: NavController, medico: String?) {
    val nombreMedico = medico ?: "Médico no especificado"

    // Estados para almacenar la opción única seleccionada
    var fechaSeleccionada by remember { mutableStateOf("") }
    var horaSeleccionada by remember { mutableStateOf("") }

    // Datos estáticos solicitados en la rúbrica
    val fechas = listOf("Jue 26", "Vie 27", "Sáb 28")
    val horas = listOf("9:00 am", "10:30 am", "3:00 pm")

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))
        Text("Agendar cita con:", style = MaterialTheme.typography.titleMedium)
        Text(nombreMedico, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

        Spacer(modifier = Modifier.height(32.dp))

        // Sección de selección de fecha
        Text("Selecciona fecha", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            fechas.forEach { fecha ->
                FilterChip(
                    selected = fechaSeleccionada == fecha,
                    onClick = { fechaSeleccionada = fecha }, // Funciona como RadioButton
                    label = { Text(fecha) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sección de selección de hora
        Text("Selecciona hora", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            horas.forEach { hora ->
                FilterChip(
                    selected = horaSeleccionada == hora,
                    onClick = { horaSeleccionada = hora }, // Funciona como RadioButton
                    label = { Text(hora) }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Botón que solo se habilita si ambas opciones fueron seleccionadas
        Button(
            onClick = {
                // Pasamos los tres parámetros a la pantalla de confirmación
                navController.navigate(Screen.Confirmacion.createRoute(nombreMedico, fechaSeleccionada, horaSeleccionada))
            },
            enabled = fechaSeleccionada.isNotEmpty() && horaSeleccionada.isNotEmpty(),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Confirmar cita")
        }
    }
}