package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.cafecampus.data.local.entity.Usuario
import com.tecsup.cafecampus.data.prefs.PreferenciasStore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AjustesScreen(
    usuario: Usuario?,
    prefs: PreferenciasStore,
    onVolver: (() -> Unit)? = null,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val modoOscuro by prefs.modoOscuro.collectAsState(initial = false)
    val notificaciones by prefs.notificaciones.collectAsState(initial = true)
    val recibirPedidos by prefs.recibirPedidos.collectAsState(initial = true)

    val scope = rememberCoroutineScope()
    val colorAdmin = Color(0xFF4F46E5)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajustes", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    if (onVolver != null) {
                        IconButton(onClick = onVolver) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBF7)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(55.dp)
                                .background(Color(0xFFE0E7FF), shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = "Avatar", tint = colorAdmin, modifier = Modifier.size(28.dp))
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = usuario?.nombre ?: "Administrador",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = usuario?.correo ?: "admin@cafe.test",
                                color = Color.Gray,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                color = Color(0xFFDBEAFE),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "Perfil activo",
                                    color = Color(0xFF1D4ED8),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // 2. Botón de "Cambiar de perfil" (Modo práctica) con clickable interactivo
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onCambiarPerfil() }, // <-- Esto garantiza que responda al toque
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colorAdmin)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.SyncAlt, contentDescription = "Cambiar", tint = Color.White)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Cambiar de perfil", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text("Modo práctica: Ir a vista Estudiante", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text("Preferencias", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBF7)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Modo oscuro", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("Cambia el tema de la app", color = Color.Gray, fontSize = 12.sp)
                            }
                            Switch(
                                checked = modoOscuro,
                                onCheckedChange = { activado ->
                                    scope.launch { prefs.guardarModoOscuro(activado) }
                                },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = colorAdmin)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = Color(0xFFE2E8F0))
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Notificaciones", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("Avisar nuevos pedidos", color = Color.Gray, fontSize = 12.sp)
                            }
                            Switch(
                                checked = notificaciones,
                                onCheckedChange = { activado ->
                                    scope.launch { prefs.guardarNotificaciones(activado) }
                                },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = colorAdmin)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = Color(0xFFE2E8F0))
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Recibir pedidos", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("Abrir o cerrar la cafetería", color = Color.Gray, fontSize = 12.sp)
                            }
                            Switch(
                                checked = recibirPedidos,
                                onCheckedChange = { activado ->
                                    scope.launch { prefs.guardarRecibirPedidos(activado) }
                                },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = colorAdmin)
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Se guardan en el dispositivo", color = Color.Gray, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("con DataStore", color = colorAdmin, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onCerrarSesion,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                border = BorderStroke(1.dp, Color(0xFFDC2626))
            ) {
                Text("Cerrar sesión", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

private val CircleShape = RoundedCornerShape(50)