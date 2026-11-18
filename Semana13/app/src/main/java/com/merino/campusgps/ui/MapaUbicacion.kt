package com.merino.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
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

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    tienePermiso: Boolean = false,
    radioMetros: Double = 200.0,
    modifier: Modifier = Modifier
) {
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.0464, -77.0428), 12f)
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
        properties = MapProperties(isMyLocationEnabled = tienePermiso),
        uiSettings = MapUiSettings(
            myLocationButtonEnabled = tienePermiso,
            zoomControlsEnabled = true
        )
    ) {
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
