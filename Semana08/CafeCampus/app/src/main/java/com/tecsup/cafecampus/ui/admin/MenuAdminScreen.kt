package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var categoriaSeleccionada by remember { mutableStateOf("Todo") }
    var productoAEliminar by remember { mutableStateOf<Producto?>(null) }

    val colorAdmin = Color(0xFF4F46E5) // Índigo del administrador

    // Filtrado por categoría
    val productosFiltrados = productos.filter { p ->
        categoriaSeleccionada == "Todo" || p.categoria.equals(categoriaSeleccionada, ignoreCase = true)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Gestión del menú", fontWeight = FontWeight.Bold) }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNuevoProducto,
                containerColor = colorAdmin,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar producto", tint = Color.White)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // Filtros superiores de categoría (Todo, Desayunos, Almuerzos, Bebidas)
            val categorias = listOf("Todo", "Desayunos", "Almuerzos", "Bebidas")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categorias.forEach { cat ->
                    val seleccionado = categoriaSeleccionada == cat
                    Button(
                        onClick = { categoriaSeleccionada = cat },
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (seleccionado) colorAdmin else Color(0xFFF1F5F9)
                        ),
                        modifier = Modifier.height(38.dp)
                    ) {
                        Text(
                            text = cat,
                            color = if (seleccionado) Color.White else Color.DarkGray,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de productos del administrador
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(productosFiltrados) { p ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBF7))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Mini tarjeta simulando la imagen del producto con tono ámbar
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFFFEDD5)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(p.nombre.take(3).uppercase(), color = Color(0xFFEA580C), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(p.nombre, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("${p.categoria} · S/ ${p.precio}", color = Color.Gray, fontSize = 13.sp)

                                Spacer(modifier = Modifier.height(6.dp))

                                // Etiqueta de disponibilidad interactiva
                                Surface(
                                    color = if (p.disponible) Color(0xFFDCFCE7) else Color(0xFFFEE2E2),
                                    shape = RoundedCornerShape(6.dp),
                                    onClick = { vmAdmin.alternarDisponibilidad(p) }
                                ) {
                                    Text(
                                        text = if (p.disponible) "Disponible" else "Agotado",
                                        color = if (p.disponible) Color(0xFF16A34A) else Color(0xFFDC2626),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Botones de Editar y Eliminar
                            IconButton(onClick = { onEditarProducto(p.id) }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = colorAdmin)
                            }
                            IconButton(onClick = { productoAEliminar = p }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFDC2626))
                            }
                        }
                    }
                }
            }
        }

        // Diálogo de confirmación para eliminar
        if (productoAEliminar != null) {
            AlertDialog(
                onDismissRequest = { productoAEliminar = null },
                title = { Text("Eliminar producto", fontWeight = FontWeight.Bold) },
                text = { Text("¿Estás seguro de eliminar '${productoAEliminar?.nombre}' del menú?") },
                confirmButton = {
                    TextButton(onClick = {
                        vmAdmin.eliminar(productoAEliminar!!)
                        productoAEliminar = null
                    }) { Text("Eliminar", color = Color(0xFFDC2626), fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(onClick = { productoAEliminar = null }) { Text("Cancelar") }
                }
            )
        }
    }
}