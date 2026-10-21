package com.tecsup.cafecampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.tecsup.cafecampus.data.local.CafeDatabase
import com.tecsup.cafecampus.data.prefs.PreferenciasStore
import com.tecsup.cafecampus.data.repository.CafeRepositoryRoom
import com.tecsup.cafecampus.ui.navigation.NavCafe
import com.tecsup.cafecampus.ui.theme.CafeCampusTheme
import com.tecsup.cafecampus.viewmodel.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = CafeDatabase.obtener(applicationContext)
        val prefs = PreferenciasStore(applicationContext)
        val repo = CafeRepositoryRoom(db.usuarioDao(), db.productoDao(), db.pedidoDao())

        // Usamos la fábrica que creaste para generar los ViewModels
        val factory = CafeViewModelFactory(repo, prefs)
        val sesionVm = ViewModelProvider(this, factory)[SesionViewModel::class.java]
        val menuVm = ViewModelProvider(this, factory)[MenuViewModel::class.java]
        val pedidosClienteVm = ViewModelProvider(this, factory)[PedidosClienteViewModel::class.java]
        val panelAdminVm = ViewModelProvider(this, factory)[PanelAdminViewModel::class.java]
        val pedidosAdminVm = ViewModelProvider(this, factory)[PedidosAdminViewModel::class.java]
        val menuAdminVm = ViewModelProvider(this, factory)[MenuAdminViewModel::class.java]

        setContent {
            CafeCampusTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    NavCafe(prefs, sesionVm, menuVm, pedidosClienteVm, panelAdminVm, pedidosAdminVm, menuAdminVm)
                }
            }
            val prefs = PreferenciasStore(applicationContext)
            val modoOscuro by prefs.modoOscuro.collectAsState(initial = false)

            CafeCampusTheme(darkTheme = modoOscuro) { // <-- Lee el modo oscuro persistido
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    NavCafe(prefs, sesionVm, menuVm, pedidosClienteVm, panelAdminVm, pedidosAdminVm, menuAdminVm)
                }
            }

        }
    }
}