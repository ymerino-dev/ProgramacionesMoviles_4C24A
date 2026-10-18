package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.viewmodel.PedidosAdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallePedidoAdminScreen(
    pedidoId: Int,
    vmAdmin: PedidosAdminViewModel,
    onVolver: () -> Unit
) {
    // Buscamos el pedido en la lista actual del administrador
    val pedidos by vmAdmin.pedidos.collectAsState()
    val p = pedidos.find { it.pedido.id == pedidoId }

    val colorAdmin = Color(0xFF4F46E5)

    Scaffold(
        topBar = { TopAppBar(title = { Text("Detalle del pedido") }) }
    ) { padding ->
        if (p == null) {
            Text("Pedido no encontrado", modifier = Modifier.padding(padding))
            return@Scaffold
        }

        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text("Pedido #${p.pedido.id.toString().padStart(4, '0')}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Recojo: ${p.pedido.horaRecojo} • Estado: ${p.pedido.estado}", color = colorAdmin)

            Spacer(modifier = Modifier.height(16.dp))
            Text("Cliente", color = Color.Gray)
            Text(p.cliente.nombre, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(p.detalles) { detalle ->
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Text("${detalle.cantidad}x ${detalle.nombreProducto}", modifier = Modifier.weight(1f))
                        Text("S/ ${detalle.precioUnitario * detalle.cantidad}")
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Text("Total", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.weight(1f))
                Text("S/ ${p.pedido.total}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            }

            // Tarjeta Ámbar para la nota del cliente
            if (p.pedido.nota.isNotBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEDD5))) {
                    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                        Text("Nota del cliente", fontWeight = FontWeight.Bold, color = Color(0xFFD97706))
                        Text(p.pedido.nota)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (p.pedido.estado != EstadoPedido.ENTREGADO.name && p.pedido.estado != EstadoPedido.CANCELADO.name) {
                Text("Cambiar estado", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        vmAdmin.avanzar(p.pedido.id, p.pedido.estado)
                        onVolver()
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorAdmin)
                ) {
                    Text(
                        when(p.pedido.estado) {
                            EstadoPedido.RECIBIDO.name -> "Pasar a Preparando"
                            EstadoPedido.PREPARANDO.name -> "Pasar a Listo"
                            EstadoPedido.LISTO.name -> "Pasar a Entregado"
                            else -> "Avanzar a la siguiente etapa"
                        }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = {
                        vmAdmin.cancelar(p.pedido.id)
                        onVolver()
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                ) {
                    Text("Cancelar pedido")
                }
            }
        }
    }
}