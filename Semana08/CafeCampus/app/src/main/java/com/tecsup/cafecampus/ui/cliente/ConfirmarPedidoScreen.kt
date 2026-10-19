package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
            Column(modifier = Modifier.weight(1f)) {

                // 1. Tarjeta resumen de los productos en el carrito
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBF7)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        LazyColumn(modifier = Modifier.heightIn(max = 180.dp)) {
                            items(carrito) { item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
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
                                            Text(item.producto.nombre, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                            Text("x${item.cantidad}", color = Color.Gray, fontSize = 12.sp)
                                        }
                                    }
                                    Text("S/ ${item.producto.precio * item.cantidad}", fontWeight = FontWeight.Bold, color = Color.DarkGray)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 2. Hora de recojo
                Text("Hora de recojo", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    horas.forEach { hora ->
                        val seleccionado = horaSeleccionada == hora
                        Button(
                            onClick = { horaSeleccionada = hora },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (seleccionado) colorNaranja else Color(0xFFF1F5F9)
                            ),
                            modifier = Modifier.weight(1f).height(40.dp)
                        ) {
                            Text(
                                text = hora,
                                color = if (seleccionado) Color.White else Color.DarkGray,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 3. Forma de pago
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
                        )
                    ) {
                        Text("Pagar en caja", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { pagoSeleccionado = "YAPE" },
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (!esCaja) Color(0xFFFFEDD5) else Color.Transparent,
                            contentColor = if (!esCaja) colorNaranja else Color.Gray
                        )
                    ) {
                        Text("Yape / Plin", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

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

            // 5. Botones inferiores
            Column {
                Button(
                    onClick = {
                        // Extraemos y juntamos todas las notas
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
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = colorNaranja)
                ) {
                    Text("Seguir pidiendo", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}