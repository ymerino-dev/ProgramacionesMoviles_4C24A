package com.merino.campusgps.ui

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.merino.campusgps.model.TipoError
import com.merino.campusgps.model.UbicacionUi

@Composable
fun MensajeError(
    error: UbicacionUi.Error,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = error.mensaje,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        when (error.tipo) {
            TipoError.SIN_PERMISO -> {
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedButton(
                    onClick = {
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", contexto.packageName, null)
                        )
                        contexto.startActivity(intent)
                    }
                ) {
                    Text("Abrir ajustes")
                }
            }
            TipoError.GPS_APAGADO, TipoError.OTRO -> {
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedButton(onClick = onReintentar) {
                    Text("Reintentar")
                }
            }
            TipoError.SIN_PLAY_SERVICES -> {
                // Sin botón de reintentar según el contrato
            }
        }
    }
}