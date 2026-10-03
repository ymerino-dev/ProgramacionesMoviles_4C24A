package com.merino.carritotecsup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleProducto(
    productoId: Int,
    onNavigateBack: () -> Unit,
    onAgregarAlCarrito: (Producto, Int) -> Unit
) {
    // Catálogo simulado completo para buscar por ID
    val listaProductos = listOf(
        Producto(1, "Inca Kola 1.5L", 8.50, "Bebidas", "Gaseosa sabor nacional helada 1.5 litros."),
        Producto(2, "Coca Cola 1.5L", 8.50, "Bebidas", "Bebida gaseosa refrescante 1.5 litros."),
        Producto(3, "Arroz Costeño 5kg", 21.90, "Abarrotes", "Arroz superior extra seleccionado grano entero."),
        Producto(4, "Aceite Primor 1L", 11.20, "Abarrotes", "Aceite vegetal comestible soya y girasol."),
        Producto(5, "Galletas Soda Field", 3.50, "Snacks", "Paquete de galletas de soda saladas crujientes."),
        Producto(6, "Papas Lays 200g", 7.00, "Snacks", "Papas fritas crocantes sabor original con sal."),
        Producto(7, "Detergente Bolívar 1kg", 9.80, "Limpieza", "Detergente en polvo aroma floral lavanda."),
        Producto(8, "Leche Gloria Azul 400g", 4.70, "Abarrotes", "Leche evaporada entera enriquecida con vitaminas.")
    )

    val producto = listaProductos.find { it.id == productoId } ?: Producto(0, "Producto no encontrado", 0.0, "General", "No disponible")

    var cantidad by remember { mutableStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Producto") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Regresar")
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
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = producto.categoria.uppercase(),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "S/ ${String.format("%.2f", producto.precio)}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Descripción",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = producto.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Control de Cantidad
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Cantidad:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedButton(
                            onClick = { if (cantidad > 1) cantidad-- },
                            modifier = Modifier.size(48.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("-", style = MaterialTheme.typography.titleLarge)
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = cantidad.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        OutlinedButton(
                            onClick = { cantidad++ },
                            modifier = Modifier.size(48.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("+", style = MaterialTheme.typography.titleLarge)
                        }
                    }
                }
            }

            // Botón Agregar al Carrito
            Button(
                onClick = {
                    onAgregarAlCarrito(producto, cantidad)
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("AGREGAR AL CARRITO (S/ ${String.format("%.2f", producto.precio * cantidad)})")
            }
        }
    }
}
