package com.tecsup.cafecampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.tecsup.cafecampus.data.local.CafeDatabase
import com.tecsup.cafecampus.data.prefs.PreferenciasStore
import com.tecsup.cafecampus.data.repository.CafeRepositoryRoom
import com.tecsup.cafecampus.ui.cliente.MisPedidosScreen
import com.tecsup.cafecampus.ui.cliente.SeguimientoScreen
import com.tecsup.cafecampus.ui.theme.CafeCampusTheme
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = CafeDatabase.obtener(applicationContext)
        val prefs = PreferenciasStore(applicationContext)
        val repository = CafeRepositoryRoom(
            usuarioDao = db.usuarioDao(),
            productoDao = db.productoDao(),
            pedidoDao = db.pedidoDao()
        )

        // Instanciamos el cerebro del cliente
        val pedidosClienteViewModel = PedidosClienteViewModel(repository)

        setContent {
            CafeCampusTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Simulamos que el estudiante "Juan" (ID 1) está logueado
                    LaunchedEffect(Unit) {
                        pedidosClienteViewModel.setClienteId(1)
                    }

                    // Navegación de prueba para el flujo del Cliente
                    var pantallaCliente by remember { mutableStateOf("MIS_PEDIDOS") }
                    var pedidoSeleccionadoId by remember { mutableIntStateOf(0) }

                    when (pantallaCliente) {
                        "MIS_PEDIDOS" -> {
                            MisPedidosScreen(
                                vmPedidos = pedidosClienteViewModel,
                                onVerDetalle = { id ->
                                    pedidoSeleccionadoId = id
                                    pantallaCliente = "SEGUIMIENTO"
                                }
                            )
                        }
                        "SEGUIMIENTO" -> {
                            SeguimientoScreen(
                                pedidoId = pedidoSeleccionadoId,
                                vmPedidos = pedidosClienteViewModel,
                                onVolver = { pantallaCliente = "MIS_PEDIDOS" }
                            )
                        }
                    }
                }
            }
        }
    }
}