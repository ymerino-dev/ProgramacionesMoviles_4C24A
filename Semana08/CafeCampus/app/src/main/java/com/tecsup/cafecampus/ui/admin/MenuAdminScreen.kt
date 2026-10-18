package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.viewmodel.MenuAdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuAdminScreen(
    vmAdmin: MenuAdminViewModel,
    onNuevoProducto: () -> Unit,
    onEditarProducto: (Int) -> Unit
) {
    val productos by vmAdmin.productos.collectAsState()
    var productoAEliminar by remember { mutableStateOf<Producto?>(null) }
    val colorAdmin = Color(0xFF4F46E5)

    Scaffold(
        topBar = { TopAppBar(title = { Text("Gestión del menú") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onNuevoProducto, containerColor = colorAdmin) {
                Icon(Icons.Default.Add, contentDescription = "Agregar producto", tint = Color.White)
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            items(productos) { p ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7FD))
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(p.nombre, fontWeight = FontWeight.Bold)
                            Text("${p.categoria} • S/ ${p.precio}", color = Color.Gray)
                            Spacer(modifier = Modifier.height(4.dp))

                            // Alternar disponibilidad tocando el texto
                            TextButton(
                                onClick = { vmAdmin.alternarDisponibilidad(p) },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                if (p.disponible) {
                                    Text("Disponible", color = Color(0xFF16A34A), style = MaterialTheme.typography.bodySmall)
                                } else {
                                    Text("Agotado", color = Color.Red, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                        IconButton(onClick = { onEditarProducto(p.id) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.Gray)
                        }
                        IconButton(onClick = { productoAEliminar = p }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                        }
                    }
                }
            }
        }

        // Diálogo de confirmación para no borrar por accidente
        if (productoAEliminar != null) {
            AlertDialog(
                onDismissRequest = { productoAEliminar = null },
                title = { Text("Eliminar producto") },
                text = { Text("¿Estás seguro de eliminar '${productoAEliminar?.nombre}' del menú?") },
                confirmButton = {
                    TextButton(onClick = {
                        vmAdmin.eliminar(productoAEliminar!!)
                        productoAEliminar = null
                    }) { Text("Eliminar", color = Color.Red) }
                },
                dismissButton = {
                    TextButton(onClick = { productoAEliminar = null }) { Text("Cancelar") }
                }
            )
        }
    }
}