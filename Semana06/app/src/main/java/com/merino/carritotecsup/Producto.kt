package com.merino.carritotecsup

data class Producto(
    val id: Int = 0,
    val nombre: String = "",
    val precio: Double = 0.0,
    val categoria: String = "General",
    val descripcion: String = "Sin descripción disponible.",
    val imagenRes: Int = R.drawable.inca_kola,
    var cantidad: Int = 1,
    var esFavorito: Boolean = false
)
