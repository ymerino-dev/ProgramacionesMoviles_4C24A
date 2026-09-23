package com.merinoana.navlab.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.merinoana.navlab.navigation.Screen

@Composable
fun ListScreen(navController: NavController) {
    // Lista simulada de datos
    val items = listOf(
        "Elemento 1" to 1,
        "Elemento 2" to 2,
        "Elemento 3" to 3,
        "Elemento 4" to 4,
        "Elemento 5" to 5
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Lista de Elementos",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            items(items) { (name, id) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            // Navega a Detail pasando el ID
                            navController.navigate(Screen.Detail.createRoute(id))
                        }
                ) {
                    Text(
                        text = name,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}