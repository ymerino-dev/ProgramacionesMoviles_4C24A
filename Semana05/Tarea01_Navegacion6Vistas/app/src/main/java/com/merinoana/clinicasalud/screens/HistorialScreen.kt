package com.merinoana.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HistorialScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Historial Médico", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Consulta General", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("15 Ago 2026 - Dr. Carlos Ruiz", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Diagnóstico: Resfriado común. Reposo por 3 días.", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Chequeo Anual", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("10 Mar 2026 - Dra. Elena Castro", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Diagnóstico: Paciente sana. Análisis de sangre en parámetros normales.", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}