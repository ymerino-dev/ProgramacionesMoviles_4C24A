package com.merinoana.clinicasalud.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var currentRoute by remember { mutableStateOf(Screen.Inicio.route) }
    val citasGlobales = remember {
        mutableStateListOf(
            Triple("Dra. Ana Torres", "Jue 26 - 9:00 am", "Confirmada"),
            Triple("Dr. Luis Vega", "Vie 20 - 10:30 am", "Completada")
        )
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // CABECERA DEL DRAWER (Perfil del usuario)
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "AM", // Iniciales de Ana Merino
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Ana Yanira Merino Ramos",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Paciente",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))

                // OPCIONES DEL MENÚ CON ICONOS CIRCULARES
                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    selected = currentRoute == Screen.Inicio.route,
                    icon = { Icon(if (currentRoute == Screen.Inicio.route) Icons.Filled.RadioButtonChecked else Icons.Outlined.RadioButtonUnchecked, contentDescription = null) },
                    onClick = {
                        currentRoute = Screen.Inicio.route
                        navController.navigate(Screen.Inicio.route)
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Mis citas") },
                    selected = currentRoute == Screen.MisCitas.route,
                    icon = { Icon(if (currentRoute == Screen.MisCitas.route) Icons.Filled.RadioButtonChecked else Icons.Outlined.RadioButtonUnchecked, contentDescription = null) },
                    onClick = {
                        currentRoute = Screen.MisCitas.route
                        navController.navigate(Screen.MisCitas.route)
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Historial médico") },
                    selected = currentRoute == Screen.Historial.route,
                    icon = { Icon(if (currentRoute == Screen.Historial.route) Icons.Filled.RadioButtonChecked else Icons.Outlined.RadioButtonUnchecked, contentDescription = null) },
                    onClick = {
                        currentRoute = Screen.Historial.route
                        navController.navigate(Screen.Historial.route)
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Perfil") },
                    selected = currentRoute == Screen.MiPerfil.route,
                    icon = { Icon(if (currentRoute == Screen.MiPerfil.route) Icons.Filled.RadioButtonChecked else Icons.Outlined.RadioButtonUnchecked, contentDescription = null) },
                    onClick = {
                        currentRoute = Screen.MiPerfil.route
                        navController.navigate(Screen.MiPerfil.route)
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Clínica Salud+") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Inicio.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Inicio.route) { InicioScreen(navController) }
                composable(Screen.MisCitas.route) { MisCitasScreen(citasGlobales) }
                composable(Screen.Historial.route) { HistorialScreen() }
                composable(Screen.MiPerfil.route) { MiPerfilScreen() }

                composable(Screen.Perfil.route) { backStackEntry ->
                    val medico = backStackEntry.arguments?.getString("medico")
                    PerfilScreen(navController, medico)
                }
                composable(Screen.Agendar.route) { backStackEntry ->
                    val medico = backStackEntry.arguments?.getString("medico")
                    AgendarScreen(navController, medico)
                }
                composable(Screen.Confirmacion.route) { backStackEntry ->
                    val medico = backStackEntry.arguments?.getString("medico")
                    val fecha = backStackEntry.arguments?.getString("fecha")
                    val hora = backStackEntry.arguments?.getString("hora")
                    ConfirmacionScreen(navController, medico, fecha, hora) { nuevaCita ->
                        citasGlobales.add(0, nuevaCita) // Añade la nueva cita arriba en la lista
                    }
                }
            }
        }
    }
}