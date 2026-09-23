package com.merinoana.navlab.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.merinoana.navlab.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Creamos 15 elementos de ejemplo
            items(15) { index ->
                val numero = index + 1
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(Screen.Detail.createRoute(numero))
                        }
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Elemento número $numero",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Toca para ver el detalle",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Divider(color = MaterialTheme.colorScheme.surfaceVariant)
            }
        }
    }
}