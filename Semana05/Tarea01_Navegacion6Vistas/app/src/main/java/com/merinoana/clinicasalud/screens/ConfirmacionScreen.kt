package com.merinoana.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ConfirmacionScreen(
    navController: NavController,
    medico: String?,
    fecha: String?,
    hora: String?,
    onCitaConfirmada: (Triple<String, String, String>) -> Unit // Nueva función para guardar
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.CheckCircle, contentDescription = "Éxito", tint = Color(0xFF4CAF50), modifier = Modifier.size(100.dp))
        Spacer(modifier = Modifier.height(24.dp))
        Text("¡Cita agendada!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "${medico ?: ""}\n${fecha ?: ""} - ${hora ?: ""}", textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                if (medico != null && fecha != null && hora != null) {
                    // Guardamos la cita en la lista dinámica
                    onCitaConfirmada(Triple(medico, "$fecha - $hora", "Confirmada"))
                }
                navController.navigate(Screen.MisCitas.route) {
                    popUpTo(Screen.Inicio.route) { inclusive = false }
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Ver mis citas")
        }
    }
}