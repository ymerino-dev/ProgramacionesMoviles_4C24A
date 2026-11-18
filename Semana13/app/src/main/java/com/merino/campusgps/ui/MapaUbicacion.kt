package com.merino.campusgps.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.merino.campusgps.model.LugarConDistancia
import com.merino.campusgps.model.UbicacionUi
import com.merino.campusgps.util.formatearDistancia

// COMENTARIO [Estilo Oscuro]: Definición JSON del estilo nocturno para Google Maps
private const val ESTILO_MAPA_OSCURO_JSON = """
[
  {"elementType": "geometry", "stylers": [{"color": "#242f3e"}]},
  {"elementType": "labels.text.stroke", "stylers": [{"color": "#242f3e"}]},
  {"elementType": "labels.text.fill", "stylers": [{"color": "#746855"}]},
  {"featureType": "administrative.locality", "elementType": "labels.text.fill", "stylers": [{"color": "#d59563"}]},
  {"featureType": "poi", "elementType": "labels.text.fill", "stylers": [{"color": "#d59563"}]},
  {"featureType": "poi.park", "elementType": "geometry", "stylers": [{"color": "#263c3f"}]},
  {"featureType": "road", "elementType": "geometry", "stylers": [{"color": "#38414e"}]},
  {"featureType": "road.highway", "elementType": "geometry", "stylers": [{"color": "#746855"}]},
  {"featureType": "water", "elementType": "geometry", "stylers": [{"color": "#17263c"}]}
]
"""

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    tienePermiso: Boolean = false,
    radioMetros: Double = 200.0,
    modifier: Modifier = Modifier,
    esModoOscuro: Boolean = isSystemInDarkTheme()
) {
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.0464, -77.0428), 12f)
    }

    // COMENTARIO [MapStyleOptions]: Se carga el estilo oscuro dinámicamente según el tema del sistema
    val estiloMapa = remember(esModoOscuro) {
        if (esModoOscuro) MapStyleOptions(ESTILO_MAPA_OSCURO_JSON) else null
    }

    LaunchedEffect(estado) {
        if (estado is UbicacionUi.Lista) {
            val pos = LatLng(estado.posicion.latitud, estado.posicion.longitud)
            camara.animate(CameraUpdateFactory.newLatLngZoom(pos, 16f))
        }
    }

    LaunchedEffect(seleccionado) {
        val item = lugares.find { it.lugar.id == seleccionado }
        if (item != null) {
            val destino = LatLng(item.lugar.latitud, item.lugar.longitud)
            camara.animate(CameraUpdateFactory.newLatLngZoom(destino, 17f))
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = camara,
        properties = MapProperties(
            isMyLocationEnabled = tienePermiso,
            mapStyleOptions = estiloMapa
        ),
        uiSettings = MapUiSettings(
            myLocationButtonEnabled = tienePermiso,
            zoomControlsEnabled = true
        )
    ) {
        // COMENTARIO [Circle de Cercanía]: Dibuja el radio de tolerancia (100m/200m/500m) en torno al usuario
        if (estado is UbicacionUi.Lista) {
            val pos = LatLng(estado.posicion.latitud, estado.posicion.longitud)
            Circle(
                center = pos,
                radius = radioMetros,
                fillColor = Color(0x221565C0),
                strokeColor = Color(0xFF1565C0),
                strokeWidth = 2f
            )
            Marker(
                state = rememberUpdatedMarkerState(position = pos),
                title = "Estás aquí"
            )
        }

        lugares.forEach { item ->
            val esSeleccionado = item.lugar.id == seleccionado
            val posLugar = LatLng(item.lugar.latitud, item.lugar.longitud)
            val colorMarcador = if (esSeleccionado) {
                BitmapDescriptorFactory.HUE_AZURE
            } else {
                BitmapDescriptorFactory.HUE_ORANGE
            }
            val textoDistancia = item.metros?.let { " • ${formatearDistancia(it)}" } ?: ""

            Marker(
                state = rememberUpdatedMarkerState(position = posLugar),
                title = item.lugar.nombre,
                snippet = "${item.lugar.categoria}$textoDistancia",
                icon = BitmapDescriptorFactory.defaultMarker(colorMarcador)
            )
        }
    }
}
