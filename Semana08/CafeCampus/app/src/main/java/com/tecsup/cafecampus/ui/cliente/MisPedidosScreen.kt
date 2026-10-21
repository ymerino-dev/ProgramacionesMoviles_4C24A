package com.tecsup.cafecampus.ui.cliente

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MisPedidosScreen(
    vmPedidos: PedidosClienteViewModel,
    onVerDetalle: (Int) -> Unit, // Navega al Seguimiento al tocar una tarjeta
    onRepetirPedido: (PedidoConDetalle) -> Unit = {},
    onIrAlCarrito: () -> Unit = {}
) {
    // Lee todos los pedidos del cliente logueado
    val todosLosPedidos by vmPedidos.misPedidos.collectAsState()
    var pestanaActiva by remember { mutableIntStateOf(0) } // 0 = Activos, 1 = Historial

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = Color(0xFF1E293B),
                    contentColor = Color.White,
                    actionColor = Color(0xFFFB923C),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Pestañas (Tabs)
            TabRow(
                selectedTabIndex = pestanaActiva,
                containerColor = Color.White,
                contentColor = Color(0xFFEA580C)
            ) {
                Tab(selected = pestanaActiva == 0, onClick = { pestanaActiva = 0 }, text = { Text("Activos") })
                Tab(selected = pestanaActiva == 1, onClick = { pestanaActiva = 1 }, text = { Text("Historial") })
            }

            AnimatedContent(
                targetState = pestanaActiva,
                label = "TabAnimation",
                modifier = Modifier.fillMaxSize()
            ) { tabIndex ->
                val listaLocal = if (tabIndex == 0) {
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

                if (listaLocal.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ReceiptLong,
                                contentDescription = "Sin pedidos",
                                tint = Color.LightGray,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No hay pedidos registrados",
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (tabIndex == 0) "Tus pedidos en proceso aparecerán aquí" else "Tu historial de pedidos aparecerá aquí",
                                color = Color.LightGray,
                                fontSize = 13.sp
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        items(listaLocal, key = { it.pedido.id }) { p ->
                            Card(
                                onClick = { onVerDetalle(p.pedido.id) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBF7))
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("Pedido #${p.pedido.id.toString().padStart(4, '0')}", fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text("S/ ${p.pedido.total}", color = Color(0xFFEA580C), fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(p.detalles.joinToString { "${it.cantidad}x ${it.nombreProducto}" }, color = Color.Gray)
                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Chip de estado colorido
                                        val colorEstado = when(p.pedido.estado) {
                                            EstadoPedido.RECIBIDO.name -> Color.Blue
                                            EstadoPedido.PREPARANDO.name -> Color(0xFFD97706) // Ámbar
                                            EstadoPedido.LISTO.name -> Color(0xFF16A34A) // Verde
                                            EstadoPedido.CANCELADO.name -> Color.Red
                                            else -> Color.Gray
                                        }
                                        Text(p.pedido.estado, color = colorEstado, fontWeight = FontWeight.Bold)

                                        // Botón Repetir pedido dentro de tarjetas finalizadas / del historial
                                        if (tabIndex == 1 || p.pedido.estado == EstadoPedido.ENTREGADO.name || p.pedido.estado == EstadoPedido.CANCELADO.name) {
                                            Button(
                                                onClick = {
                                                    onRepetirPedido(p)
                                                    scope.launch {
                                                        val result = snackbarHostState.showSnackbar(
                                                            message = "¡Pedido #${p.pedido.id.toString().padStart(4, '0')} cargado al carrito!",
                                                            actionLabel = "Ver Carrito",
                                                            duration = SnackbarDuration.Short
                                                        )
                                                        if (result == SnackbarResult.ActionPerformed) {
                                                            onIrAlCarrito()
                                                        }
                                                    }
                                                },
                                                shape = RoundedCornerShape(10.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA580C)),
                                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                                modifier = Modifier.height(34.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Replay,
                                                    contentDescription = "Repetir",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Repetir pedido", fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
    }
}