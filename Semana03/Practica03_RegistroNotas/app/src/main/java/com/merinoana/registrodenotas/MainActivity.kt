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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.merinoana.registrodenotas.ui.theme.RegistroDeNotasTheme
import kotlin.math.roundToInt

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
    // Variables de notas
    var notaFundamentos by remember { mutableStateOf(0f) }
    var notaPOO by remember { mutableStateOf(0f) }
    var notaMoviles by remember { mutableStateOf(0f) }
    var notaBD by remember { mutableStateOf(0f) }

    // Variables de controles
    var redondear by remember { mutableStateOf(false) }
    var confirmar by remember { mutableStateOf(false) }

    // Variable para mostrar el resultado
    var mostrarResultado by remember { mutableStateOf(false) }

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
        Text("Notas del ciclo", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("Desliza para asignar cada nota (0 a 20)", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
        Spacer(modifier = Modifier.height(24.dp))

        // Cursos (Arreglado el diseño con weight(1f) para que no se partan los números)
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Fundamentos de Programación (20%)", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text(text = "${notaFundamentos.toInt()}", fontWeight = FontWeight.Bold)
        }
        Slider(value = notaFundamentos, onValueChange = { notaFundamentos = it }, valueRange = 0f..20f, steps = 19)

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Programación Orientada a Objetos (25%)", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text(text = "${notaPOO.toInt()}", fontWeight = FontWeight.Bold)
        }
        Slider(value = notaPOO, onValueChange = { notaPOO = it }, valueRange = 0f..20f, steps = 19)

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Programación en Móviles (30%)", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text(text = "${notaMoviles.toInt()}", fontWeight = FontWeight.Bold)
        }
        Slider(value = notaMoviles, onValueChange = { notaMoviles = it }, valueRange = 0f..20f, steps = 19)

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Base de Datos (25%)", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text(text = "${notaBD.toInt()}", fontWeight = FontWeight.Bold)
        }
        Slider(value = notaBD, onValueChange = { notaBD = it }, valueRange = 0f..20f, steps = 19)

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Redondear promedio final")
            Switch(checked = redondear, onCheckedChange = { redondear = it })
        }

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = confirmar, onCheckedChange = { confirmar = it })
            Text(text = "Confirmo que las notas son correctas")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón
        Button(
            onClick = { mostrarResultado = true },
            modifier = Modifier.fillMaxWidth(),
            enabled = confirmar
        ) {
            Text(text = "CALCULAR PROMEDIO")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Si no se ha calculado, muestra texto. Si se calculó, muestra tarjeta.
        if (!mostrarResultado) {
            Text(text = "Asigna las notas y confirma para calcular", color = MaterialTheme.colorScheme.outline)
        } else {
            // Matemática de pesos y redondeo
            val ponderado = (notaFundamentos * 0.20) + (notaPOO * 0.25) + (notaMoviles * 0.30) + (notaBD * 0.25)
            val final = if (redondear) ponderado.roundToInt().toDouble() else ponderado

            // Determinar estado y color
            val (observacion, colorEstado) = when {
                final >= 17 -> "EXCELENTE" to Color(0xFF2E7D32)
                final >= 13 -> "APROBADO" to Color(0xFF4CAF50)
                final >= 10 -> "EN RECUPERACIÓN" to Color(0xFFFFC107)
                else -> "DESAPROBADO" to Color(0xFFF44336)
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Promedio ponderado: ${String.format("%.2f", ponderado)}")

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Promedio final: ", style = MaterialTheme.typography.titleLarge)
                        Text(
                            text = if (redondear) "${final.toInt()}" else String.format("%.2f", final),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (redondear) {
                        Text(text = "(redondeado)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(color = colorEstado.copy(alpha = 0.2f), shape = MaterialTheme.shapes.small) {
                        Text(
                            text = observacion,
                            color = colorEstado,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "✓ Promedio calculado correctamente", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.weight(1f))
        // Firma final
        Text(
            text = "Desarrollado por: Ana Yanira Merino Ramos",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.fillMaxWidth()
        )
    }
}