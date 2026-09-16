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
}