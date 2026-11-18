package com.merino.centrotareas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.merino.centrotareas.model.CargaUi
import com.merino.centrotareas.model.PrimosUi
import com.merino.centrotareas.viewmodel.TareasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(viewModel: TareasViewModel) {
    val mensaje by viewModel.mensaje.collectAsState()
    val progreso by viewModel.progreso.collectAsState()
    val corriendo by viewModel.corriendo.collectAsState()
    val estadoPrimos by viewModel.primos.collectAsState()
    val estadoCarga by viewModel.carga.collectAsState()
    val simularError by viewModel.simularError.collectAsState()

    var toques by rememberSaveable { mutableIntStateOf(0) }
    var numeroInput by rememberSaveable { mutableStateOf("500000") }
    val azulTema = Color(0xFF1565C0)

    Scaffold(
        containerColor = Color(0xFFF8F9FA),
        topBar = {
            TopAppBar(
                title = { Text("CentroTareas", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = azulTema,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- TARJETA 1: TAREA CON PROGRESO ---
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.outlinedCardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Tarea con progreso",
                        style = MaterialTheme.typography.titleMedium,
                        color = azulTema,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Button(
                        onClick = { toques++ },
                        colors = ButtonDefaults.buttonColors(containerColor = azulTema)
                    ) {
                        Text("Tócame: $toques")
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { viewModel.tareaLarga() },
                            enabled = !corriendo,
                            colors = ButtonDefaults.buttonColors(containerColor = azulTema)
                        ) {
                            Text("Iniciar")
                        }
                        OutlinedButton(
                            onClick = { viewModel.cancelar() },
                            enabled = corriendo
                        ) {
                            Text("Cancelar")
                        }
                    }
                    LinearProgressIndicator(
                        progress = { progreso },
                        modifier = Modifier.fillMaxWidth(),
                        color = azulTema
                    )
                    Text(text = "${(progreso * 100).toInt()} %", color = Color.Gray)
                    Text(text = mensaje, color = Color.Gray)
                }
            }

            // --- TARJETA 2: CONTAR NÚMEROS PRIMOS ---
            val calculando = estadoPrimos is PrimosUi.Calculando

            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.outlinedCardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Contar números primos",
                        style = MaterialTheme.typography.titleMedium,
                        color = azulTema,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    OutlinedTextField(
                        value = numeroInput,
                        onValueChange = { texto ->
                            if (texto.all { it.isDigit() }) numeroInput = texto
                        },
                        label = { Text("Hasta el número") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = {
                                val n = numeroInput.toIntOrNull()
                                if (n != null && n >= 2) {
                                    viewModel.contar(n)
                                }
                            },
                            enabled = !calculando,
                            colors = ButtonDefaults.buttonColors(containerColor = azulTema)
                        ) {
                            Text("Contar")
                        }
                        OutlinedButton(
                            onClick = { viewModel.cancelarPrimos() },
                            enabled = calculando
                        ) {
                            Text("Cancelar")
                        }
                    }

                    when (val estado = estadoPrimos) {
                        is PrimosUi.Inicial -> {}
                        is PrimosUi.Calculando -> {
                            CircularProgressIndicator(color = azulTema)
                        }
                        is PrimosUi.Listo -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFE8F5E9), RoundedCornerShape(12.dp))
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Primos hasta $numeroInput: ${estado.cantidad}",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B5E20)
                                )
                                Text(
                                    text = "Tiempo: ${estado.milisegundos} ms",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF2E7D32)
                                )
                            }
                        }
                        is PrimosUi.Cancelado -> {
                            Text(text = "Cancelado", color = Color.Gray)
                        }
                    }
                }
            }

            // --- TARJETA 3: DOS CARGAS ---
            val cargando = estadoCarga is CargaUi.Cargando

            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.outlinedCardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Dos cargas",
                        style = MaterialTheme.typography.titleMedium,
                        color = azulTema,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Switch(
                            checked = simularError,
                            onCheckedChange = { viewModel.cambiarSimularError(it) },
                            enabled = !cargando
                        )
                        Text(text = "Simular error")
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(
                            onClick = { viewModel.cargarUnaPorUna() },
                            enabled = !cargando
                        ) {
                            Text("Una por una")
                        }
                        Button(
                            onClick = { viewModel.cargarALaVez() },
                            enabled = !cargando,
                            colors = ButtonDefaults.buttonColors(containerColor = azulTema)
                        ) {
                            Text("A la vez")
                        }
                    }

                    when (val estado = estadoCarga) {
                        is CargaUi.Inicial -> {}
                        is CargaUi.Cargando -> {
                            CircularProgressIndicator(color = azulTema)
                        }
                        is CargaUi.Exito -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFE8F5E9), RoundedCornerShape(12.dp))
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = "Modo: ${estado.modo}",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B5E20)
                                )
                                Text(
                                    text = "Perfil: ${estado.perfil}",
                                    color = Color(0xFF1B5E20)
                                )
                                Text(
                                    text = "Pedidos: ${estado.pedidos}",
                                    color = Color(0xFF1B5E20)
                                )
                                Text(
                                    text = "Tiempo: ${estado.milisegundos} ms",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF2E7D32)
                                )
                            }
                        }
                        is CargaUi.Error -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        MaterialTheme.colorScheme.errorContainer,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = estado.mensaje,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            OutlinedButton(onClick = { viewModel.cargarALaVez() }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
            }
        }
    }
}