package com.merino.avisoscampus.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.merino.avisoscampus.model.PermisoUi
import com.merino.avisoscampus.notificacion.Notificador
import com.merino.avisoscampus.viewmodel.AvisosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAvisos(vm: AvisosViewModel) {
    val contexto = LocalContext.current
    val permiso by vm.permiso.collectAsState()

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        vm.permisoRespondido(concedido)
    }

    LaunchedEffect(Unit) {
        if (Notificador.tienePermiso(contexto)) {
            vm.permisoRespondido(true)
        }
    }

    LaunchedEffect(Unit) {
        vm.eventos.collect { aviso ->
            val mostrado = Notificador.mostrar(contexto, aviso)
            // TODO Paso 8: si no se mostró, avisar al ViewModel
        }
    }

    // TODO Paso 6: val escuchando by vm.escuchandoAvion.collectAsState()
    // DisposableEffect(escuchando) {
    //     if (escuchando) {
    //         // TODO 1: crear ModoAvionReceiver { activo -> vm.alCambiarModoAvion(activo) }
    //         // TODO 2: registrarlo con RECEIVER_NOT_EXPORTED
    //         onDispose { /* TODO 3: unregisterReceiver */ }
    //     } else {
    //         onDispose {}
    //     }
    // }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "AvisosCampus",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Tarjeta 1: Permiso de notificaciones
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Permiso de notificaciones",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Estado: ")
                        val (etiqueta, colorFondo, colorTexto) = when (permiso) {
                            PermisoUi.SinPedir -> Triple("Sin pedir", Color.LightGray.copy(alpha = 0.4f), Color.DarkGray)
                            PermisoUi.Concedido -> Triple("Permitido", Color(0xFFC8E6C9), Color(0xFF2E7D32))
                            PermisoUi.Denegado -> Triple("Denegado", Color(0xFFFFCDD2), Color(0xFFC62828))
                        }
                        Text(
                            text = etiqueta,
                            color = colorTexto,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .background(colorFondo, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Button(
                        onClick = {
                            if (Notificador.tienePermiso(contexto)) {
                                vm.permisoRespondido(true)
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                lanzador.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                        }
                    ) {
                        Text("Permitir notificaciones")
                    }
                }
            }

            // Tarjeta 2: Avisos
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Avisos",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            vm.avisarAhora()
                        }
                    ) {
                        Text("Mostrar aviso")
                    }
                }
            }

            // Tarjeta 3: Recordatorio
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Recordatorio",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                // TODO Paso 5: vm.recordarEn(10)
                            }
                        ) {
                            Text("Recordar en 10 s")
                        }
                        OutlinedButton(
                            onClick = {
                                // TODO Paso 5: vm.cancelarRecordatorio()
                            },
                            enabled = false
                        ) {
                            Text("Cancelar")
                        }
                    }
                    // TODO Paso 5: mostrar Text("Faltan $faltan s") cuando faltan != null
                }
            }

            // Tarjeta 4: Receptor de anuncios
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Receptor de anuncios",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Escuchar el modo avión")
                        Switch(
                            checked = false, // TODO Paso 6: escuchando
                            onCheckedChange = {
                                // TODO Paso 6: vm.cambiarEscucha(it)
                            }
                        )
                    }
                    Text(
                        text = "Activa o desactiva el modo avión del celular",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // TODO Paso 7: HistorialAvisos(avisos = avisos)

            // TODO Paso 8: Recuadro de error y botón "Abrir ajustes" cuando mensaje != null
        }
    }
}