package com.tecsup.cafecampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.tecsup.cafecampus.data.local.CafeDatabase
import com.tecsup.cafecampus.data.prefs.PreferenciasStore
import com.tecsup.cafecampus.data.repository.CafeRepositoryRoom
import com.tecsup.cafecampus.ui.cliente.MisPedidosScreen
import com.tecsup.cafecampus.ui.cliente.LoginScreen
import com.tecsup.cafecampus.ui.theme.CafeCampusTheme
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
        val pedidosViewModel = PedidosClienteViewModel(repository)

        setContent {
            CafeCampusTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // PRUEBA DIRECTA: Puedes cambiar esto entre LoginScreen y MisPedidosScreen
                    LoginScreen(
                        vm = sesionViewModel,
                        onLoginSuccess = { rol ->
                            println("Acceso concedido como: $rol")
                            // Aquí pondremos la navegación completa en el siguiente paso
                        }
                    )

                    // Si quieres probar directamente tus pedidos, descomenta la línea de abajo y comenta la de arriba:
                    // MisPedidosScreen(vmPedidos = pedidosViewModel, onVerDetalle = { idPedido -> })
                }
            }
        }
    }
}