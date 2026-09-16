package com.merinoana.lab02carritokotlin
data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)
fun main(){
    println("==================================")
    println("   CARRITO DE COMPRAS - TECSUP    ")
    println("==================================")

    val nombreCliente = "Ana Merino"
    val carrito = mutableListOf<Producto>()

    println("Cliente: $nombreCliente\n")

    carrito.add(Producto("Laptop HP",2500.0,1))
    carrito.add(Producto("Mouse Logitech", 45.5, 2))
    carrito.add(Producto("Audifonos Sony", 120.0, 1))
    carrito.add(Producto("USB Kingston 64GB", 75.0, 3))

    for (producto in carrito) {
        println("Producto agregado: ${producto.nombre}")
    }

    println()

    val subtotal = calcularSubtotal(carrito)
    val igv = calcularIGV(subtotal)
    val total = calcularTotal(subtotal, igv)

    println("Subtotal : S/ $subtotal")
    println("IGV (18%): S/ $igv")
    println("TOTAL    : S/ $total")

    println()
    val masCaro = carrito.maxByOrNull { it.precio }
    if (masCaro != null) {
        println("Producto mas caro: ${masCaro.nombre} " + String.format("(S/ %.2f)", masCaro.precio))
    }

    val descuento = calcularDescuento(total)

// Mostramos el mensaje correcto según el monto
    if (total > 5000) {
        println("Descuento aplicado: 10% por compra mayor a S/ 5000")
    } else if (total > 3000) {
        println("Descuento aplicado: 5% por compra mayor a S/ 3000")
    }

    val totalConDescuento = total - descuento
    println(String.format("%-23s: S/ %8.2f", "TOTAL CON DESCUENTO", totalConDescuento))

    println("\nGracias por su compra, $nombreCliente!")

}

fun calcularSubtotal(productos: List<Producto>): Double {
    var subtotal = 0.0
    for (p in productos) {
        subtotal += p.precio * p.cantidad
    }
    return subtotal
}

fun calcularIGV(subtotal: Double): Double {
    return subtotal * 0.18
}

fun calcularTotal(subtotal: Double, igv: Double): Double {
    return subtotal + igv
}

fun calcularDescuento(total: Double): Double {
    return when {
        total > 5000 -> total * 0.10
        total > 3000 -> total * 0.05
        else -> 0.0
    }
}
