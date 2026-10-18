package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeguimientoScreen(
    pedidoId: Int,
    vmPedidos: PedidosClienteViewModel,
    onVolver: () -> Unit
) {
    // Lee el pedido en tiempo real desde la BD
    val pedidoConDetalle by vmPedidos.pedido(pedidoId).collectAsState(initial = null)
    val colorCliente = Color(0xFFEA580C)

    Scaffold(
        topBar = { TopAppBar(title = { Text("Seguimiento") }) }
    ) { padding ->
        if (pedidoConDetalle == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = colorCliente)
            }
            return@Scaffold
        }

        val p = pedidoConDetalle!!
        val estadoActual = p.pedido.estado

        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            // Tarjeta principal naranja
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = colorCliente),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Pedido #${p.pedido.id.toString().padStart(4, '0')}", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Recojo a las ${p.pedido.horaRecojo}", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Línea de tiempo simplificada (Stepper)
            Text("Estado del pedido:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            EstadoPaso("Recibido", estadoActual == EstadoPedido.RECIBIDO.name || estadoActual == EstadoPedido.PREPARANDO.name || estadoActual == EstadoPedido.LISTO.name || estadoActual == EstadoPedido.ENTREGADO.name)
            EstadoPaso("Preparando", estadoActual == EstadoPedido.PREPARANDO.name || estadoActual == EstadoPedido.LISTO.name || estadoActual == EstadoPedido.ENTREGADO.name)
            EstadoPaso("Listo", estadoActual == EstadoPedido.LISTO.name || estadoActual == EstadoPedido.ENTREGADO.name)
            EstadoPaso("Entregado", estadoActual == EstadoPedido.ENTREGADO.name)

            if (estadoActual == EstadoPedido.CANCELADO.name) {
                Text("TU PEDIDO FUE CANCELADO", color = Color.Red, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))

            // Resumen de la compra
            Text("Resumen", fontWeight = FontWeight.Bold)
            LazyColumn {
                items(p.detalles) { detalle ->
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Text("${detalle.cantidad}x ${detalle.nombreProducto}", modifier = Modifier.weight(1f))
                        Text("S/ ${detalle.precioUnitario * detalle.cantidad}")
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Total pagado: S/ ${p.pedido.total}", fontWeight = FontWeight.Bold, color = colorCliente)

            Spacer(modifier = Modifier.weight(1f))
            OutlinedButton(onClick = onVolver, modifier = Modifier.fillMaxWidth()) {
                Text("Volver al menú", color = colorCliente)
            }
        }
    }
}

@Composable
fun EstadoPaso(texto: String, completado: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(if (completado) Color(0xFF16A34A) else Color.LightGray, shape = RoundedCornerShape(12.dp))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(texto, fontWeight = if (completado) FontWeight.Bold else FontWeight.Normal, color = if (completado) Color.Black else Color.Gray)
    }
}