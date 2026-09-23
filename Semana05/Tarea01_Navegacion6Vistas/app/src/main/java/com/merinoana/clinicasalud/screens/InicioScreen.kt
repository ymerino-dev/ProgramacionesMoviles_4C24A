package com.merinoana.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(navController: NavController) {
    var especialidadSeleccionada by remember { mutableStateOf("Cardiología") }
    val especialidades = listOf("Cardiología", "Pediatría", "Dermatología", "Medicina General")

    // Lista de médicos: Nombre, Especialidad, Calificación
    val medicos = listOf(
        Triple("Dra. Ana Torres", "Cardiología", "4.8"),
        Triple("Dr. Luis Vega", "Pediatría", "4.7"),
        Triple("Dra. Rosa Díaz", "Dermatología", "4.9")
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Hola, Ana Yanira Merino Ramos", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // LazyRow para chips de especialidad
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(especialidades) { especialidad ->
                FilterChip(
                    selected = especialidadSeleccionada == especialidad,
                    onClick = { especialidadSeleccionada = especialidad },
                    label = { Text(especialidad) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Médicos disponibles", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        // LazyColumn para lista de médicos
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(medicos) { (nombre, especialidad, calificacion) ->
                Card(
                    onClick = { navController.navigate(Screen.Perfil.createRoute(nombre)) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Person, contentDescription = "Doctor", modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(nombre, fontWeight = FontWeight.Bold)
                            Text(especialidad, style = MaterialTheme.typography.bodyMedium)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = "Rating", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(calificacion, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}