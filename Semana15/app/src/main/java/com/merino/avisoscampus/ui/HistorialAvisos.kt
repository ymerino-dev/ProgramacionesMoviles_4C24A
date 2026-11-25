package com.merino.avisoscampus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.merino.avisoscampus.model.Aviso
import com.merino.avisoscampus.model.Origen

@Composable
fun HistorialAvisos(avisos: List<Aviso>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Historial",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            if (avisos.isEmpty()) {
                Text(
                    text = "Sin avisos todavía",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                avisos.take(20).forEach { aviso ->
                    val (colorFondo, colorTexto) = when (aviso.origen) {
                        Origen.BOTON -> Color(0xFFBBDEFB) to Color(0xFF1565C0)
                        Origen.RECORDATORIO -> Color(0xFFFFF9C4) to Color(0xFFF57F17)
                        Origen.MODO_AVION -> Color(0xFFE1BEE7) to Color(0xFF6A1B9A)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = aviso.origen.texto,
                            color = colorTexto,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .background(colorFondo, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        )

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = aviso.titulo,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = aviso.texto,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Text(
                            text = aviso.hora,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}