package com.merino.carritotecsup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.merino.carritotecsup.ui.theme.Lab04CarritoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }

            Lab04CarritoTheme(darkTheme = isDarkTheme) {
                AppNavegacion(
                    isDarkTheme = isDarkTheme,
                    onToggleDarkTheme = { isDarkTheme = it }
                )
            }
        }
    }
}
