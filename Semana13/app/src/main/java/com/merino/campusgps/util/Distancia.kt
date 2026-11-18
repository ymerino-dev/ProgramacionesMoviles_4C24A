package com.merino.campusgps.util

import android.location.Location
import com.merino.campusgps.model.Posicion

// COMENTARIO [Cálculo de Distancia]: Utiliza Location.distanceBetween para calcular la distancia en metros
// entre la ubicación actual del usuario (origen) y las coordenadas de cada lugar en el campus.
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
