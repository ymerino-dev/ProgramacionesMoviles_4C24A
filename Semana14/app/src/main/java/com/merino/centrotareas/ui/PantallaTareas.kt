package com.merino.centrotareas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.merino.centrotareas.viewmodel.TareasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(viewModel: TareasViewModel) {
    val mensaje by viewModel.mensaje.collectAsState()
    val progreso by viewModel.progreso.collectAsState()
    var toques by remember { mutableIntStateOf(0) }
    val azulTema = Color(0xFF1565C0)

    Scaffold(
        containerColor = Color(0xFFF8F9FA),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "CentroTareas",
                        fontWeight = FontWeight.Bold
                    )
                },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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
                    Button(
                        onClick = { viewModel.tareaLarga() },
                        colors = ButtonDefaults.buttonColors(containerColor = azulTema)
                    ) {
                        Text("Iniciar")
                    }
                    LinearProgressIndicator(
                        progress = { progreso },
                        modifier = Modifier.fillMaxWidth(),
                        color = azulTema
                    )
                    Text(
                        text = "${(progreso * 100).toInt()} %",
                        color = Color.Gray
                    )
                    Text(
                        text = mensaje,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}