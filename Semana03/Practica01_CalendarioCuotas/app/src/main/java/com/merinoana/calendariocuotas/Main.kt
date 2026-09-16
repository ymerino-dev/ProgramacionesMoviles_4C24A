package com.merinoana.calendariocuotas
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

    // 4. Imprimir la cabecera de la tabla
    println(String.format("%-4s %-12s %-10s %-10s %-10s", "N-", "FECHA", "MONTO", "P.Mensual", "Resta Pago"))

    // 5. Lógica del calendario y restas
    val fechaActual = LocalDate.now()
    val formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    var montoFila = montoAPagar // El monto empieza siendo el total a pagar

    for (i in 1..cuotas) {
        // Generamos la fecha del próximo mes
        val fechaPago = fechaActual.plusMonths(i.toLong())

        // Calculamos cuánto queda por pagar
        val restaPago = montoFila - pagoMensual

        // Imprimimos la fila alineada
        println(String.format("%-4d %-12s %-10.2f %-10.2f %-10.2f",
            i,
            fechaPago.format(formatoFecha),
            montoFila,
            pagoMensual,
            restaPago
        ))

        // Actualizamos el monto para el siguiente ciclo
        montoFila = restaPago
    }

}