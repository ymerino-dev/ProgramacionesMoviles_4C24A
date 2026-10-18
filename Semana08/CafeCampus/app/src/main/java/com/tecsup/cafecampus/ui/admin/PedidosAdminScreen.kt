package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
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
fun PedidosAdminScreen(
    vmAdmin: PedidosAdminViewModel,
    onVerDetallePedido: (Int) -> Unit
) {
    val estadoActual by vmAdmin.estadoSeleccionado.collectAsState()
    val pedidos by vmAdmin.pedidos.collectAsState()
    val colorAdmin = Color(0xFF4F46E5)

    // Solo los 3 estados requeridos (Nuevos/Recibidos, Preparando, Listos)
    val estados = listOf(EstadoPedido.RECIBIDO, EstadoPedido.PREPARANDO, EstadoPedido.LISTO)

    Scaffold(
        topBar = { TopAppBar(title = { Text("Pedidos", fontWeight = FontWeight.Bold) }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            // Filtros tipo Chip superior
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                estados.forEach { est ->
                    val seleccionado = estadoActual == est
                    val nombreChip = when(est) {
                        EstadoPedido.RECIBIDO -> "Nuevos"
                        EstadoPedido.PREPARANDO -> "Preparando"
                        EstadoPedido.LISTO -> "Listos"
                        else -> est.name
                    }
                    Button(
                        onClick = { vmAdmin.seleccionarEstado(est) },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (seleccionado) colorAdmin else Color(0xFFF1F5F9)
                        ),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                        modifier = Modifier.height(38.dp)
                    ) {
                        Text(
                            text = nombreChip,
                            color = if (seleccionado) Color.White else Color.DarkGray,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de tarjetas con el formato solicitado
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pedidos) { p ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onVerDetallePedido(p.pedido.id) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBF7))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Cabecera de la tarjeta: Foto de perfil + Código y Nombre en negros + Estado en azul sombreado
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .background(Color(0xFFE0E7FF), shape = CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Person, contentDescription = "Avatar", tint = colorAdmin, modifier = Modifier.size(20.dp))
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "#${p.pedido.id.toString().padStart(4, '0')} ${p.cliente.nombre}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = Color.Black
                                    )
                                }

                                // Estado en azul sombreado
                                Surface(
                                    color = Color(0xFFDBEAFE),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = when(p.pedido.estado) {
                                            EstadoPedido.RECIBIDO.name -> "Recibido"
                                            EstadoPedido.PREPARANDO.name -> "Preparando"
                                            EstadoPedido.LISTO.name -> "Listo"
                                            else -> p.pedido.estado
                                        },
                                        color = Color(0xFF1D4ED8),
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Detalle: Cantidad x Nombre del alimento en gris
                            val resumenAlimentos = p.detalles.joinToString { "${it.cantidad} x ${it.nombreProducto}" }
                            Text(resumenAlimentos, color = Color.Gray, fontSize = 13.sp)

                            Spacer(modifier = Modifier.height(6.dp))

                            // Mas abajo: Recojo · Precio · Nota
                            val textoNota = if (p.pedido.nota.isNotBlank()) " · Nota: ${p.pedido.nota}" else ""
                            Text(
                                text = "Recojo ${p.pedido.horaRecojo}  ·  S/ ${p.pedido.total}$textoNota",
                                color = Color.DarkGray,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Botones de acción inferiores
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = { vmAdmin.avanzar(p.pedido.id, p.pedido.estado) },
                                    modifier = Modifier.weight(1f).height(42.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = colorAdmin)
                                ) {
                                    Text(
                                        text = when(p.pedido.estado) {
                                            EstadoPedido.RECIBIDO.name -> "Pasar a Preparando"
                                            EstadoPedido.PREPARANDO.name -> "Pasar a Listo"
                                            else -> "Avanzar"
                                        },
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                OutlinedButton(
                                    onClick = { vmAdmin.cancelar(p.pedido.id) },
                                    modifier = Modifier.height(42.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626))
                                ) {
                                    Text("Cancelar", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}