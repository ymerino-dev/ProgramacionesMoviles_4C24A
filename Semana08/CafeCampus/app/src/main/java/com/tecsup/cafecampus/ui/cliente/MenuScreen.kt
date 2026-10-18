package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.cafecampus.data.local.entity.Usuario
import com.tecsup.cafecampus.viewmodel.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    usuario: Usuario?,
    vm: MenuViewModel,
    onVerDetalle: (Int) -> Unit,
    onVerCarrito: () -> Unit,
    onIrMisPedidos: () -> Unit,
    onIrPerfil: () -> Unit
) {
    val productos by vm.productos.collectAsState()
    val carrito by vm.carrito.collectAsState()

    var textoBusqueda by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf("Todo") }

    val cantidadCarrito = carrito.sumOf { it.cantidad }
    val colorNaranja = Color(0xFFEA580C)

    // Filtrar productos por categoría y barra de búsqueda
    val productosFiltrados = productos.filter { p ->
        val coincideCategoria = categoriaSeleccionada == "Todo" || p.categoria.equals(categoriaSeleccionada, ignoreCase = true)
        val coincideBusqueda = p.nombre.contains(textoBusqueda, ignoreCase = true)
        coincideCategoria && coincideBusqueda
    }

    Scaffold(
        topBar = {
            // Cabecera superior personalizada con saludo y bolsa de compras
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "¡Hola, ${usuario?.nombre ?: "Estudiante"}!",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "¿Qué se te antoja hoy?",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }

                // Icono de bolsa con insignia de notificación
                BadgedBox(
                    badge = {
                        if (cantidadCarrito > 0) {
                            Badge(containerColor = colorNaranja) {
                                Text(cantidadCarrito.toString(), color = Color.White)
                            }
                        }
                    }
                ) {
                    IconButton(
                        onClick = onVerCarrito,
                        modifier = Modifier
                            .background(Color(0xFFFFF7ED), shape = CircleShape)
                            .size(44.dp)
                    ) {
                        Icon(
                            Icons.Outlined.ShoppingBag,
                            contentDescription = "Bolsa de compras",
                            tint = colorNaranja
                        )
                    }
                }
            }
        }
        // Nota: El bottomBar global se encarga del menú inferior, aquí no se declara
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Barra de búsqueda
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                placeholder = { Text("Buscar en el menú...", color = Color.Gray) },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    focusedBorderColor = colorNaranja
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Filtros de categorías (Todo, Desayunos, Almuerzos, Bebidas)
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
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (seleccionado) colorNaranja else Color(0xFFF1F5F9)
                        ),
                        modifier = Modifier.height(38.dp)
                    ) {
                        Text(
                            text = cat,
                            color = if (seleccionado) Color.White else Color.DarkGray,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Populares hoy",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Cuadrícula (Grid) de productos populares estilo tarjeta
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(productosFiltrados) { p ->
                    Card(
                        modifier = Modifier
                            .height(190.dp)
                            .clickable { onVerDetalle(p.id) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Espacio reservado superior para la imagen del producto
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(75.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFFEF3C7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(p.nombre.take(3).uppercase(), color = colorNaranja, fontWeight = FontWeight.Bold)
                            }

                            // Nombre del producto
                            Text(
                                text = p.nombre,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = Color.Black
                            )

                            // Precio y botón de adición rápida
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "S/ ${p.precio}",
                                    fontWeight = FontWeight.Bold,
                                    color = colorNaranja,
                                    fontSize = 14.sp
                                )

                                IconButton(
                                    onClick = { onVerDetalle(p.id) },
                                    modifier = Modifier
                                        .size(30.dp)
                                        .background(colorNaranja, shape = CircleShape)
                                ) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = "Agregar",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
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