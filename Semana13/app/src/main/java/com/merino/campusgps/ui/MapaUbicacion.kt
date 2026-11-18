package com.merino.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
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
    modifier: Modifier = Modifier
) {
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.0464, -77.0428), 12f)
    }

    // TODO 1: LaunchedEffect(estado): si es Lista, animar la cámara (zoom 16f)
    LaunchedEffect(estado) {
        if (estado is UbicacionUi.Lista) {
            val pos = LatLng(estado.posicion.latitud, estado.posicion.longitud)
            camara.animate(CameraUpdateFactory.newLatLngZoom(pos, 16f))
        }
    }

    // Paso 6: animar la cámara al lugar seleccionado cuando cambia `seleccionado`
    LaunchedEffect(seleccionado) {
        val item = lugares.find { it.lugar.id == seleccionado }
        if (item != null) {
            val destino = LatLng(item.lugar.latitud, item.lugar.longitud)
            camara.animate(CameraUpdateFactory.newLatLngZoom(destino, 17f))
        }
    }

    GoogleMap(modifier = modifier, cameraPositionState = camara) {
        // TODO 2: si el estado es Lista, un Marker "Estás aquí"
        if (estado is UbicacionUi.Lista) {
            val pos = LatLng(estado.posicion.latitud, estado.posicion.longitud)
            Marker(
                state = rememberUpdatedMarkerState(position = pos),
                title = "Estás aquí"
            )
        }

        // TODO 3 (Paso 4): un Marker por cada lugar
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