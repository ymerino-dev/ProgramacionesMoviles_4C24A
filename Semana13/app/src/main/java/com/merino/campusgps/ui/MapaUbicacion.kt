package com.merino.campusgps.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color as AndroidColor
import android.graphics.Paint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.TilesOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import com.merino.campusgps.model.LugarConDistancia
import com.merino.campusgps.model.UbicacionUi
import com.merino.campusgps.util.formatearDistancia

// Genera un Bitmap circular limpio de 44x44 px con borde blanco grueso y centro azul (#1565C0)
private fun crearBitmapPuntoAzul(): Bitmap {
    val size = 44
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val paintBorde = Paint().apply {
        isAntiAlias = true
        color = AndroidColor.WHITE
        style = Paint.Style.FILL
    }
    canvas.drawCircle(size / 2f, size / 2f, size / 2f, paintBorde)

    val paintCentro = Paint().apply {
        isAntiAlias = true
        color = AndroidColor.parseColor("#1565C0")
        style = Paint.Style.FILL
    }
    canvas.drawCircle(size / 2f, size / 2f, (size / 2f) - 4f, paintCentro)

    return bitmap
}

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
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Configuración inicial de osmdroid (User Agent y SharedPreferences)
    remember {
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", android.content.Context.MODE_PRIVATE))
        Configuration.getInstance().userAgentValue = context.packageName
        true
    }

    // Instancia persistente del MapView de osmdroid
    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(15.0)
            controller.setCenter(GeoPoint(-12.0464, -77.0428))
        }
    }

    // Gestión del ciclo de vida (onResume, onPause, onDetach) con LifecycleEventObserver
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onDetach()
        }
    }

    // Animación de cámara cuando cambia el estado a Lista (ubicación obtenida)
    LaunchedEffect(estado) {
        if (estado is UbicacionUi.Lista) {
            val point = GeoPoint(estado.posicion.latitud, estado.posicion.longitud)
            mapView.controller.animateTo(point)
            mapView.controller.setZoom(16.0)
        }
    }

    // Animación de cámara al seleccionar un lugar de la lista
    LaunchedEffect(seleccionado) {
        val item = lugares.find { it.lugar.id == seleccionado }
        if (item != null) {
            val point = GeoPoint(item.lugar.latitud, item.lugar.longitud)
            mapView.controller.animateTo(point)
            mapView.controller.setZoom(17.0)
        }
    }

    // Actualización de capas (Overlays, MyLocationOverlay, Círculo de Radio, Marcadores)
    androidx.compose.runtime.SideEffect {
        mapView.overlayManager.tilesOverlay?.setColorFilter(if (esModoOscuro) TilesOverlay.INVERT_COLORS else null)
        mapView.overlays.clear()

        // 1. MyLocationOverlay con ícono circular personalizado (punto azul con borde blanco)
        if (tienePermiso) {
            val customBitmap = crearBitmapPuntoAzul()
            val myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), mapView).apply {
                enableMyLocation()
                setPersonIcon(customBitmap)
                setDirectionIcon(customBitmap)
            }
            mapView.overlays.add(myLocationOverlay)
        }

        // 2. Círculo de radio de cercanía y marcador "Estás aquí"
        if (estado is UbicacionUi.Lista) {
            val centerPoint = GeoPoint(estado.posicion.latitud, estado.posicion.longitud)
            
            val circlePolygon = Polygon(mapView).apply {
                points = Polygon.pointsAsCircle(centerPoint, radioMetros)
                fillColor = 0x221565C0.toInt()
                strokeColor = 0xFF1565C0.toInt()
                strokeWidth = 2f
            }
            mapView.overlays.add(circlePolygon)

            val hereMarker = Marker(mapView).apply {
                position = centerPoint
                title = "Estás aquí"
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            }
            mapView.overlays.add(hereMarker)
        }

        // 3. Marcadores de los lugares con tintes diferenciados:
        // Verde (#2E7D32) para el más cercano (índice 0), Azul (#1565C0) para el seleccionado, Gris azulado (#78909C) para el resto
        lugares.forEachIndexed { index, item ->
            val esSeleccionado = item.lugar.id == seleccionado
            val esMasCercano = index == 0 && item.metros != null
            val placePoint = GeoPoint(item.lugar.latitud, item.lugar.longitud)
            val textoDistancia = item.metros?.let { " • ${formatearDistancia(it)}" } ?: ""

            val colorHex = when {
                esMasCercano -> "#2E7D32"
                esSeleccionado -> "#1565C0"
                else -> "#78909C"
            }

            val markerDrawable = ContextCompat.getDrawable(context, org.osmdroid.library.R.drawable.marker_default)?.mutate()
            markerDrawable?.setTint(AndroidColor.parseColor(colorHex))

            val marker = Marker(mapView).apply {
                position = placePoint
                title = item.lugar.nombre
                subDescription = "${item.lugar.categoria}$textoDistancia"
                icon = markerDrawable
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            }
            mapView.overlays.add(marker)
        }

        mapView.invalidate()
    }

    Box(modifier = modifier) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize()
        )

        // SmallFloatingActionButton azul (#1565C0) con Icons.Rounded.MyLocation en la esquina inferior derecha
        if (estado is UbicacionUi.Lista) {
            SmallFloatingActionButton(
                onClick = {
                    val pos = estado.posicion
                    mapView.controller.animateTo(GeoPoint(pos.latitud, pos.longitud))
                    mapView.controller.setZoom(16.0)
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = Color(0xFF1565C0),
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Rounded.MyLocation,
                    contentDescription = "Centrar en mi ubicación"
                )
            }
        }
    }
}
