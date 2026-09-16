package com.merinoana.registrodenotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.merinoana.registrodenotas.ui.theme.RegistroDeNotasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistroDeNotasTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PantallaRegistroNotas(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PantallaRegistroNotas(modifier: Modifier = Modifier) {
    // Variables de estado (Float para el Slider)
    var notaFundamentos by remember { mutableStateOf(0f) }
    var notaPOO by remember { mutableStateOf(0f) }
    var notaMoviles by remember { mutableStateOf(0f) }
    var notaBD by remember { mutableStateOf(0f) }
    var redondear by remember { mutableStateOf(false) }
    var confirmar by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Registro de Notas",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Notas del ciclo",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Desliza para asignar cada nota (0 a 20)",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Curso 1: Fundamentos (20%)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Fundamentos de Programación (20%)", fontWeight = FontWeight.Bold)
            Text(text = "${notaFundamentos.toInt()}")
        }
        Slider(
            value = notaFundamentos,
            onValueChange = { notaFundamentos = it },
            valueRange = 0f..20f,
            steps = 19
        )

        // Curso 2: POO (25%)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Programación Orientada a Objetos (25%)", fontWeight = FontWeight.Bold)
            Text(text = "${notaPOO.toInt()}")
        }
        Slider(
            value = notaPOO,
            onValueChange = { notaPOO = it },
            valueRange = 0f..20f,
            steps = 19
        )

        // Curso 3: Móviles (30%)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Programación en Móviles (30%)", fontWeight = FontWeight.Bold)
            Text(text = "${notaMoviles.toInt()}")
        }
        Slider(
            value = notaMoviles,
            onValueChange = { notaMoviles = it },
            valueRange = 0f..20f,
            steps = 19
        )

        // Curso 4: Base de Datos (25%)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Base de Datos (25%)", fontWeight = FontWeight.Bold)
            Text(text = "${notaBD.toInt()}")
        }
        Slider(
            value = notaBD,
            onValueChange = { notaBD = it },
            valueRange = 0f..20f,
            steps = 19
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Switch de redondeo
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Redondear promedio final")
            androidx.compose.material3.Switch(
                checked = redondear,
                onCheckedChange = { redondear = it }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Checkbox de confirmación
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            androidx.compose.material3.Checkbox(
                checked = confirmar,
                onCheckedChange = { confirmar = it }
            )
            Text(text = "Confirmo que las notas son correctas")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón condicionado
        androidx.compose.material3.Button(
            onClick = { /* Aquí irá el cálculo en la Fase 4 */ },
            modifier = Modifier.fillMaxWidth(),
            enabled = confirmar // La magia: se habilita solo si el checkbox es true
        ) {
            Text(text = "CALCULAR PROMEDIO")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mensaje inferior
        Text(
            text = "Asigna las notas y confirma para calcular",
            color = MaterialTheme.colorScheme.outline
        )

    }
}