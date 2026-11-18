package com.merino.centrotareas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.merino.centrotareas.model.CargaUi
import com.merino.centrotareas.model.PrimosUi
import com.merino.centrotareas.model.RegistroTiempo
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
    val forzarTimeout by viewModel.forzarTimeout.collectAsState()
    val historial by viewModel.historial.collectAsState()
    val historialTiempos by viewModel.historialTiempos.collectAsState()

    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    val categorias = listOf("Progreso", "Primos", "Cargas", "Historial")

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
        ) {
            // --- MENÚ SUPERIOR DE SELECCIÓN CON PRIMARYTABROW ---
            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                contentColor = azulTema
            ) {
                categorias.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            // --- CUERPO PRINCIPAL SEGÚN SECCIÓN SELECCIONADA ---
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                when (selectedTabIndex) {
                    0 -> SeccionProgreso(
                        progreso = progreso,
                        corriendo = corriendo,
                        mensaje = mensaje,
                        azulTema = azulTema,
                        onIniciar = { viewModel.tareaLarga() },
                        onCancelar = { viewModel.cancelar() }
                    )
                    1 -> SeccionPrimos(
                        estadoPrimos = estadoPrimos,
                        azulTema = azulTema,
                        onContar = { hasta -> viewModel.contar(hasta) },
                        onCancelar = { viewModel.cancelarPrimos() }
                    )
                    2 -> SeccionCargas(
                        estadoCarga = estadoCarga,
                        simularError = simularError,
                        forzarTimeout = forzarTimeout,
                        historialTiempos = historialTiempos,
                        azulTema = azulTema,
                        onCambiarSimularError = { viewModel.cambiarSimularError(it) },
                        onCambiarForzarTimeout = { viewModel.cambiarForzarTimeout(it) },
                        onCargarUnaPorUna = { viewModel.cargarUnaPorUna() },
                        onCargarALaVez = { viewModel.cargarALaVez() },
                        onLimpiarTabla = { viewModel.limpiarHistorialTiempos() }
                    )
                    3 -> SeccionHistorial(
                        historial = historial,
                        azulTema = azulTema,
                        onLimpiar = { viewModel.limpiarHistorial() }
                    )
                }
            }
        }
    }
}

@Composable
private fun SeccionProgreso(
    progreso: Float,
    corriendo: Boolean,
    mensaje: String,
    azulTema: Color,
    onIniciar: () -> Unit,
    onCancelar: () -> Unit
) {
    var toques by rememberSaveable { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Prueba: Emisión de Progreso con Flow",
                    fontWeight = FontWeight.Bold,
                    color = azulTema
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ejecuta una tarea simulada de 100 pasos desde el Repository emitiendo valores progresivos mediante Flow en Dispatchers.Default sin bloquear el hilo UI.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
            }
        }

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
                verticalArrangement = Arrangement.spacedBy(12.dp)
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
                        onClick = onIniciar,
                        enabled = !corriendo,
                        colors = ButtonDefaults.buttonColors(containerColor = azulTema)
                    ) {
                        Text("Iniciar")
                    }
                    OutlinedButton(
                        onClick = onCancelar,
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
    }
}

@Composable
private fun SeccionPrimos(
    estadoPrimos: PrimosUi,
    azulTema: Color,
    onContar: (Int) -> Unit,
    onCancelar: () -> Unit
) {
    var numeroInput by rememberSaveable { mutableStateOf("500000") }
    val calculando = estadoPrimos is PrimosUi.Calculando

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Prueba: Primos con Progreso en Tiempo Real",
                    fontWeight = FontWeight.Bold,
                    color = azulTema
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Calcula números primos en Dispatchers.Default reportando el porcentaje de avance cada 1% con capacidad de cancelación instantánea.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
            }
        }

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
                verticalArrangement = Arrangement.spacedBy(12.dp)
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
                                onContar(n)
                            }
                        },
                        enabled = !calculando,
                        colors = ButtonDefaults.buttonColors(containerColor = azulTema)
                    ) {
                        Text("Contar")
                    }
                    OutlinedButton(
                        onClick = onCancelar,
                        enabled = calculando
                    ) {
                        Text("Cancelar")
                    }
                }

                when (estadoPrimos) {
                    is PrimosUi.Inicial -> {}
                    is PrimosUi.Calculando -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            LinearProgressIndicator(
                                progress = { estadoPrimos.progreso },
                                modifier = Modifier.fillMaxWidth(),
                                color = azulTema
                            )
                            Text(
                                text = "${(estadoPrimos.progreso * 100).toInt()} %",
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                        }
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
                                text = "Primos hasta $numeroInput: ${estadoPrimos.cantidad}",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B5E20)
                            )
                            Text(
                                text = "Tiempo: ${estadoPrimos.milisegundos} ms",
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
    }
}

