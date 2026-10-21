package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.cafecampus.viewmodel.SesionViewModel

@Composable
fun LoginScreen(
    vm: SesionViewModel,
    onLoginSuccess: (String) -> Unit
) {
    val usuarioActual by vm.usuarioActual.collectAsState()
    val error by vm.error.collectAsState()

    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var rolSeleccionado by remember { mutableStateOf("CLIENTE") }

    LaunchedEffect(usuarioActual) {
        usuarioActual?.let {
            onLoginSuccess(it.rol)
        }
    }

    // Colores oficiales según la interfaz solicitada
    val colorCliente = Color(0xFFEA580C) // Naranja
    val colorAdmin = Color(0xFF4F46E5)   // Índigo
    val colorActivo = if (rolSeleccionado == "CLIENTE") colorCliente else colorAdmin

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Títulos principales
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = "Bienvenido",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ingresa para continuar",
                    color = Color.Gray,
                    fontSize = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Selector superior de pestañas (Estudiante / Administrador)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF1F5F9), shape = RoundedCornerShape(24.dp))
                .padding(4.dp)
        ) {
            Button(
                onClick = {
                    rolSeleccionado = "CLIENTE"
                    correo = "juan@campus.test"
                    vm.limpiarError()
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (rolSeleccionado == "CLIENTE") colorCliente else Color.Transparent
                )
            ) {
                Text(
                    "Estudiante",
                    color = if (rolSeleccionado == "CLIENTE") Color.White else Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }

            Button(
                onClick = {
                    rolSeleccionado = "ADMIN"
                    correo = "admin@cafe.test"
                    vm.limpiarError()
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (rolSeleccionado == "ADMIN") colorAdmin else Color.Transparent
                )
            ) {
                Text(
                    "Administrador",
                    color = if (rolSeleccionado == "ADMIN") Color.White else Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Campo de Correo con etiqueta flotante e ícono de sobre
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it; vm.limpiarError() },
            label = { Text("Correo") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Icono Correo", tint = colorActivo) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorActivo,
                focusedLabelColor = colorActivo
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Contraseña con etiqueta flotante e ícono de candado
        OutlinedTextField(
            value = clave,
            onValueChange = { clave = it; vm.limpiarError() },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Icono Candado", tint = colorActivo) },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorActivo,
                focusedLabelColor = colorActivo
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Enlace de "¿Olvidaste tu contraseña?" alineado a la derecha
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            TextButton(onClick = { /* Acción de recuperación */ }) {
                Text(
                    text = "¿Olvidaste tu contraseña?",
                    color = colorActivo,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (error != null) {
            Text(text = error!!, color = Color.Red, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Botón principal de Ingresar
        Button(
            onClick = { vm.login(correo, clave, rolSeleccionado) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorActivo)
        ) {
            Text(text = "Ingresar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Contenido condicional inferior según el rol activo
        if (rolSeleccionado == "CLIENTE") {
            // Enlace de registro para estudiantes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(onClick = { /* Acción de registro */ }) {
                    Text(
                        text = "¿No tienes cuenta? ",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "Regístrate",
                        color = colorCliente,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            // Bloque informativo de "Modo práctica" exclusivo para Administrador
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF2F6))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Modo práctica",
                        color = colorAdmin,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Puedes cambiar de perfil desde Ajustes para probar ambos roles.",
                        color = Color.DarkGray,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}