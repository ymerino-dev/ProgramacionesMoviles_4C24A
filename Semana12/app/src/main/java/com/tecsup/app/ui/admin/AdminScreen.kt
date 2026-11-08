package com.tecsup.app.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(viewModel: AdminViewModel) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Panel de Administración") }) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)
        ) {
            Text(text = "Bienvenido al panel de control de administrador", style = MaterialTheme.typography.titleMedium)
        }
    }
}