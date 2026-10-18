package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeguimientoScreen(
    pedidoId: Int,
    vmPedidos: PedidosClienteViewModel,
    onVolver: () -> Unit
) {
    val pedidoConDetalle by vmPedidos.pedido(pedidoId).collectAsState(initial = null)
    val colorNaranja = Color(0xFFEA580C)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seguimiento", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        if (pedidoConDetalle == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = colorNaranja)
            }
            return@Scaffold
        }

        val p = pedidoConDetalle!!
        val estadoActual = p.pedido.estado

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {
            // Tarjeta superior naranja exacta a la imagen del manual
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = colorNaranja),
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
                        Text(
                            text = "Pedido #${p.pedido.id.toString().padStart(4, '0')}",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        // Recojo a las 10:30 (en plomo claro / blanco tenue)
                        Text("Recojo a las ${p.pedido.horaRecojo}", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Tiempo estimado: 10 min", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }

                    // Icono de reloj blanco dentro de un círculo transparente
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(Color.White.copy(alpha = 0.2f), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.Schedule, contentDescription = "Reloj", tint = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Línea de tiempo vertical (Stepper con checks y grises)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBF7))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    EstadoPasoFiel("Recibido", "Tu pedido llegó a la cocina", estadoActual == EstadoPedido.RECIBIDO.name || estadoActual == EstadoPedido.PREPARANDO.name || estadoActual == EstadoPedido.LISTO.name || estadoActual == EstadoPedido.ENTREGADO.name, true)
                    EstadoPasoFiel("Preparando", "Están cocinando tu pedido", estadoActual == EstadoPedido.PREPARANDO.name || estadoActual == EstadoPedido.LISTO.name || estadoActual == EstadoPedido.ENTREGADO.name, true)
                    EstadoPasoFiel("Listo", "Puedes pasar a recogerlo", estadoActual == EstadoPedido.LISTO.name || estadoActual == EstadoPedido.ENTREGADO.name, true)
                    EstadoPasoFiel("Entregado", "¡Buen provecho!", estadoActual == EstadoPedido.ENTREGADO.name, false)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Resumen inferior del pedido
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = p.detalles.joinToString { "${it.cantidad} x ${it.nombreProducto}" },
                        color = Color.DarkGray,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 13.sp)
                        Text("S/ ${p.pedido.total}", fontWeight = FontWeight.Bold, color = colorNaranja, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun EstadoPasoFiel(titulo: String, subtitulo: String, completado: Boolean, mostrarLinea: Boolean) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(30.dp)) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .background(if (completado) Color(0xFF16A34A) else Color(0xFFE2E8F0), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (completado) {
                    Text("✓", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            if (mostrarLinea) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(35.dp)
                        .background(if (completado) Color(0xFF16A34A) else Color(0xFFE2E8F0))
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.padding(bottom = 16.dp)) {
            Text(titulo, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = if (completado) Color.Black else Color.Gray)
            Text(subtitulo, fontSize = 12.sp, color = Color.Gray)
        }
    }
}