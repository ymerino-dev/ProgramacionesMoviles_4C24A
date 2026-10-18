package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel


@Composable
fun MisPedidosScreen(
    vmPedidos: PedidosClienteViewModel,
    onVerDetalle: (Int) -> Unit // Navega al Seguimiento al tocar una tarjeta
) {
    // Lee todos los pedidos del cliente logueado
    val todosLosPedidos by vmPedidos.misPedidos.collectAsState()
    var pestanaActiva by remember { mutableIntStateOf(0) } // 0 = Activos, 1 = Historial

    // Filtramos la lista según la pestaña
    val pedidosMostrados = if (pestanaActiva == 0) {
        todosLosPedidos.filter {
            it.pedido.estado == EstadoPedido.RECIBIDO.name ||
                    it.pedido.estado == EstadoPedido.PREPARANDO.name ||
                    it.pedido.estado == EstadoPedido.LISTO.name
        }
    } else {
        todosLosPedidos.filter {
            it.pedido.estado == EstadoPedido.ENTREGADO.name ||
                    it.pedido.estado == EstadoPedido.CANCELADO.name
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Pestañas (Tabs)
        TabRow(
            selectedTabIndex = pestanaActiva,
            containerColor = Color.White,
            contentColor = Color(0xFFEA580C)
        ) {
            Tab(selected = pestanaActiva == 0, onClick = { pestanaActiva = 0 }, text = { Text("Activos") })
            Tab(selected = pestanaActiva == 1, onClick = { pestanaActiva = 1 }, text = { Text("Historial") })
        }

        // Lista de tarjetas
        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            items(pedidosMostrados) { p ->
                Card(
                    onClick = { onVerDetalle(p.pedido.id) },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBF7))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row {
                            Text("Pedido #${p.pedido.id.toString().padStart(4, '0')}", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.weight(1f))
                            Text("S/ ${p.pedido.total}", color = Color(0xFFEA580C), fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(p.detalles.joinToString { "${it.cantidad}x ${it.nombreProducto}" }, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))

                        // Chip de estado colorido
                        val colorEstado = when(p.pedido.estado) {
                            EstadoPedido.RECIBIDO.name -> Color.Blue
                            EstadoPedido.PREPARANDO.name -> Color(0xFFD97706) // Ámbar
                            EstadoPedido.LISTO.name -> Color(0xFF16A34A) // Verde
                            EstadoPedido.CANCELADO.name -> Color.Red
                            else -> Color.Gray
                        }
                        Text(p.pedido.estado, color = colorEstado, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}