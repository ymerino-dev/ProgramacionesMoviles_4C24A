package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
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
        topBar = { TopAppBar(title = { Text(if (productoAEditar == null) "Nuevo producto" else "Editar producto") }) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it; errorValidacion = "" },
                label = { Text("Nombre del producto") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoría (Ej: Bebidas, Snacks)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it; errorValidacion = "" },
                label = { Text("Precio (S/)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Disponible en el menú", modifier = Modifier.weight(1f))
                Switch(checked = disponible, onCheckedChange = { disponible = it })
            }

            if (errorValidacion.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(errorValidacion, color = Color.Red)
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val precioNum = precio.toDoubleOrNull() ?: 0.0
                    // Validaciones estrictas del contrato
                    if (nombre.isBlank()) {
                        errorValidacion = "El nombre es obligatorio"
                    } else if (precioNum <= 0.0) {
                        errorValidacion = "El precio debe ser mayor que 0"
                    } else {
                        val p = Producto(
                            id = productoAEditar?.id ?: 0, // Si el ID es 0, Room crea uno nuevo automáticamente
                            nombre = nombre,
                            descripcion = descripcion,
                            categoria = categoria,
                            precio = precioNum,
                            disponible = disponible,
                            icono = productoAEditar?.icono ?: "cafe" // Conserva el ícono o usa el por defecto
                        )
                        vmAdmin.guardar(p)
                        onVolver()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorAdmin)
            ) {
                Text("Guardar producto")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(onClick = onVolver, modifier = Modifier.fillMaxWidth().height(50.dp)) {
                Text("Cancelar")
            }
        }
    }
}