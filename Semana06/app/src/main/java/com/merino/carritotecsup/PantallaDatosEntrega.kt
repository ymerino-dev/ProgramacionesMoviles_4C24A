package com.merino.carritotecsup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDatosEntrega(
    subtotal: Double = 0.0,
    onNavigateBack: () -> Unit,
    onConfirmarPedido: (tipoEntrega: String, metodoPago: String, direccion: String, total: Double) -> Unit
) {
    var direccion by remember { mutableStateOf("Av. Principal 123, Lima - Perú") }
    var referencia by remember { mutableStateOf("Frente al parque principal") }
    var telefono by remember { mutableStateOf("987654321") }

    var isDireccionError by remember { mutableStateOf(false) }
    var isTelefonoError by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }

    // Tipo de Entrega: Recojo en Tienda vs Delivery
    var tipoEntrega by remember { mutableStateOf("Delivery") }
    val opcionesEntrega = listOf("Delivery", "Recojo en Tienda")

    // Métodos de Pago
    var metodoPago by remember { mutableStateOf("Yape") }
    val opcionesPago = listOf("Efectivo", "Yape", "Plin")

    // Cálculo dinámico de envío y total
    val costoEnvio = if (tipoEntrega == "Recojo en Tienda") 0.0 else 5.00
    val totalGeneral = subtotal + costoEnvio

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Datos de Entrega y Pago") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // SECCIÓN TIPO DE ENTREGA (Recojo vs Delivery)
                Text(
                    text = "Tipo de Entrega",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        opcionesEntrega.forEach { opcion ->
                            val esRecojo = (opcion == "Recojo en Tienda")
                            val desc = if (esRecojo) "Gratis (S/ 0.00)" else "Envío a domicilio (S/ 5.00)"
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = (tipoEntrega == opcion),
                                        onClick = { tipoEntrega = opcion }
                                    )
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (tipoEntrega == opcion),
                                    onClick = { tipoEntrega = opcion }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(text = opcion, fontWeight = FontWeight.Bold)
                                    Text(text = desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // SECCIÓN DIRECCIÓN DE ENVÍO
                if (tipoEntrega == "Delivery") {
                    Text(
                        text = "Dirección de Envío",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = direccion,
                        onValueChange = {
                            direccion = it
                            isDireccionError = false
                            mensajeError = ""
                        },
                        label = { Text("Dirección *") },
                        isError = isDireccionError,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = referencia,
                        onValueChange = { referencia = it },
                        label = { Text("Referencia (Opcional)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                } else {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Lugar de Recojo:",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = "Mi Bodega TECSUP - Av. Cascanueces 2221, Santa Anita",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                OutlinedTextField(
                    value = telefono,
                    onValueChange = {
                        telefono = it
                        isTelefonoError = false
                        mensajeError = ""
                    },
                    label = { Text("Teléfono de Contacto *") },
                    isError = isTelefonoError,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                if (mensajeError.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = mensajeError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // SECCIÓN MÉTODOS DE PAGO
                Text(
                    text = "Método de Pago",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    opcionesPago.forEach { metodo ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (metodoPago == metodo),
                                    onClick = { metodoPago = metodo }
                                )
                                .padding(vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (metodoPago == metodo),
                                onClick = { metodoPago = metodo }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = metodo, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // RESUMEN DE PAGO
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Subtotal productos:")
                            Text("S/ ${String.format("%.2f", subtotal)}")
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Costo de entrega:")
                            Text(
                                if (costoEnvio == 0.0) "GRATIS" else "S/ ${String.format("%.2f", costoEnvio)}",
                                fontWeight = FontWeight.Bold
                            )
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("TOTAL A PAGAR:", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Text("S/ ${String.format("%.2f", totalGeneral)}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val dBlank = (tipoEntrega == "Delivery" && direccion.trim().isBlank())
                    val tBlank = telefono.trim().isBlank()

                    isDireccionError = dBlank
                    isTelefonoError = tBlank

                    if (dBlank || tBlank) {
                        mensajeError = "Por favor completa los campos marcados (*)"
                    } else {
                        val dirFinal = if (tipoEntrega == "Recojo en Tienda") "Recojo en Tienda TECSUP" else direccion
                        onConfirmarPedido(tipoEntrega, metodoPago, dirFinal, totalGeneral)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("CONFIRMAR PEDIDO Y PAGAR")
            }
        }
    }
}
