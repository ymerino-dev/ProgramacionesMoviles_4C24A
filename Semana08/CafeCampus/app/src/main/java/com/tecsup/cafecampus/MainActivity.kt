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
import com.tecsup.cafecampus.ui.admin.PedidosAdminScreen
import com.tecsup.cafecampus.ui.cliente.ConfirmarPedidoScreen
import com.tecsup.cafecampus.ui.cliente.DetalleProductoScreen
import com.tecsup.cafecampus.ui.cliente.LoginScreen
import com.tecsup.cafecampus.ui.theme.CafeCampusTheme
import com.tecsup.cafecampus.viewmodel.MenuViewModel
import com.tecsup.cafecampus.viewmodel.PedidosAdminViewModel
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel
import com.tecsup.cafecampus.viewmodel.SesionViewModel

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

        val sesionViewModel = SesionViewModel(repository, prefs)
        val menuViewModel = MenuViewModel(repository)
        val pedidosClienteViewModel = PedidosClienteViewModel(repository)
        val pedidosAdminViewModel = PedidosAdminViewModel(repository)

        setContent {
            CafeCampusTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Navegación de emergencia para probar el laboratorio
                    var pantallaActual by remember { mutableStateOf("LOGIN") }

                    when (pantallaActual) {
                        "LOGIN" -> LoginScreen(
                            vm = sesionViewModel,
                            onLoginSuccess = { rol ->
                                if (rol == "CLIENTE") {
                                    pantallaActual = "COMPRAR"
                                } else {
                                    pantallaActual = "ADMIN"
                                }
                            }
                        )

                        "COMPRAR" -> {
                            // Cargamos el primer producto para hacer una compra rápida
                            val productos by menuViewModel.productos.collectAsState()
                            val carrito by menuViewModel.carrito.collectAsState()

                            if (carrito.isEmpty() && productos.isNotEmpty()) {
                                DetalleProductoScreen(
                                    producto = productos.first(),
                                    vm = menuViewModel,
                                    onVolver = { pantallaActual = "CONFIRMAR" }
                                )
                            }
                        }

                        "CONFIRMAR" -> {
                            val carrito by menuViewModel.carrito.collectAsState()
                            val usuario by sesionViewModel.usuarioActual.collectAsState()

                            ConfirmarPedidoScreen(
                                clienteId = usuario?.id ?: 1,
                                carrito = carrito,
                                total = menuViewModel.total(),
                                vmPedidos = pedidosClienteViewModel,
                                onSeguirPidiendo = { },
                                onConfirmado = {
                                    menuViewModel.vaciar()
                                    pantallaActual = "ADMIN" // Tras confirmar, saltamos directo al admin para ver el pedido
                                }
                            )
                        }

                        "ADMIN" -> PedidosAdminScreen(vmAdmin = pedidosAdminViewModel)
                    }
                }
            }
        }
    }
}