package com.merinoana.calendariocuotas

fun main() {
    println("=== SISTEMA DE PAGOS ===")
    print("Ingrese el nombre del producto: ")
    val producto = readln()

    print("Ingrese el precio: ")
    val precio = readln().toDouble()

    print("Ingrese la cantidad: ")
    val cantidad = readln().toInt()

    // 1. Validacion estricta
    var cuotas = 0
    while (cuotas != 6 && cuotas != 12 && cuotas != 24) {
        print("N-Cuotas (6, 12 o 24): ")
        cuotas = readln().toInt()

        if (cuotas != 6 && cuotas != 12 && cuotas != 24) {
            println("Validacion: Solo se permiten 6, 12 o 24 cuotas. Intente de nuevo.")
        }
    }

    // 2. Calculos matematicos
    val montoInicial = precio * cantidad

    val porcentajeInteres = when (cuotas) {
        6 -> 0.20
        12 -> 0.40
        24 -> 0.60
        else -> 0.0
    }

    val interes = montoInicial * porcentajeInteres
    val montoAPagar = montoInicial + interes
    val pagoMensual = montoAPagar / cuotas

    // 3. Imprimir el encabezado superior tal como en la pizarra
    println("\nPxC -> Monto Inicial: $montoInicial")
    println("Monto a Pagar: $montoAPagar \t Interes: $interes")
    println("Pago Mensual: $pagoMensual\n")

}