package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.clickable
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
import com.tecsup.cafecampus.viewmodel.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    vm: MenuViewModel,
    onVerDetalle: (Int) -> Unit,
    onVerCarrito: () -> Unit
) {
    val productos by vm.productos.collectAsState()
    val carrito by vm.carrito.collectAsState()

    val totalCarrito = vm.total()
    val cantidadCarrito = carrito.sumOf { it.cantidad }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Menú") }) },
        floatingActionButton = {
            if (carrito.isNotEmpty()) {
                ExtendedFloatingActionButton(
                    onClick = onVerCarrito,
                    containerColor = Color(0xFFEA580C),
                    contentColor = Color.White
                ) {
                    Text("Ver pedido ($cantidadCarrito) - S/ $totalCarrito")
                }
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            items(productos) { p ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp).clickable { onVerDetalle(p.id) },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBF7))
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(p.nombre, fontWeight = FontWeight.Bold)
                            Text(p.descripcion, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("S/ ${p.precio}", color = Color(0xFFEA580C), fontWeight = FontWeight.Bold)
                        }
                        if (!p.disponible) {
                            Text("Agotado", color = Color.Red, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}