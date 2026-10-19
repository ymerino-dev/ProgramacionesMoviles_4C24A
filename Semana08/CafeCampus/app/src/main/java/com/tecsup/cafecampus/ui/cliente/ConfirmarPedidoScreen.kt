package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.cafecampus.model.ItemCarrito
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmarPedidoScreen(
    clienteId: Int,
    carrito: List<ItemCarrito>,
    total: Double,
    vmPedidos: PedidosClienteViewModel,
    onSeguirPidiendo: () -> Unit,
    onConfirmado: (Long) -> Unit
) {
    var horaSeleccionada by remember { mutableStateOf("10:30") }
    var pagoSeleccionado by remember { mutableStateOf("CAJA") }

    val horas = listOf("Ahora", "10:30", "11:00", "11:30")
    val colorNaranja = Color(0xFFEA580C)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirmar pedido", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onSeguirPidiendo) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // CONTENIDO CON SCROLL VERTICAL (Evita que los botones desaparezcan)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                // 1. Tarjeta resumen de los productos en el carrito
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBF7)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Usamos un forEach en lugar de LazyColumn para permitir el scroll padre
                        carrito.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFFFFEDD5)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("🍲", fontSize = 16.sp)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(item.producto.nombre, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1)
                                        Text("x${item.cantidad}", color = Color.Gray, fontSize = 12.sp)
                                    }
                                }
                                Text("S/ ${item.producto.precio * item.cantidad}", fontWeight = FontWeight.Bold, color = Color.DarkGray)
                            }
                        }
                    }
                }

                // 2. Hora de recojo (AHORA CON SCROLL HORIZONTAL)
                Column {
                    Text("Hora de recojo", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()), // <-- Evita que los textos se apachurren
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        horas.forEach { hora ->
                            val seleccionado = horaSeleccionada == hora
                            Button(
                                onClick = { horaSeleccionada = hora },
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (seleccionado) colorNaranja else Color(0xFFF1F5F9)
                                ),
                                modifier = Modifier.height(40.dp)
                            ) {
                                Text(
                                    text = hora,
                                    color = if (seleccionado) Color.White else Color.DarkGray,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }

                // 3. Forma de pago
                Column {
                    Text("Forma de pago", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        val esCaja = pagoSeleccionado == "CAJA"
                        OutlinedButton(
                            onClick = { pagoSeleccionado = "CAJA" },
                            modifier = Modifier.weight(1f).height(44.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (esCaja) Color(0xFFFFEDD5) else Color.Transparent,
                                contentColor = if (esCaja) colorNaranja else Color.Gray
                            ),
                            border = BorderStroke(1.dp, if (esCaja) colorNaranja else Color.LightGray)
                        ) {
                            Text("Pagar en caja", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = { pagoSeleccionado = "YAPE" },
                            modifier = Modifier.weight(1f).height(44.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (!esCaja) Color(0xFFFFEDD5) else Color.Transparent,
                                contentColor = if (!esCaja) colorNaranja else Color.Gray
                            ),
                            border = BorderStroke(1.dp, if (!esCaja) colorNaranja else Color.LightGray)
                        ) {
                            Text("Yape / Plin", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }

                // 4. Desglose de totales
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Subtotal", color = Color.Gray, fontSize = 13.sp)
                            Text("S/ $total", color = Color.DarkGray, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Servicio", color = Color.Gray, fontSize = 13.sp)
                            Text("S/ 0.00", color = Color.DarkGray, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = Color(0xFFE2E8F0))
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                            Text("S/ $total", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = colorNaranja)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Botones inferiores (Siempre visibles al final)
            Column {
                Button(
                    onClick = {
                        val notasDelCliente = carrito
                            .filter { it.nota.isNotBlank() }
                            .joinToString(" | ") { "${it.producto.nombre}: ${it.nota}" }

                        vmPedidos.confirmar(
                            clienteId = clienteId,
                            items = carrito,
                            horaRecojo = horaSeleccionada,
                            pago = pagoSeleccionado,
                            nota = notasDelCliente,
                            onExito = { nuevoPedidoId -> onConfirmado(nuevoPedidoId) }
                        )
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorNaranja)
                ) {
                    Text("Confirmar pedido", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = onSeguirPidiendo,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = colorNaranja),
                    border = BorderStroke(1.dp, colorNaranja)
                ) {
                    Text("Seguir pidiendo", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}