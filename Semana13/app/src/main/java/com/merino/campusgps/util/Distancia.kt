package com.merino.campusgps.util

import android.location.Location
import com.merino.campusgps.model.Posicion

fun calcularDistanciaMetros(origen: Posicion, latDestino: Double, lonDestino: Double): Float {
    val resultados = FloatArray(1)
    Location.distanceBetween(
        origen.latitud,
        origen.longitud,
        latDestino,
        lonDestino,
        resultados
    )
    return resultados[0]
}