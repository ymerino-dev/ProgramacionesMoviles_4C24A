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
fun PedidosAdminScreen(
    vmAdmin: PedidosAdminViewModel
) {
    val estadoActual by vmAdmin.estadoSeleccionado.collectAsState()
    val pedidos by vmAdmin.pedidos.collectAsState()
    val colorAdmin = Color(0xFF4F46E5)

    val estados = listOf(EstadoPedido.RECIBIDO, EstadoPedido.PREPARANDO, EstadoPedido.LISTO, EstadoPedido.ENTREGADO)

    Scaffold(
        topBar = { TopAppBar(title = { Text("Gestión de Pedidos") }) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            // FilterChips de estados
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                estados.forEach { est ->
                    FilterChip(
                        selected = estadoActual == est,
                        onClick = { vmAdmin.seleccionarEstado(est) },
                        label = { Text(est.name.lowercase().replaceFirstChar { it.uppercase() }) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = colorAdmin,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de pedidos del estado seleccionado
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(pedidos) { p ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7FD))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row {
                                Text("Pedido #${p.pedido.id.toString().padStart(4, '0')}", fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.weight(1f))
                                Text("S/ ${p.pedido.total}", color = colorAdmin, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Recojo: ${p.pedido.horaRecojo} • Pago: ${p.pedido.pago}", color = Color.Gray)

                            if (p.pedido.nota.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Nota del cliente: ${p.pedido.nota}", color = Color(0xFFD97706))
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                if (p.pedido.estado != EstadoPedido.ENTREGADO.name && p.pedido.estado != EstadoPedido.CANCELADO.name) {
                                    Button(
                                        onClick = { vmAdmin.avanzar(p.pedido.id, p.pedido.estado) },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(containerColor = colorAdmin)
                                    ) {
                                        Text(
                                            when(p.pedido.estado) {
                                                EstadoPedido.RECIBIDO.name -> "Pasar a Preparando"
                                                EstadoPedido.PREPARANDO.name -> "Pasar a Listo"
                                                EstadoPedido.LISTO.name -> "Pasar a Entregado"
                                                else -> "Avanzar"
                                            }
                                        )
                                    }
                                    OutlinedButton(
                                        onClick = { vmAdmin.cancelar(p.pedido.id) },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                                    ) {
                                        Text("Cancelar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}