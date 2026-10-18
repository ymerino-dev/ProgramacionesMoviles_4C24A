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
import com.tecsup.cafecampus.ui.cliente.LoginScreen
import com.tecsup.cafecampus.ui.theme.CafeCampusTheme
import com.tecsup.cafecampus.viewmodel.SesionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Instanciamos la Base de Datos y el DataStore (El mueble y la pizarra)
        val db = CafeDatabase.obtener(applicationContext)
        val prefs = PreferenciasStore(applicationContext)

        // 2. Creamos al Gerente (Repository) y le pasamos los bibliotecarios (DAOs)
        val repository = CafeRepositoryRoom(
            usuarioDao = db.usuarioDao(),
            productoDao = db.productoDao(),
            pedidoDao = db.pedidoDao()
        )

        // 3. Creamos el Cerebro de la pantalla (ViewModel)
        val sesionViewModel = SesionViewModel(repository, prefs)

        setContent {
            CafeCampusTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 4. ¡Mostramos la cara de la app!
                    LoginScreen(
                        vm = sesionViewModel,
                        onLoginSuccess = { rol ->
                            // Más adelante conectaremos esto a la navegación real.
                            // Por ahora, si el login es correcto, solo cambiará el color del botón.
                            println("¡Login exitoso! Bienvenido, $rol")
                        }
                    )
                }
            }
        }
    }
}