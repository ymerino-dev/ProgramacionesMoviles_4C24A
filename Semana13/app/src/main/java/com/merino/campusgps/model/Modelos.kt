package com.merino.campusgps.model

data class Posicion(val latitud: Double, val longitud: Double)

data class Lugar(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val latitud: Double,
    val longitud: Double
)

// metros = null mientras todavía no se conoce la ubicación del usuario
data class LugarConDistancia(
    val lugar: Lugar,
    val metros: Float?,
    val dentroDelRadio: Boolean = true
)

enum class TipoError {
    SIN_PERMISO,
    GPS_APAGADO,
    SIN_PLAY_SERVICES,
    OTRO
}

sealed interface UbicacionUi {
    data object Inicial : UbicacionUi
    data object Buscando : UbicacionUi
    data class Lista(
        val posicion: Posicion,
        val categoriaSeleccionada: String = "Todas",
        val lugaresFiltrados: List<LugarConDistancia> = emptyList(),
        val radioMetros: Double = 200.0
    ) : UbicacionUi
    data class Error(val mensaje: String, val tipo: TipoError = TipoError.OTRO) : UbicacionUi
}
