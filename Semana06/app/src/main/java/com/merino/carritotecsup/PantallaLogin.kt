package com.merino.carritotecsup

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun PantallaLogin(
    onNavigateToCrearCuenta: () -> Unit,
    onNavigateToInicio: () -> Unit
) {
    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isUsuarioError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }

    // Usuario y contraseña fijos de prueba
    val usuarioValido = "admin@tecsup.edu.pe"
    val passwordValida = "123456"

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(80.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Logo Mi Bodega",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Mi Bodega TECSUP",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Inicia sesión para continuar",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = usuario,
                onValueChange = {
                    usuario = it
                    isUsuarioError = false
                    errorMessage = ""
                },
                label = { Text("Correo o Usuario") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                isError = isUsuarioError,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    isPasswordError = false
                    errorMessage = ""
                },
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                isError = isPasswordError,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Demostración: admin@tecsup.edu.pe / 123456",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val usuarioTrim = usuario.trim()
                    val passwordTrim = password.trim()

                    if (usuarioTrim.isBlank()) {
                        isUsuarioError = true
                        errorMessage = "El usuario no puede estar vacío"
                        return@Button
                    }

                    if (passwordTrim.isBlank()) {
                        isPasswordError = true
                        errorMessage = "La contraseña no puede estar vacía"
                        return@Button
                    }

                    if (usuarioTrim == usuarioValido && passwordTrim == passwordValida) {
                        onNavigateToInicio()
                    } else {
                        isUsuarioError = true
                        isPasswordError = true
                        errorMessage = "Usuario o contraseña incorrectos. Usa admin@tecsup.edu.pe / 123456"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("INGRESAR")
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = onNavigateToCrearCuenta
            ) {
                Text("¿No tienes cuenta? Regístrate aquí")
            }
        }
    }
}
