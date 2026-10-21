package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.outlined.ShoppingBag
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
import androidx.compose.ui.unit.sp
import com.tecsup.cafecampus.R
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
    val productosFiltrados by vm.productosFiltrados.collectAsState()
    val textoBusqueda by vm.searchQuery.collectAsState()
    val categoriaSeleccionada by vm.categoriaSeleccionada.collectAsState()
    val carrito by vm.carrito.collectAsState()

    val cantidadCarrito = carrito.sumOf { it.cantidad }
    val colorNaranja = Color(0xFFEA580C)

    Scaffold(
        topBar = {
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
                        Icon(Icons.Outlined.ShoppingBag, contentDescription = "Bolsa", tint = colorNaranja)
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { vm.onSearchQueryChange(it) },
                placeholder = { Text("Buscar en el menú...", color = Color.Gray) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.Gray)
                },
                trailingIcon = {
                    if (textoBusqueda.isNotEmpty()) {
                        IconButton(onClick = { vm.onSearchQueryChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar", tint = Color.Gray)
                        }
                    }
                },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    focusedBorderColor = colorNaranja
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            val categorias = listOf("Todo", "Desayunos", "Almuerzos", "Bebidas")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categorias.forEach { cat ->
                    val seleccionado = categoriaSeleccionada == cat
                    Button(
                        onClick = { vm.onCategoriaChange(cat) },
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (seleccionado) colorNaranja else Color(0xFFF1F5F9)
                        ),
                        modifier = Modifier.height(38.dp)
                    ) {
                        Text(cat, color = if (seleccionado) Color.White else Color.DarkGray, fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text("Populares hoy", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(12.dp))

            if (productosFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.SearchOff,
                            contentDescription = "Sin resultados",
                            tint = Color.LightGray,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No se encontraron productos",
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Prueba buscando con otro término o categoría",
                            color = Color.LightGray,
                            fontSize = 13.sp
                        )
                    }
                }
            } else {

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
                            modifier = Modifier.fillMaxSize().padding(12.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            // AQUÍ ESTÁ LA IMAGEN DEL PRODUCTO
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(75.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            ) {
                                // Selector inteligente con todas tus imágenes reales
                                val imagenRes = when {
                                    p.nombre.contains("Menú", ignoreCase = true) -> R.drawable.menu_dia
                                    p.nombre.contains("Sándwich", ignoreCase = true) || p.nombre.contains("Sandwich", ignoreCase = true) -> R.drawable.sandwich_pollp
                                    p.nombre.contains("Café", ignoreCase = true) || p.nombre.contains("Cafe", ignoreCase = true) -> R.drawable.cafe_americano
                                    p.nombre.contains("Jugo", ignoreCase = true) -> R.drawable.jugo_naranja
                                    p.nombre.contains("Keke", ignoreCase = true) || p.nombre.contains("Plátano", ignoreCase = true) -> R.drawable.keke_platano
                                    p.nombre.contains("Chicha", ignoreCase = true) -> R.drawable.chicha_morada
                                    p.nombre.contains("Empanada", ignoreCase = true) -> R.drawable.empanada
                                    p.nombre.contains("Papa", ignoreCase = true) || p.nombre.contains("Lays", ignoreCase = true) -> R.drawable.papalays
                                    else -> R.drawable.menu_dia // Imagen por defecto por si agregas otro plato
                                }

                                Image(
                                    painter = painterResource(id = imagenRes),
                                    contentDescription = p.nombre,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Text(p.nombre, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1, color = Color.Black)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("S/ ${p.precio}", fontWeight = FontWeight.Bold, color = colorNaranja, fontSize = 14.sp)
                                IconButton(
                                    onClick = { onVerDetalle(p.id) },
                                    modifier = Modifier.size(30.dp).background(colorNaranja, shape = CircleShape)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Agregar", tint = Color.White, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
}