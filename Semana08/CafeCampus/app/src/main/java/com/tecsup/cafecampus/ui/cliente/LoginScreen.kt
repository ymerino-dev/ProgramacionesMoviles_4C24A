package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.viewmodel.SesionViewModel

@Composable
fun LoginScreen(
    vm: SesionViewModel,
    onLoginSuccess: (String) -> Unit // Función para navegar cuando el login es exitoso
) {
    // 1. Observamos los datos que nos envía el cerebro (ViewModel)
    val usuarioActual by vm.usuarioActual.collectAsState()
    val error by vm.error.collectAsState()

    // 2. Variables para las cajas de texto y el selector
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var rolSeleccionado by remember { mutableStateOf("CLIENTE") }

    // 3. Efecto mágico: Si el ViewModel nos dice que ya hay usuario, saltamos la pantalla
    LaunchedEffect(usuarioActual) {
        usuarioActual?.let {
            onLoginSuccess(it.rol) // Avisa a la navegación que nos cambie de pantalla
        }
    }

    // 4. Colores oficiales del laboratorio según el rol seleccionado
    val colorCliente = Color(0xFFEA580C) // Naranja
    val colorAdmin = Color(0xFF4F46E5)   // Índigo
    val colorActivo = if (rolSeleccionado == "CLIENTE") colorCliente else colorAdmin

    // 5. Diseño visual
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Bienvenido", style = MaterialTheme.typography.headlineLarge)
        Text(text = "Ingresa para continuar", color = Color.Gray)

        Spacer(modifier = Modifier.height(32.dp))

        // Botones para elegir el rol
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    rolSeleccionado = "CLIENTE"
                    vm.limpiarError() // Borra errores al cambiar de pestaña
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (rolSeleccionado == "CLIENTE") colorActivo else Color.LightGray
                )
            ) {
                Text("Estudiante")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    rolSeleccionado = "ADMIN"
                    vm.limpiarError()
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (rolSeleccionado == "ADMIN") colorActivo else Color.LightGray
                )
            ) {
                Text("Administrador")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Cajas de texto
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it; vm.limpiarError() },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = clave,
            onValueChange = { clave = it; vm.limpiarError() },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(), // Oculta la clave con puntitos
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar el texto de error si existe
        if (error != null) {
            Text(text = error!!, color = Color.Red)
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Botón principal de ingreso
        Button(
            onClick = { vm.login(correo, clave, rolSeleccionado) },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorActivo)
        ) {
            Text("Ingresar")
        }
    }
}