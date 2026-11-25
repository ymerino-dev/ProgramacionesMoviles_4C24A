package com.merino.murocampus.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.merino.murocampus.viewmodel.PostsViewModel
import com.merino.murocampus.viewmodel.UiState

@Composable
fun PantallaDetalle(
    postId: Int,
    vm: PostsViewModel,
    alVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        vm.limpiarEstadoEliminacion()
        alVolver()
    }

    LaunchedEffect(postId) {
        vm.abrir(postId)
    }

    val estado by vm.detalle.collectAsStateWithLifecycle()
    val estadoEliminacion by vm.eliminacion.collectAsStateWithLifecycle()
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }

    LaunchedEffect(estadoEliminacion) {
        if (estadoEliminacion is UiState.Exito) {
            vm.limpiarEstadoEliminacion()
            alVolver()
        }
    }

    if (mostrarDialogoEliminar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            title = { Text("Eliminar publicación") },
            text = { Text("¿Estás seguro de que deseas eliminar esta publicación?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoEliminar = false
                        vm.eliminarPost(postId)
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoEliminar = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    when (val s = estado) {
        is UiState.Cargando -> Cargando("Cargando detalle...")
        is UiState.Error -> ErrorConReintento(
            mensaje = s.mensaje,
            alReintentar = { vm.abrir(postId) }
        )
        is UiState.Exito -> {
            val post = s.datos.post
            val comentarios = s.datos.comentarios

            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = post.title,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = post.body,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(Modifier.height(4.dp))

                            val eliminando = estadoEliminacion is UiState.Cargando

                            OutlinedButton(
                                onClick = { mostrarDialogoEliminar = true },
                                enabled = !eliminando,
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.error
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                if (eliminando) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(18.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                    Spacer(Modifier.width(8.dp))
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                }
                                Text("Eliminar publicación")
                            }

                            if (estadoEliminacion is UiState.Error) {
                                Text(
                                    text = (estadoEliminacion as UiState.Error).mensaje,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Comentarios (${comentarios.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF19679E),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                items(comentarios, key = { it.id }) { comentario ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = comentario.email,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF19679E)
                            )
                            Text(
                                text = comentario.body,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}