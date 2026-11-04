package com.tecsup.authfirebaseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.FirebaseApp
import com.tecsup.authfirebaseapp.ui.AuthApp
import com.tecsup.authfirebaseapp.ui.theme.AuthFirebaseAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa Firebase con el contexto de la aplicación
        FirebaseApp.initializeApp(this)

        setContent {
            AuthFirebaseAppTheme {
                AuthApp()
            }
        }
    }
}
