package com.merino.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AllInclusive
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.GpsFixed
import androidx.compose.material.icons.rounded.LocalParking
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.NearMe
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.merino.campusgps.model.UbicacionUi
import com.merino.campusgps.util.formatearDistancia
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

    val categorias = listOf(
        Triple("Todas", Icons.Rounded.AllInclusive, "Todas"),
        Triple("Estudio", Icons.Rounded.MenuBook, "Estudio"),
        Triple("Comida", Icons.Rounded.Restaurant, "Comida"),
        Triple("Eventos", Icons.Rounded.Event, "Eventos"),
        Triple("Servicios", Icons.Rounded.LocalParking, "Servicios")
    )
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
                title = { Text("CampusGPS", fontWeight = FontWeight.Bold) },
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
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Zona a) Mapa (42% de altura de pantalla con bordes inferiores redondeados y sombra suave)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.42f)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            ) {
                MapaUbicacion(
                    estado = estado,
                    lugares = lugaresAMostrar,
                    seleccionado = seleccionado,
                    tienePermiso = estado is UbicacionUi.Lista,
                    radioMetros = radioMetros,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Zona b) Panel de Estado y Acción Rápida (Surface con fondo #F4F7FB)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                color = Color(0xFFF4F7FB),
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { alPulsar() },
                            enabled = estado !is UbicacionUi.Buscando,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
                        ) {
                            Icon(Icons.Rounded.GpsFixed, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Actualizar ubicación", style = MaterialTheme.typography.labelMedium)
                        }

                        OutlinedButton(
                            onClick = {
                                masCercano?.let { cercano ->
                                    vm.seleccionar(cercano.id)
                                }
                            },
                            modifier = Modifier.weight(1f),
                            border = BorderStroke(1.dp, Color(0xFF1565C0))
                        ) {
                            Icon(Icons.Rounded.NearMe, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color(0xFF1565C0))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Ir al más cercano", style = MaterialTheme.typography.labelMedium, color = Color(0xFF1565C0))
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    when (val e = estado) {
                        is UbicacionUi.Inicial -> Text("Pulsa 'Actualizar ubicación' para empezar", style = MaterialTheme.typography.bodySmall)
                        is UbicacionUi.Buscando -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                        is UbicacionUi.Lista -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Lat: ${String.format("%.4f", e.posicion.latitud)} | Lon: ${String.format("%.4f", e.posicion.longitud)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                masCercano?.let { cercano ->
                                    val distanciaTexto = lugaresAMostrar.find { it.lugar.id == cercano.id }?.metros?.let { " (${formatearDistancia(it)})" } ?: ""
                                    Text(
                                        text = "Más cercano: ${cercano.nombre}$distanciaTexto",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2E7D32)
                                    )
                                }
                            }
                        }
                        is UbicacionUi.Error -> {
                            MensajeError(
                                error = e,
                                onReintentar = { alPulsar() }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Zona c) Barra de Filtros Dual (Categoría + Radio)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Fila 1: LazyRow con FilterChip por categoría con iconos
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categorias) { (cat, icon, label) ->
                        FilterChip(
                            selected = cat.equals(categoriaSeleccionada, ignoreCase = true),
                            onClick = { vm.seleccionarCategoria(cat) },
                            label = { Text(label) },
                            leadingIcon = {
                                Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        )
                    }
                }

                // Fila 2: Row compacta con etiqueta "Radio:" y 3 FilterChip (100m, 200m, 500m)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Radio:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    radios.forEach { (radioValor, label) ->
                        FilterChip(
                            selected = radioValor == radioMetros,
                            onClick = { vm.cambiarRadio(radioValor) },
                            label = { Text(label) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Zona d) Lista de Lugares ocupando el espacio restante (weight(1f))
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
