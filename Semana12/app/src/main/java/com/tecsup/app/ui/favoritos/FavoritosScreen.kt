package com.tecsup.app.ui.favoritos

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosScreen(viewModel: FavoritosViewModel) {
    val favoritos by viewModel.favoritos.collectAsState()
    Scaffold(
        topBar = { TopAppBar(title = { Text("Mis Favoritos") }) }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Text(text = "Total de favoritos: ${favoritos.size}", modifier = Modifier.padding(16.dp))
        }
    }
}