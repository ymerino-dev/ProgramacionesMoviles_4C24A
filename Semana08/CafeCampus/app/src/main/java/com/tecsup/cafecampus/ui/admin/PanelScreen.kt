package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.viewmodel.PanelAdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanelScreen(
    vmPanel: PanelAdminViewModel,
    nuevosPedidos: List<PedidoConDetalle>,
    onPreparar: (Int, String) -> Unit,
    onVerTodos: () -> Unit
) {
    val nuevos by vmPanel.nuevos.collectAsState()
    val preparando by vmPanel.preparando.collectAsState()
    val listos by vmPanel.listos.collectAsState()
    val ventasHoy by vmPanel.ventasHoy.collectAsState()

    val colorAdmin = Color(0xFF4F46E5) // Color Índigo del Administrador

    Scaffold(
        topBar = { TopAppBar(title = { Text("Panel de control") }) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            // 1. Contadores de colores superiores
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ContadorCard("Nuevos", nuevos, Color(0xFF3B82F6), Modifier.weight(1f))
                ContadorCard("Preparando", preparando, Color(0xFFD97706), Modifier.weight(1f))
                ContadorCard("Listos", listos, Color(0xFF16A34A), Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Tarjeta de ventas del día
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = colorAdmin)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Ventas del día", color = Color.White)
                    Text("S/ $ventasHoy", color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Resumen de nuevos pedidos
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Nuevos pedidos", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = onVerTodos) {
                    Text("Ver todos", color = colorAdmin)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(nuevosPedidos.take(3)) { p ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7FD))
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Pedido #${p.pedido.id.toString().padStart(4, '0')}", fontWeight = FontWeight.Bold)
                                Text("${p.detalles.sumOf { it.cantidad }} productos • Recojo: ${p.pedido.horaRecojo}", color = Color.Gray)
                            }
                            Button(
                                onClick = { onPreparar(p.pedido.id, p.pedido.estado) },
                                colors = ButtonDefaults.buttonColors(containerColor = colorAdmin)
                            ) {
                                Text("Preparar")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContadorCard(titulo: String, cantidad: Int, color: Color, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(titulo, color = color, fontWeight = FontWeight.Bold)
            Text(cantidad.toString(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = color)
        }
    }
}