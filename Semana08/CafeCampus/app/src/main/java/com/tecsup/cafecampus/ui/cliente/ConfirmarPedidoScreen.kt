package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
    var horaSeleccionada by remember { mutableStateOf("Ahora") }
    var pagoSeleccionado by remember { mutableStateOf("CAJA") }

    val horas = listOf("Ahora", "10:30", "11:00", "11:30")
    val metodosPago = listOf("CAJA" to "Pagar en caja", "YAPE" to "Yape / Plin")

    Scaffold(
        topBar = { TopAppBar(title = { Text("Confirmar pedido") }) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(carrito) { item ->
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Text("${item.cantidad}x ${item.producto.nombre}", modifier = Modifier.weight(1f))
                        Text("S/ ${item.producto.precio * item.cantidad}")
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            Text("Hora de recojo", fontWeight = FontWeight.Bold)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                horas.forEach { hora ->
                    FilterChip(
                        selected = horaSeleccionada == hora,
                        onClick = { horaSeleccionada = hora },
                        label = { Text(hora) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFEA580C),
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Forma de pago", fontWeight = FontWeight.Bold)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                metodosPago.forEach { (codigo, texto) ->
                    OutlinedButton(
                        onClick = { pagoSeleccionado = codigo },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (pagoSeleccionado == codigo) Color(0xFFFFEDD5) else Color.Transparent
                        )
                    ) {
                        Text(texto, color = if (pagoSeleccionado == codigo) Color(0xFFEA580C) else Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text("Total", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.weight(1f))
                Text("S/ $total", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge, color = Color(0xFFEA580C))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    vmPedidos.confirmar(
                        clienteId = clienteId,
                        items = carrito,
                        horaRecojo = horaSeleccionada,
                        pago = pagoSeleccionado,
                        nota = "",
                        onExito = { nuevoPedidoId -> onConfirmado(nuevoPedidoId) }
                    )
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA580C))
            ) {
                Text("Confirmar pedido")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onSeguirPidiendo,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Seguir pidiendo", color = Color(0xFFEA580C))
            }
        }
    }
}