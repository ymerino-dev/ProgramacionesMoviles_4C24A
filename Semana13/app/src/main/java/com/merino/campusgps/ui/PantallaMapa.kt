package com.merino.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    val contexto = LocalContext.current

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        // TODO 1: si algún valor es true -> vm.buscar(); si no -> vm.permisoDenegado()
        if (resultado.values.any { it }) {
            vm.buscar()
        } else {
            vm.permisoDenegado()
        }
    }

    fun alPulsar() {
        // TODO 2: ¿ya tienes FINE o COARSE? -> vm.buscar()
        val tieneFine = ContextCompat.checkSelfPermission(
            contexto, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val tieneCoarse = ContextCompat.checkSelfPermission(
            contexto, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (tieneFine || tieneCoarse) {
            vm.buscar()
        } else {
            // TODO 3: si no -> lanzador.launch(arrayOf(FINE, COARSE))
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
            MapaUbicacion(
                estado = estado,
                lugares = lugares,
                seleccionado = seleccionado,
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

            Spacer(modifier = Modifier.height(12.dp))

            when (val e = estado) {
                is UbicacionUi.Inicial -> Text("Pulsa el botón para empezar")
                is UbicacionUi.Buscando -> CircularProgressIndicator()
                is UbicacionUi.Lista -> {
                    Text("Latitud: ${e.posicion.latitud}")
                    Text("Longitud: ${e.posicion.longitud}")
                }
                is UbicacionUi.Error -> {
                    Text(
                        text = e.mensaje,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            ListaLugares(
                lugares = lugares,
                seleccionado = seleccionado,
                onSeleccionar = { /* TODO Paso 6: vm.seleccionar(it) */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}