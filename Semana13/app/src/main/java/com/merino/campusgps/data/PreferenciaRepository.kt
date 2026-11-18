package com.merino.campusgps.data

import kotlinx.coroutines.flow.Flow

// COMENTARIO: Interfaz del repositorio de preferencias para abstraer DataStore
interface PreferenciaRepository {
    val ultimoLugarId: Flow<Int?>
    suspend fun guardarUltimoLugarId(id: Int?)
}
