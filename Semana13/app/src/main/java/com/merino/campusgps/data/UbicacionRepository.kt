package com.merino.campusgps.data

import com.merino.campusgps.model.Posicion

interface UbicacionRepository {
    fun playServicesDisponible(): Boolean
    suspend fun obtenerPosicion(): Result<Posicion>
}