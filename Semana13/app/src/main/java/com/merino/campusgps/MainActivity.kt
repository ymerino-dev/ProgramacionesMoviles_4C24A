package com.merino.campusgps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.merino.campusgps.ui.theme.CampusGPSTheme
import com.merino.campusgps.ui.PantallaMapa
import com.merino.campusgps.viewmodel.CampusViewModelFactory
import com.merino.campusgps.viewmodel.MapaViewModel

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