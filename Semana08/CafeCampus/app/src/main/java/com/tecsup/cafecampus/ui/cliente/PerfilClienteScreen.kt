package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.sp
import com.tecsup.cafecampus.data.local.entity.Usuario
import com.tecsup.cafecampus.data.prefs.PreferenciasStore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilClienteScreen(
    usuario: Usuario?,
    prefs: PreferenciasStore,
    onCerrarSesion: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val colorCliente = Color(0xFFEA580C) // Naranja corporativo del estudiante

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mi Perfil") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Círculo con icono de usuario
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFFFFEDD5), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Perfil",
                    tint = colorCliente,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Datos del estudiante
            Text(
                text = usuario?.nombre ?: "Estudiante",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = usuario?.correo ?: "estudiante@cafe.test",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = Color(0xFFFFEDD5),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Rol: Estudiante",
                    color = colorCliente,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(24.dp))

            // Botón de Cerrar Sesión exclusivo del estudiante
            OutlinedButton(
                onClick = {
                    scope.launch {
                        prefs.cerrarSesion()
                    }
                    onCerrarSesion()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cerrar sesión", fontWeight = FontWeight.Bold)
            }
        }
    }
}