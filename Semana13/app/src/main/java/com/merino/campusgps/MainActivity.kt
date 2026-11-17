package com.tecsup.campusgps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.merino.campusgps.ui.theme.CampusGPSTheme
import com.tecsup.campusgps.ui.PantallaMapa
import com.tecsup.campusgps.viewmodel.CampusViewModelFactory
import com.tecsup.campusgps.viewmodel.MapaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CampusGPSTheme {
                val vm: MapaViewModel = viewModel(
                    factory = CampusViewModelFactory(applicationContext)
                )
                PantallaMapa(vm = vm)
            }
        }
    }
}