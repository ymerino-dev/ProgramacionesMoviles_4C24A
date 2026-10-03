package com.merino.carritotecsup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio(
    onNavigateToDetalle: (Int) -> Unit,
    onNavigateToCarrito: () -> Unit,
    onAgregarRapido: (Producto) -> Unit
) {
    // Lista de productos simulados de "Mi Bodega"
    val listaProductos = remember {
        listOf(
            Producto(1, "Inca Kola 1.5L", 8.50, "Bebidas", "Gaseosa sabor nacional helada 1.5 litros."),
            Producto(2, "Coca Cola 1.5L", 8.50, "Bebidas", "Bebida gaseosa refrescante 1.5 litros."),
            Producto(3, "Arroz Costeño 5kg", 21.90, "Abarrotes", "Arroz superior extra seleccionado grano entero."),
            Producto(4, "Aceite Primor 1L", 11.20, "Abarrotes", "Aceite vegetal comestible soya y girasol."),
            Producto(5, "Galletas Soda Field", 3.50, "Snacks", "Paquete de galletas de soda saladas crujientes."),
            Producto(6, "Papas Lays 200g", 7.00, "Snacks", "Papas fritas crocantes sabor original con sal."),
            Producto(7, "Detergente Bolívar 1kg", 9.80, "Limpieza", "Detergente en polvo aroma floral lavanda."),
            Producto(8, "Leche Gloria Azul 400g", 4.70, "Abarrotes", "Leche evaporada entera enriquecida con vitaminas.")
        )
    }

    // Estados para los filtros combinados (Buscador + Categoría)
    var textoBusqueda by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf("Todos") }

    val categorias = listOf("Todos", "Bebidas", "Abarrotes", "Snacks", "Limpieza")

    // --- FILTRADO COMBINADO EN TIEMPO REAL (Requisito Fase 2) ---
    val productosFiltrados = remember(textoBusqueda, categoriaSeleccionada) {
        listaProductos.filter { producto ->
            val coincideCategoria = categoriaSeleccionada == "Todos" || producto.categoria.equals(categoriaSeleccionada, ignoreCase = true)
            val coincideTexto = producto.nombre.contains(textoBusqueda, ignoreCase = true) ||
                    producto.descripcion.contains(textoBusqueda, ignoreCase = true)
            coincideCategoria && coincideTexto
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Bodega TECSUP - Inicio") },
                actions = {
                    IconButton(onClick = onNavigateToCarrito) {
                        Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Carrito")
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
            Spacer(modifier = Modifier.height(8.dp))

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

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Productos Destacados (${productosFiltrados.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // LazyColumn de productos filtrados en tiempo real
            if (productosFiltrados.isEmpty()) {
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
                    items(productosFiltrados) { producto ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateToDetalle(producto.id) }
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
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
