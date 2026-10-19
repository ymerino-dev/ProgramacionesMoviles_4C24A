package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.cafecampus.R
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.viewmodel.MenuAdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioProductoScreen(
    productoAEditar: Producto? = null,
    vmAdmin: MenuAdminViewModel,
    onVolver: () -> Unit
) {
    var nombre by remember { mutableStateOf(productoAEditar?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(productoAEditar?.descripcion ?: "") }
    var categoria by remember { mutableStateOf(productoAEditar?.categoria ?: "Snacks") }
    var precio by remember { mutableStateOf(productoAEditar?.precio?.toString() ?: "") }
    var disponible by remember { mutableStateOf(productoAEditar?.disponible ?: true) }

    var errorValidacion by remember { mutableStateOf("") }
    val colorAdmin = Color(0xFF4F46E5)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (productoAEditar == null) "Nuevo producto" else "Editar producto", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
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
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Tarjeta superior con la IMAGEN REAL
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEDD5))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.menu_dia),
                                contentDescription = "Imagen del producto",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    "Imagen del producto",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFEA580C),
                                    fontSize = 14.sp
                                )
                                Text("Toca para elegir color", color = Color.Gray, fontSize = 12.sp)
                            }
                        }
                    }

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it; errorValidacion = "" },
                        label = { Text("Nombre del producto", color = Color.Gray) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = categoria,
                        onValueChange = { categoria = it },
                        label = { Text("Categoría", color = Color.Gray) },
                        trailingIcon = {
                            Icon(
                                Icons.Default.ChevronRight,
                                contentDescription = "Desplegar",
                                tint = Color.Gray
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = precio,
                        onValueChange = { precio = it; errorValidacion = "" },
                        label = { Text("Precio (S/)", color = Color.Gray) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripción", color = Color.Gray) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBF7))
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Disponible en el menú",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    "Los estudiantes podrán pedirlo",
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            }
                            Switch(
                                checked = disponible,
                                onCheckedChange = { disponible = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = colorAdmin
                                )
                            )
                        }
                    }

                    if (errorValidacion.isNotEmpty()) {
                        Text(errorValidacion, color = Color(0xFFDC2626), fontSize = 13.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = {
                            val precioNum = precio.toDoubleOrNull() ?: 0.0
                            if (nombre.isBlank()) {
                                errorValidacion = "El nombre es obligatorio"
                            } else if (precioNum <= 0.0) {
                                errorValidacion = "El precio debe ser mayor que 0"
                            } else {
                                val p = Producto(
                                    id = productoAEditar?.id ?: 0,
                                    nombre = nombre,
                                    descripcion = descripcion,
                                    categoria = categoria,
                                    precio = precioNum,
                                    disponible = disponible,
                                    icono = productoAEditar?.icono ?: "cafe"
                                )
                                vmAdmin.guardar(p)
                                onVolver()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorAdmin)
                    ) {
                        Text("Guardar producto", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onVolver,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.DarkGray)
                    ) {
                        Text("Cancelar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}