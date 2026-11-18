package com.merino.centrotareas.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
    val categorias: List<Triple<String, ImageVector, String>> = listOf(
        Triple("Progreso", Icons.Rounded.Speed, "Progreso"),
        Triple("Primos", Icons.Rounded.Calculate, "Primos"),
        Triple("Cargas", Icons.Rounded.SyncAlt, "Cargas"),
        Triple("Historial", Icons.Rounded.History, "Historial")
    )

    val azulTema = Color(0xFF1565C0)

    Scaffold(
        containerColor = Color(0xFFF8FAFC),
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
            // --- 1. NAVEGACIÓN SUPERIOR CON ÍCONOS Y PESTAÑAS ESTILIZADAS ---
            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                contentColor = azulTema
            ) {
                categorias.forEachIndexed { index, cat ->
                    val title = cat.first
                    val icon = cat.second
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = title,
                                tint = if (selectedTabIndex == index) azulTema else Color(0xFF64748B)
                            )
                        },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedTabIndex == index) azulTema else Color(0xFF64748B)
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
private fun BannerEducativo(
    titulo: String,
    descripcion: String,
    icono: ImageVector,
    chips: List<String>,
    azulTema: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
        border = BorderStroke(1.dp, Color(0xFFBBDEFB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    modifier = Modifier.size(40.dp),
                    tonalElevation = 2.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icono,
                            contentDescription = null,
                            tint = azulTema,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = titulo,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = azulTema
                    )
                    Text(
                        text = descripcion,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF334155)
                    )
                }
            }

            // Badges técnicos tipo píldora
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                chips.forEach { chip ->
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = Color.White.copy(alpha = 0.8f),
                        border = BorderStroke(1.dp, azulTema.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = chip,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = azulTema,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
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
        BannerEducativo(
            titulo = "Emisión de Progreso con Flow",
            descripcion = "Ejecuta una tarea reactiva de 100 pasos desde el Repository emitiendo valores mediante Flow en Dispatchers.Default sin bloquear la UI.",
            icono = Icons.Rounded.Speed,
            chips = listOf("Flow", "Dispatchers.Default", "StateFlow"),
            azulTema = azulTema
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Control de Tarea Reactiva",
                    style = MaterialTheme.typography.titleMedium,
                    color = azulTema,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start)
                )

                Button(
                    onClick = { toques++ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(Icons.Rounded.TouchApp, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tócame (Prueba de UI): $toques", fontWeight = FontWeight.SemiBold)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onIniciar,
                        enabled = !corriendo,
                        colors = ButtonDefaults.buttonColors(containerColor = azulTema),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Icon(Icons.Rounded.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Iniciar", fontWeight = FontWeight.SemiBold)
                    }
                    OutlinedButton(
                        onClick = onCancelar,
                        enabled = corriendo,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Icon(Icons.Rounded.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Cancelar", fontWeight = FontWeight.SemiBold)
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    LinearProgressIndicator(
                        progress = { progreso },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = azulTema,
                        trackColor = Color(0xFFE2E8F0)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = mensaje, style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
                        Text(text = "${(progreso * 100).toInt()} %", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = azulTema)
                    }
                }
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
        BannerEducativo(
            titulo = "Cálculo Intensivo en CPU con Progreso",
            descripcion = "Busca números primos en Dispatchers.Default reportando el porcentaje de avance cada 1% con capacidad de cancelación instantánea mediante ensureActive().",
            icono = Icons.Rounded.Calculate,
            chips = listOf("CPU-Intensive", "ensureActive()", "Cancelable"),
            azulTema = azulTema
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Contar números primos",
                    style = MaterialTheme.typography.titleMedium,
                    color = azulTema,
                    fontWeight = FontWeight.Bold,
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
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            val n = numeroInput.toIntOrNull()
                            if (n != null && n >= 2) {
                                onContar(n)
                            }
                        },
                        enabled = !calculando,
                        colors = ButtonDefaults.buttonColors(containerColor = azulTema),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Icon(Icons.Rounded.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Contar", fontWeight = FontWeight.SemiBold)
                    }
                    OutlinedButton(
                        onClick = onCancelar,
                        enabled = calculando,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Icon(Icons.Rounded.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Cancelar", fontWeight = FontWeight.SemiBold)
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
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp),
                                color = azulTema,
                                trackColor = Color(0xFFE2E8F0)
                            )
                            Text(
                                text = "Calculando primos... ${(estadoPrimos.progreso * 100).toInt()} %",
                                color = Color(0xFF64748B),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    is PrimosUi.Listo -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFE8F5E9), RoundedCornerShape(12.dp))
                                .border(1.dp, Color(0xFFC8E6C9), RoundedCornerShape(12.dp))
                                .padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Primos hasta $numeroInput: ${estadoPrimos.cantidad}",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B5E20),
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = "Tiempo de ejecución: ${estadoPrimos.milisegundos} ms",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF2E7D32)
                            )
                        }
                    }
                    is PrimosUi.Cancelado -> {
                        Text(text = "Cálculo cancelado por el usuario", color = Color(0xFF64748B), style = MaterialTheme.typography.bodySmall)
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
        BannerEducativo(
            titulo = "Concurrencia, Timeout y Reintentos",
            descripcion = "Compara cargas secuenciales vs. paralelas con async/await, reintentos automáticos con espera exponencial y control de timeout.",
            icono = Icons.Rounded.SyncAlt,
            chips = listOf("async / await", "withTimeout", "Reintento x3"),
            azulTema = azulTema
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Gestión de Cargas Asíncronas",
                    style = MaterialTheme.typography.titleMedium,
                    color = azulTema,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start)
                )

                // --- CONTENEDOR SECUNDARIO: CONDICIONES DE PRUEBA ---
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF8FAFC),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Condiciones de prueba",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF334155)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Simular error de red (IOException)", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF475569))
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
                            Text(text = "Forzar Timeout (2s)", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF475569))
                            Switch(
                                checked = forzarTimeout,
                                onCheckedChange = onCambiarForzarTimeout,
                                enabled = !cargando
                            )
                        }
                    }
                }

                // --- BOTONES DE ACCIÓN EQUILIBRADOS ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onCargarUnaPorUna,
                        enabled = !cargando,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Icon(Icons.Rounded.Timer, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Una por una", fontWeight = FontWeight.SemiBold)
                    }
                    Button(
                        onClick = onCargarALaVez,
                        enabled = !cargando,
                        colors = ButtonDefaults.buttonColors(containerColor = azulTema),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Icon(Icons.Rounded.Layers, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("A la vez", fontWeight = FontWeight.SemiBold)
                    }
                }

                // --- ESTADO DE CARGA / RESULTADO ---
                when (val estado = estadoCarga) {
                    is CargaUi.Inicial -> {}
                    is CargaUi.Cargando -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            CircularProgressIndicator(color = azulTema)
                            Text(
                                text = estado.mensaje,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF64748B),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    is CargaUi.Exito -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFE8F5E9), RoundedCornerShape(12.dp))
                                .border(1.dp, Color(0xFFC8E6C9), RoundedCornerShape(12.dp))
                                .padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Modo: ${estado.modo}",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B5E20),
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(text = "Perfil: ${estado.perfil}", color = Color(0xFF1B5E20), style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Pedidos: ${estado.pedidos}", color = Color(0xFF1B5E20), style = MaterialTheme.typography.bodyMedium)
                            Text(
                                text = "Tiempo total: ${estado.milisegundos} ms",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.SemiBold
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
                                .padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = estado.mensaje,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                fontWeight = FontWeight.Bold
                            )
                            OutlinedButton(
                                onClick = onCargarALaVez,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Reintentar operación")
                            }
                        }
                    }
                }
            }
        }

        // --- TABLA COMPARATIVA DE TIEMPOS PULIDA ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
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
                        fontWeight = FontWeight.Bold
                    )
                    if (historialTiempos.isNotEmpty()) {
                        IconButton(
                            onClick = onLimpiarTabla,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.DeleteSweep,
                                contentDescription = "Limpiar tabla",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }

                if (historialTiempos.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay registros de tiempo aún.\nEjecuta cargas para poblar la tabla.",
                            color = Color(0xFF94A3B8),
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    // Cabecera sombreada
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "#", fontWeight = FontWeight.Bold, color = Color(0xFF475569), modifier = Modifier.weight(0.8f))
                            Text(text = "Modo de Carga", fontWeight = FontWeight.Bold, color = Color(0xFF475569), modifier = Modifier.weight(2.2f))
                            Text(text = "Tiempo", fontWeight = FontWeight.Bold, color = Color(0xFF475569), modifier = Modifier.weight(1.2f), textAlign = TextAlign.End)
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 240.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(historialTiempos) { registro ->
                            val esParalelo = registro.modo.contains("A la vez", ignoreCase = true)
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = Color(0xFFFAFAFA),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(0.5.dp, Color(0xFFE2E8F0))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "#${registro.intento}",
                                        modifier = Modifier.weight(0.8f),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color(0xFF64748B),
                                        fontWeight = FontWeight.Medium
                                    )
                                    Row(
                                        modifier = Modifier.weight(2.2f),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = registro.modo,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF1E293B)
                                        )
                                        if (esParalelo) {
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = Color(0xFFDCFCE7)
                                            ) {
                                                Text(
                                                    text = "Paralelo",
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = Color(0xFF166534),
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = "${registro.milisegundos} ms",
                                        modifier = Modifier.weight(1.2f),
                                        textAlign = TextAlign.End,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color(0xFF059669),
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
        BannerEducativo(
            titulo = "Historial de Resultados",
            descripcion = "Registro cronológico de todas las tareas ejecutadas, cálculos de primos y cargas de red completadas o canceladas durante la sesión.",
            icono = Icons.Rounded.History,
            chips = listOf("Log", "StateFlow", "Sesión Actual"),
            azulTema = azulTema
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
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
                        fontWeight = FontWeight.Bold
                    )
                    if (historial.isNotEmpty()) {
                        IconButton(
                            onClick = onLimpiar,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.DeleteSweep,
                                contentDescription = "Limpiar historial",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }

                if (historial.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay registros guardados en el historial todavía.\nEjecuta pruebas en las otras pestañas para registrarlas.",
                            color = Color(0xFF94A3B8),
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
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
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = Color(0xFFF8FAFC),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(0.5.dp, Color(0xFFE2E8F0))
                            ) {
                                Text(
                                    text = item,
                                    modifier = Modifier.padding(14.dp),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF334155)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}