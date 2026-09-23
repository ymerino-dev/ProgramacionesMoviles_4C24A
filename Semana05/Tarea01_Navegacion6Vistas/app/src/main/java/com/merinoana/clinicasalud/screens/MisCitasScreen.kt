package com.merinoana.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MisCitasScreen() {
    // Datos simulados de citas previas y confirmadas
    val citas = listOf(
        Triple("Dra. Ana Torres", "Jue 26 - 9:00 am", "Confirmada"),
        Triple("Dr. Luis Vega", "Vie 20 - 10:30 am", "Completada")
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Mis Citas", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(citas) { (medico, fecha, estado) ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(medico, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Text(fecha, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        // Diferenciación visual de los estados exigida en la rúbrica
                        Text(
                            text = estado,
                            color = if (estado == "Confirmada") Color(0xFF4CAF50) else Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}