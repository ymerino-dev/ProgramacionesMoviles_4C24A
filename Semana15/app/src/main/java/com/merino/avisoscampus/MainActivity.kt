package com.merino.avisoscampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.merino.avisoscampus.data.AvisosRepositoryMemoria
import com.merino.avisoscampus.ui.PantallaAvisos
import com.merino.avisoscampus.ui.theme.AvisosCampusTheme
import com.merino.avisoscampus.viewmodel.AvisosViewModel
import com.merino.avisoscampus.viewmodel.AvisosViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repo = AvisosRepositoryMemoria()
        setContent {
            AvisosCampusTheme {
                val vm: AvisosViewModel = viewModel(
                    factory = AvisosViewModelFactory(repo)
                )
                PantallaAvisos(vm)
            }
        }
    }
}