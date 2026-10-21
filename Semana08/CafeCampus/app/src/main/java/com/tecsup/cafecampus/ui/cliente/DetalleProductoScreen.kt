package com.tecsup.cafecampus.ui.cliente

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Schedule
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
import com.tecsup.cafecampus.viewmodel.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    producto: Producto,
    vm: MenuViewModel,
    onVolver: () -> Unit
) {
    var cantidad by remember { mutableIntStateOf(1) }
    var nota by remember { mutableStateOf("") }

    val subtotal = producto.precio * cantidad
    val colorNaranja = Color(0xFFEA580C)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del producto", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Espacio superior para imágenes del producto
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFFFEDD5)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🖼️ [Espacio para imagen]",
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Nombre y precio principal
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = producto.nombre,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "S/ ${producto.precio}",
                        color = colorNaranja,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Indicador de disponibilidad y tiempo estimado en gris
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = if (producto.disponible) Color(0xFFDCFCE7) else Color(0xFFFEE2E2),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (producto.disponible) "Disponible" else "Agotado",
                            color = if (producto.disponible) Color(0xFF16A34A) else Color(0xFFDC2626),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.Schedule,
                            contentDescription = "Tiempo",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "10 – 15 min",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Subcomentario descriptivo del producto
                Text(
                    text = producto.descripcion.ifBlank { "Entrada + segundo + refresco. Cambia cada día según el menú de cocina." },
                    color = Color.DarkGray,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Selector de cantidad (+ / -)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Cantidad", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .background(Color(0xFFF1F5F9), shape = RoundedCornerShape(24.dp))
                            .padding(4.dp)
                    ) {
                        IconButton(
                            onClick = { if (cantidad > 1) cantidad-- },
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color(0xFFFFEDD5), shape = CircleShape)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Menos", tint = colorNaranja, modifier = Modifier.size(16.dp))
                        }

                        AnimatedContent(
                            targetState = cantidad,
                            transitionSpec = {
                                if (targetState > initialState) {
                                    (slideInVertically { height -> height } + fadeIn()).togetherWith(
                                        slideOutVertically { height -> -height } + fadeOut())
                                } else {
                                    (slideInVertically { height -> -height } + fadeIn()).togetherWith(
                                        slideOutVertically { height -> height } + fadeOut())
                                }.using(SizeTransform(clip = false))
                            },
                            label = "CantidadAnimation"
                        ) { targetCantidad ->
                            Text(
                                text = targetCantidad.toString(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }

                        IconButton(
                            onClick = { cantidad++ },
                            modifier = Modifier
                                .size(32.dp)
                                .background(colorNaranja, shape = CircleShape)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Más", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Campo de notas con ejemplo en gris (placeholder)
                Text("Nota para la cocina", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = nota,
                    onValueChange = { nota = it },
                    placeholder = { Text("Sin ají, por favor", color = Color.Gray) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFE2E8F0),
                        focusedBorderColor = colorNaranja
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Sección inferior: Total en pequeño y Botón principal
            Column {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Text("S/ $subtotal", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colorNaranja)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        vm.agregar(producto, cantidad, nota)
                        onVolver()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    enabled = producto.disponible,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorNaranja)
                ) {
                    Text("Agregar al pedido", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}