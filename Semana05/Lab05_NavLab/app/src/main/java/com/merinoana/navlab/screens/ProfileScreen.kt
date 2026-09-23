package com.merinoana.navlab.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.merinoana.navlab.navigation.Screen

@Composable
fun ProfileScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mi Perfil",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        // ¡Aquí están tus datos modificados!
        Text(
            text = "Ana Yanira Merino Ramos",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            // Regresa a Home y limpia la pila de navegación
            onClick = { navController.navigate(Screen.Home.route) { popUpTo(0) } },
            modifier = Modifier.fillMaxWidth(0.8f) // Hace el botón un poco más ancho, como en la imagen
        ) {
            Text("Ir al inicio")
        }
    }
}