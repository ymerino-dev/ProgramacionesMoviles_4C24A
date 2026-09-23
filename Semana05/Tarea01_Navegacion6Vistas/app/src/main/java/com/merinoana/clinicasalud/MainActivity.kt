package com.merinoana.clinicasalud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.merinoana.clinicasalud.screens.MainScreen
import com.merinoana.clinicasalud.ui.theme.ClinicaSaludTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           
            ClinicaSaludTheme {
                val navController = rememberNavController()
                MainScreen(navController = navController)
            }
        }
    }
}