package com.merino.carritotecsup

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Pedido(
    val id: String = "#" + (1000..9999).random(),
    val fecha: String = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date()),
    val productos: List<Producto> = emptyList(),
    val tipoEntrega: String = "Delivery",
    val metodoPago: String = "Yape",
    val total: Double = 0.0,
    val estado: String = "En preparación"
)