@Composable
private fun SeccionCargas(
    estadoCarga: CargaUi,
    simularError: Boolean,
    forzarTimeout: Boolean,
    historialTiempos: List<RegistroTiempo>,
    azulTema: Color,
    onCambiarSimularError: (Boolean) -> Unit,
    onCambiarForzarTimeout: (Boolean) -> Unit,
    onCargarUnaPorUna: () -> Unit,
    onCargarALaVez: () -> Unit,
    onLimpiarTabla: () -> Unit
) {
    val cargando = estadoCarga is CargaUi.Cargando

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Prueba: Concurrencia, Timeout y Reintentos",
                    fontWeight = FontWeight.Bold,
                    color = azulTema
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Compara cargas secuenciales vs. paralelas con reintentos automáticos (espera creciente), control de timeout y tabla comparativa de tiempos.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
            }
        }

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
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Gestión de Cargas Asíncronas",
                    style = MaterialTheme.typography.titleMedium,
                    color = azulTema,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.Start)
                )

                // --- CONTENEDOR SECUNDARIO: CONDICIONES DE PRUEBA ---
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF1F3F4),
                    tonalElevation = 1.dp
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Condiciones de prueba",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Simular error de red", style = MaterialTheme.typography.bodyMedium)
                            Switch(
                                checked = simularError,
                                onCheckedChange = onCambiarSimularError,
                                enabled = !cargando
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Forzar Timeout (2s)", style = MaterialTheme.typography.bodyMedium)
                            Switch(
                                checked = forzarTimeout,
                                onCheckedChange = onCambiarForzarTimeout,
                                enabled = !cargando
                            )
                        }
                    }
                }

                // --- BOTONES DE ACCIÓN ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onCargarUnaPorUna,
                        enabled = !cargando,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Una por una")
                    }
                    Button(
                        onClick = onCargarALaVez,
                        enabled = !cargando,
                        colors = ButtonDefaults.buttonColors(containerColor = azulTema),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("A la vez")
                    }
                }

                // --- ESTADO DE CARGA / RESULTADO ---
                when (val estado = estadoCarga) {
                    is CargaUi.Inicial -> {}
                    is CargaUi.Cargando -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            CircularProgressIndicator(color = azulTema)
                            Text(
                                text = estado.mensaje,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
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
                        OutlinedButton(onClick = onCargarALaVez) {
                            Text("Reintentar")
                        }
                    }
                }
            }
        }

        // --- TABLA COMPARATIVA DE TIEMPOS ---
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.outlinedCardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tabla comparativa de tiempos",
                        style = MaterialTheme.typography.titleMedium,
                        color = azulTema,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (historialTiempos.isNotEmpty()) {
                        TextButton(onClick = onLimpiarTabla) {
                            Text("Limpiar tabla", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }

                if (historialTiempos.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay registros de tiempo aún.\nRealiza una carga exitosa para comparar.",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                } else {
                    // Cabecera de la tabla (3 columnas: Intento, Modo, Tiempo ms)
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFEEEEEE),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Intento", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            Text(text = "Modo", fontWeight = FontWeight.Bold, modifier = Modifier.weight(2f))
                            Text(text = "Tiempo", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                        }
                    }

                    // Filas de la tabla
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 250.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(historialTiempos) { registro ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = Color(0xFFF9F9F9),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "#${registro.intento}", modifier = Modifier.weight(1f))
                                    Text(text = registro.modo, modifier = Modifier.weight(2f), fontWeight = FontWeight.Medium)
                                    Text(
                                        text = "${registro.milisegundos} ms",
                                        modifier = Modifier.weight(1f),
                                        textAlign = TextAlign.End,
                                        color = Color(0xFF2E7D32),
                                        fontWeight = FontWeight.Bold
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

@Composable
private fun SeccionHistorial(
    historial: List<String>,
    azulTema: Color,
    onLimpiar: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Historial de Resultados Guardados",
                    fontWeight = FontWeight.Bold,
                    color = azulTema
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Registro de resultados obtenidos tras completar pruebas de progreso, cálculos de primos o cargas de datos.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
            }
        }

        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.outlinedCardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Resultados guardados",
                        style = MaterialTheme.typography.titleMedium,
                        color = azulTema,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (historial.isNotEmpty()) {
                        TextButton(onClick = onLimpiar) {
                            Text("Limpiar", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }

                if (historial.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay registros guardados en el historial todavía.\nEjecuta tareas en las pestañas anteriores para registrarlas.",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(historial) { item ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                            ) {
                                Text(
                                    text = item,
                                    modifier = Modifier.padding(12.dp),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}