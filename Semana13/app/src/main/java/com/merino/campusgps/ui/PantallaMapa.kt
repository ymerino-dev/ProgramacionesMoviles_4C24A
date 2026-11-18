package com.merino.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.merino.campusgps.model.UbicacionUi
import com.merino.campusgps.viewmodel.MapaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {
    val estado by vm.estado.collectAsState()
    val lugares by vm.lugares.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()
    val contexto = LocalContext.current

    val categoriaSeleccionada = (estado as? UbicacionUi.Lista)?.categoriaSeleccionada ?: "Todas"
    val radioMetros = (estado as? UbicacionUi.Lista)?.radioMetros ?: 200.0
    val lugaresAMostrar = (estado as? UbicacionUi.Lista)?.lugaresFiltrados ?: lugares

    val categorias = listOf("Todas", "Estudio", "Comida", "Eventos", "Servicios")
    val radios = listOf(100.0 to "100 m", 200.0 to "200 m", 500.0 to "500 m")

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        if (resultado.values.any { it }) {
            vm.buscar()
        } else {
            vm.permisoDenegado()
        }
    }

    fun alPulsar() {
        val tieneFine = ContextCompat.checkSelfPermission(
            contexto, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val tieneCoarse = ContextCompat.checkSelfPermission(
            contexto, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (tieneFine || tieneCoarse) {
            vm.buscar()
        } else {
            lanzador.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CampusGPS") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Filtro por categorías (FilterChips)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categorias.forEach { cat ->
                    FilterChip(
                        selected = cat.equals(categoriaSeleccionada, ignoreCase = true),
                        onClick = { vm.seleccionarCategoria(cat) },
                        label = { Text(cat) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Selector de radio de cercanía (FilterChips)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                radios.forEach { (radioValor, label) ->
                    FilterChip(
                        selected = radioValor == radioMetros,
                        onClick = { vm.cambiarRadio(radioValor) },
                        label = { Text(label) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            MapaUbicacion(
                estado = estado,
                lugares = lugaresAMostrar,
                seleccionado = seleccionado,
                tienePermiso = estado is UbicacionUi.Lista,
                radioMetros = radioMetros,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { alPulsar() },
                enabled = estado !is UbicacionUi.Buscando
            ) {
                Text("Obtener mi ubicación")
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (val e = estado) {
                is UbicacionUi.Inicial -> Text("Pulsa el botón para empezar")
                is UbicacionUi.Buscando -> CircularProgressIndicator()
                is UbicacionUi.Lista -> {
                    Text(
                        text = "Lat: ${e.posicion.latitud} | Lon: ${e.posicion.longitud}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    masCercano?.let { cercano ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Más cercano: ${cercano.nombre}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                is UbicacionUi.Error -> {
                    MensajeError(
                        error = e,
                        onReintentar = { alPulsar() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            ListaLugares(
                lugares = lugaresAMostrar,
                seleccionado = seleccionado,
                onSeleccionar = { vm.seleccionar(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}
