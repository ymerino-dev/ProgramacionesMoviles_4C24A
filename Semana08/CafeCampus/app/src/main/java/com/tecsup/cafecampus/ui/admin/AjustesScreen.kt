package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.data.local.entity.Usuario
import com.tecsup.cafecampus.data.prefs.PreferenciasStore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AjustesScreen(
    usuario: Usuario?,
    prefs: PreferenciasStore,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    // Leemos los valores del DataStore en tiempo real
    val modoOscuro by prefs.modoOscuro.collectAsState(initial = false)
    val recibirPedidos by prefs.recibirPedidos.collectAsState(initial = true)

    // Necesitamos un "lanzador" para ejecutar las funciones suspendidas de guardado
    val scope = rememberCoroutineScope()

    val colorAdmin = Color(0xFF4F46E5)

    Scaffold(
        topBar = { TopAppBar(title = { Text("Ajustes") }) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {

            // 1. Tarjeta del perfil actual
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(50.dp).background(Color(0xFFE0E7FF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = "Perfil", tint = colorAdmin)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(usuario?.nombre ?: "Administrador", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(usuario?.correo ?: "admin@cafe.test", color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Botón para cambiar de perfil (Modo Práctica)
            Button(
                onClick = onCambiarPerfil,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorAdmin),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cambiar de perfil (Modo práctica)")
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text("Preferencias", fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))

            // 3. Switch de Modo Oscuro
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Modo oscuro", fontWeight = FontWeight.Bold)
                    Text("Cambia el tema de la app", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                }
                Switch(
                    checked = modoOscuro,
                    onCheckedChange = { activado ->
                        scope.launch { prefs.guardarModoOscuro(activado) }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Switch de Recibir Pedidos
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Recibir pedidos", fontWeight = FontWeight.Bold)
                    Text("Abre o cierra la cafetería", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                }
                Switch(
                    checked = recibirPedidos,
                    onCheckedChange = { activado ->
                        scope.launch { prefs.guardarRecibirPedidos(activado) }
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(32.dp))

            // 5. Botón de Cerrar Sesión
            OutlinedButton(
                onClick = {
                    scope.launch { prefs.cerrarSesion() }
                    onCerrarSesion()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}

// Pequeña extensión para dibujar el círculo del perfil
private val CircleShape = RoundedCornerShape(50)