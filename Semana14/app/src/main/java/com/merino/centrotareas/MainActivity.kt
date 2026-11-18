package com.merino.centrotareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.merino.centrotareas.data.TareasRepositoryLocal
import com.merino.centrotareas.ui.PantallaTareas
import com.merino.centrotareas.ui.theme.CentroTareasTheme
import com.merino.centrotareas.viewmodel.TareasViewModel
import com.merino.centrotareas.viewmodel.TareasViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = TareasRepositoryLocal()
        val factory = TareasViewModelFactory(repository)

        setContent {
            CentroTareasTheme {
                val tareasViewModel: TareasViewModel = viewModel(factory = factory)
                PantallaTareas(viewModel = tareasViewModel)
            }
        }
    }
}