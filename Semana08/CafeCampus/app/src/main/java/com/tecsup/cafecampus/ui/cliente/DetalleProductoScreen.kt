package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.viewmodel.MenuViewModel

@Composable
fun DetalleProductoScreen(
    producto: Producto,
    vm: MenuViewModel,
    onVolver: () -> Unit // Navega de regreso tras agregar al carrito
) {
    var cantidad by remember { mutableIntStateOf(1) }
    var nota by remember { mutableStateOf("") }
    val subtotal = producto.precio * cantidad

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = producto.nombre, style = MaterialTheme.typography.headlineMedium)
        Text(text = "S/ ${producto.precio}", color = Color(0xFFEA580C), style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

        if (producto.disponible) {
            SuggestionChip(onClick = { }, label = { Text("Disponible", color = Color(0xFF16A34A)) })
        } else {
            SuggestionChip(
                onClick = { },
                label = { Text("Agotado") },
                colors = SuggestionChipDefaults.suggestionChipColors(containerColor = Color.LightGray)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = producto.descripcion)

        Spacer(modifier = Modifier.height(32.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Cantidad:", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { if (cantidad > 1) cantidad-- }) {
                Icon(Icons.Default.Remove, contentDescription = "Menos")
            }
            Text(text = cantidad.toString(), style = MaterialTheme.typography.titleLarge)
            IconButton(onClick = { cantidad++ }) {
                Icon(Icons.Default.Add, contentDescription = "Más")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nota,
            onValueChange = { nota = it },
            label = { Text("Nota para la cocina (Ej: Sin ají)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                vm.agregar(producto, cantidad, nota)
                onVolver()
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = producto.disponible,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA580C))
        ) {
            Text("Agregar al pedido (S/ $subtotal)")
        }
    }
}