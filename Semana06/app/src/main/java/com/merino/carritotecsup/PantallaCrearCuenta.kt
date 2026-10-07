package com.merino.carritotecsup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCrearCuenta(
    onNavigateBack: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var referencia by remember { mutableStateOf("") }

    var isNombreError by remember { mutableStateOf(false) }
    var isTelefonoError by remember { mutableStateOf(false) }
    var isDireccionError by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Cuenta - Mi Bodega") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Registro de Cliente",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Ingresa tus datos para realizar tus pedidos con mayor rapidez.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    isNombreError = false
                    mensajeError = ""
                },
                label = { Text("Nombre Completo *") },
                isError = isNombreError,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = telefono,
                onValueChange = {
                    telefono = it
                    isTelefonoError = false
                    mensajeError = ""
                },
                label = { Text("Teléfono / Celular *") },
                isError = isTelefonoError,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = direccion,
                onValueChange = {
                    direccion = it
                    isDireccionError = false
                    mensajeError = ""
                },
                label = { Text("Dirección de Entrega *") },
                isError = isDireccionError,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = referencia,
                onValueChange = { referencia = it },
                label = { Text("Referencia (Opcional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            if (mensajeError.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = mensajeError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val nBlank = nombre.trim().isBlank()
                    val tBlank = telefono.trim().isBlank()
                    val dBlank = direccion.trim().isBlank()

                    isNombreError = nBlank
                    isTelefonoError = tBlank
                    isDireccionError = dBlank

                    if (nBlank || tBlank || dBlank) {
                        mensajeError = "Completa los campos obligatorios (*)"
                    } else {
                        onRegisterSuccess()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("REGISTRARSE")
            }
        }
    }
}
