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
}
