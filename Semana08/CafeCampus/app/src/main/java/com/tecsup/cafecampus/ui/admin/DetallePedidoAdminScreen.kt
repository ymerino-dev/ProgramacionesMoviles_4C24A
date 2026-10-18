package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.viewmodel.PedidosAdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallePedidoAdminScreen(
    pedidoId: Int,
    vmAdmin: PedidosAdminViewModel,
    onVolver: () -> Unit
) {
    val pedidos by vmAdmin.pedidos.collectAsState()
    val p = pedidos.find { it.pedido.id == pedidoId }

    val colorAdmin = Color(0xFF4F46E5) // Azul / Índigo principal

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del pedido", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { padding ->
        if (p == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Pedido no encontrado", color = Color.Gray)
            }
            return@Scaffold
        }

        val estadoActual = p.pedido.estado

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()), // Permite desplazamiento si la pantalla es pequeña
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ==========================================
                // TARJETA 1: Cabecera del Pedido y Estado
                // ==========================================
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBF7)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Pedido #${p.pedido.id.toString().padStart(4, '0')}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.Black
                            )

                            // Insignia de Estado en azul sombreado
                            Surface(
                                color = Color(0xFFDBEAFE),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = when(estadoActual) {
                                        EstadoPedido.RECIBIDO.name -> "Recibido"
                                        EstadoPedido.PREPARANDO.name -> "Preparando"
                                        EstadoPedido.LISTO.name -> "Listo"
                                        EstadoPedido.ENTREGADO.name -> "Entregado"
                                        else -> estadoActual
                                    },
                                    color = Color(0xFF1D4ED8),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Letras grises: hora de emisión y recojo
                        Text(
                            text = "Hoy 10:12  ·  Recojo ${p.pedido.horaRecojo}",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }
                }

                // ==========================================
                // TARJETA 2: Cliente, Productos y Total
                // ==========================================
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBF7)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Cliente", color = Color.Gray, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${p.cliente.nombre} · ${p.cliente.rol.lowercase().replaceFirstChar { it.uppercase() }}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = Color(0xFFE2E8F0))
                        Spacer(modifier = Modifier.height(12.dp))

                        // Lista de productos del pedido
                        p.detalles.forEach { detalle ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(detalle.nombreProducto, fontSize = 14.sp, color = Color.DarkGray)
                                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                                    Text("x${detalle.cantidad}", color = Color.Gray, fontSize = 14.sp)
                                    Text(
                                        text = "S/ ${detalle.precioUnitario * detalle.cantidad}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.Black
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = Color(0xFFE2E8F0))
                        Spacer(modifier = Modifier.height(12.dp))

                        // Monto Total en Azul Negrita
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                            Text(
                                text = "S/ ${p.pedido.total}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = colorAdmin // Azul negrita
                            )
                        }
                    }
                }

                // ==========================================
                // TARJETA 3: Nota del cliente (Fondo Amarillo)
                // ==========================================
                if (p.pedido.nota.isNotBlank()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)) // Fondo amarillo/ámbar claro
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Nota del cliente",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color(0xFFB45309) // Marrón / letras oscuras
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = p.pedido.nota,
                                fontSize = 14.sp,
                                color = Color(0xFF92400E)
                            )
                        }
                    }
                }

                // ==========================================
                // SECCIÓN: Cambiar estado (4 Pastillas)
                // ==========================================
                Column {
                    Text("Cambiar estado", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val estadosPills = listOf(
                            EstadoPedido.RECIBIDO.name to "Recibido",
                            EstadoPedido.PREPARANDO.name to "Preparando",
                            EstadoPedido.LISTO.name to "Listo",
                            EstadoPedido.ENTREGADO.name to "Entregado"
                        )

                        estadosPills.forEach { (key, label) ->
                            val seleccionado = estadoActual == key
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(20.dp),
                                color = if (seleccionado) colorAdmin else Color.Transparent,
                                border = if (!seleccionado) BorderStroke(1.dp, Color(0xFFCBD5E1)) else null
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        color = if (seleccionado) Color.White else Color.DarkGray,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==========================================
            // BOTONES INFERIORES DE ACCIÓN GERENCIAL
            // ==========================================
            if (estadoActual != EstadoPedido.ENTREGADO.name && estadoActual != EstadoPedido.CANCELADO.name) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            vmAdmin.avanzar(p.pedido.id, estadoActual)
                            onVolver()
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorAdmin)
                    ) {
                        Text(
                            text = when(estadoActual) {
                                EstadoPedido.RECIBIDO.name -> "Pasar a Preparando"
                                EstadoPedido.PREPARANDO.name -> "Pasar a Listo"
                                EstadoPedido.LISTO.name -> "Pasar a Entregado"
                                else -> "Avanzar"
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            vmAdmin.cancelar(p.pedido.id)
                            onVolver()
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color(0xFFDC2626)
                        ),
                        border = BorderStroke(1.dp, Color(0xFFDC2626))
                    ) {
                        Text("Cancelar pedido", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}