package com.tecsup.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.tecsup.app.ui.navigation.AppNav
import com.tecsup.app.ui.theme.CatalogoTheme // (O el tema visual que tenga tu proyecto)

class MainActivity : ComponentActivity() {

    // Inicializamos nuestro contenedor manual de dependencias
    private lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Creamos la instancia pasándole el contexto
        appContainer = AppContainer(applicationContext)

        setContent {
            CatalogoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Llamamos a nuestra navegación principal inyectándole el container
                    AppNav(container = appContainer)
                }
            }
        }
    }
}