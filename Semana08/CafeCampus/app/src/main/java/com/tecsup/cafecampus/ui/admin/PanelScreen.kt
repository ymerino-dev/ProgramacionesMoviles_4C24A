package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.cafecampus.data.local.dao.PedidosPorHora
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.viewmodel.PanelAdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanelScreen(
    vmPanel: PanelAdminViewModel,
    nuevosPedidos: List<PedidoConDetalle>,
    onPreparar: (Int, String) -> Unit,
    onVerTodos: () -> Unit,
    onVerDetallePedido: (Int) -> Unit
) {
    val nuevos by vmPanel.nuevos.collectAsState()
    val preparando by vmPanel.preparando.collectAsState()
    val listos by vmPanel.listos.collectAsState()
    val ventasHoy by vmPanel.ventasHoy.collectAsState()

    val productoMasVendido by vmPanel.productoMasVendido.collectAsState()
    val ventasPorMetodoPago by vmPanel.ventasPorMetodoPago.collectAsState()
    val pedidosPorHora by vmPanel.pedidosPorHora.collectAsState()

    val colorAdmin = Color(0xFF4F46E5) // Índigo del administrador

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Panel de control", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("Cafetería · Hoy", color = Color.Gray, fontSize = 12.sp)
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notificaciones", tint = Color.Black)
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                // 1. Contadores superiores con texto ajustado para evitar saltos
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ContadorCardAdmin("Nuevos", nuevos, Color(0xFF3B82F6), Modifier.weight(1f))
                    ContadorCardAdmin("Preparando", preparando, Color(0xFFD97706), Modifier.weight(1f))
                    ContadorCardAdmin("Listos", listos, Color(0xFF16A34A), Modifier.weight(1f))
                }
            }

            item {
                // 2. Tarjeta de ventas del día con contabilización de pedidos
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = colorAdmin),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Ventas del día", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("S/ $ventasHoy", color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        }
                        // Conteo de pedidos pagados
                        Text(
                            text = "${nuevos + preparando + listos} pedidos",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            item {
                // 3. Reporte del día: Producto más vendido y Ventas por Método de Pago
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBF7))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Reportes del día", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            // Producto más vendido
                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("🔥 Más vendido", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD97706))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = productoMasVendido?.nombreProducto ?: "Sin ventas",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "${productoMasVendido?.cantidadTotal ?: 0} ud. vendidas",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }
                            }

                            // Ventas por pago
                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E7FF)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("💳 Métodos de pago", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4F46E5))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    if (ventasPorMetodoPago.isEmpty()) {
                                        Text("Sin datos", fontSize = 12.sp, color = Color.Gray)
                                    } else {
                                        ventasPorMetodoPago.forEach { p ->
                                            Text(
                                                text = "${p.pago}: S/ ${p.total}",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color.DarkGray
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                // 4. Gráfico de barras real: Pedidos por hora
                val maxCantidad = (pedidosPorHora.maxOfOrNull { it.cantidad } ?: 1).coerceAtLeast(1)
                val itemsHora = if (pedidosPorHora.isNotEmpty()) {
                    pedidosPorHora
                } else {
                    listOf(
                        PedidosPorHora("8h", 0),
                        PedidosPorHora("9h", 0),
                        PedidosPorHora("10h", 0),
                        PedidosPorHora("11h", 0),
                        PedidosPorHora("12h", 0)
                    )
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBF7))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Pedidos por hora", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            itemsHora.forEach { item ->
                                val alturaCalculada = if (item.cantidad == 0) 20 else ((item.cantidad.toFloat() / maxCantidad.toFloat()) * 90 + 20).toInt()
                                val esDestacado = item.cantidad == maxCantidad && item.cantidad > 0
                                BarraHora(
                                    hora = item.hora,
                                    cantidad = item.cantidad,
                                    alturaDp = alturaCalculada,
                                    destacado = esDestacado
                                )
                            }
                        }
                    }
                }
            }

            item {
                // 4. Resumen de nuevos pedidos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Nuevos pedidos", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = onVerTodos) {
                        Text("Ver todos", color = colorAdmin, fontWeight = FontWeight.Bold)
                    }
                }
            }

            items(nuevosPedidos.take(3)) { p ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBF7))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("#${p.pedido.id.toString().padStart(4, '0')} ${p.cliente.nombre}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            val resumen = p.detalles.joinToString { "${it.cantidad} x ${it.nombreProducto}" }
                            Text("$resumen · ${p.pedido.horaRecojo}", color = Color.Gray, fontSize = 12.sp)
                        }
                        Button(
                            onClick = { onPreparar(p.pedido.id, p.pedido.estado) },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = colorAdmin),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text("Preparar", fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContadorCardAdmin(titulo: String, cantidad: Int, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(titulo, color = color, fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1)
            Spacer(modifier = Modifier.height(2.dp))
            Text(cantidad.toString(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
fun BarraHora(hora: String, cantidad: Int, alturaDp: Int, destacado: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(cantidad.toString(), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (destacado) Color(0xFF4F46E5) else Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .width(28.dp)
                .height(alturaDp.dp)
                .background(
                    color = if (destacado) Color(0xFF4F46E5) else Color(0xFFC7D2FE),
                    shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                )
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(hora, fontSize = 11.sp, color = Color.Gray)
    }
}