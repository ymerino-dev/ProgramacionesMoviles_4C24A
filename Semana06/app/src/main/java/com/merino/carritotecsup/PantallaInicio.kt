package com.merino.carritotecsup

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio(
    listaProductos: List<Producto>,
    cantidadCarrito: Int,
    onNavigateToDetalle: (Int) -> Unit,
    onNavigateToCarrito: () -> Unit,
    onToggleFavorito: (Producto) -> Unit,
    onAgregarRapido: (Producto) -> Unit,
    onOpenDrawer: () -> Unit
) {
    // Estados para los filtros combinados (Buscador + Categoría + Ordenamiento por precio)
    var textoBusqueda by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf("Todos") }
    var ordenPrecio by remember { mutableStateOf("Sin orden") }

    val categorias = listOf("Todos", "Bebidas", "Abarrotes", "Snacks", "Limpieza")
    val opcionesOrden = listOf("Sin orden", "Precio: Menor a Mayor", "Precio: Mayor a Menor")

    // --- FILTRADO COMBINADO Y ORDENAMIENTO POR PRECIO ---
    val productosFiltradosYOrdenados = remember(textoBusqueda, categoriaSeleccionada, ordenPrecio, listaProductos) {
        val filtrados = listaProductos.filter { producto ->
            val coincideCategoria = categoriaSeleccionada == "Todos" || producto.categoria.equals(categoriaSeleccionada, ignoreCase = true)
            val coincideTexto = producto.nombre.contains(textoBusqueda, ignoreCase = true) ||
                    producto.descripcion.contains(textoBusqueda, ignoreCase = true)
            coincideCategoria && coincideTexto
        }

        when (ordenPrecio) {
            "Precio: Menor a Mayor" -> filtrados.sortedBy { it.precio }
            "Precio: Mayor a Menor" -> filtrados.sortedByDescending { it.precio }
            else -> filtrados
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Bodega TECSUP - Inicio") },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menú")
                    }
                },
                actions = {
                    BadgedBox(
                        badge = {
                            if (cantidadCarrito > 0) {
                                Badge {
                                    Text(text = cantidadCarrito.toString())
                                }
                            }
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        IconButton(onClick = onNavigateToCarrito) {
                            Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Carrito")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Campo de búsqueda en tiempo real
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                label = { Text("Buscar productos...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // LazyRow de Categorías con filtro
            Text("Categorías", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categorias) { cat ->
                    FilterChip(
                        selected = categoriaSeleccionada == cat,
                        onClick = { categoriaSeleccionada = cat },
                        label = { Text(cat) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Ordenamiento por precio (Menor a Mayor / Mayor a Menor)
            Text("Ordenar por precio", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(opcionesOrden) { opcion ->
                    FilterChip(
                        selected = ordenPrecio == opcion,
                        onClick = { ordenPrecio = opcion },
                        label = { Text(opcion) },
                        leadingIcon = {
                            if (ordenPrecio == opcion) {
                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Productos Destacados (${productosFiltradosYOrdenados.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // LazyColumn de productos filtrados y ordenados
            if (productosFiltradosYOrdenados.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No se encontraron productos coincidentes.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(productosFiltradosYOrdenados) { producto ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateToDetalle(producto.id) }
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = producto.imagenRes),
                                    contentDescription = producto.nombre,
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = producto.nombre,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = producto.descripcion,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray,
                                        maxLines = 1
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "S/ ${String.format("%.2f", producto.precio)}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                // Botón de Favorito (Corazón)
                                IconButton(onClick = { onToggleFavorito(producto) }) {
                                    Icon(
                                        imageVector = if (producto.esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "Favorito",
                                        tint = if (producto.esFavorito) Color.Red else Color.Gray
                                    )
                                }

                                // Botón rápido para agregar al carrito
                                IconButton(onClick = { onAgregarRapido(producto) }) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Agregar rápido",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
