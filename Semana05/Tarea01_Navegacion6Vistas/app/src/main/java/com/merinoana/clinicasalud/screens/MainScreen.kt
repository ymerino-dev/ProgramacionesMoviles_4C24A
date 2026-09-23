package com.merinoana.clinicasalud.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp),
                drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)
            ) {
                // CABECERA DEL DRAWER (Perfil del usuario)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "AM",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Ana Yanira Merino Ramos",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Paciente • Clínica Salud+",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // OPCIONES DEL MENÚ CON 12% OPACITY PARA EL SELECCIONADO Y TEXTO EN NEGRITA
                val itemsMenu = listOf(
                    Pair("Inicio", Screen.Inicio.route),
                    Pair("Mis citas", Screen.MisCitas.route),
                    Pair("Historial médico", Screen.Historial.route),
                    Pair("Perfil", Screen.MiPerfil.route)
                )

                itemsMenu.forEach { (label, route) ->
                    val isSelected = currentRoute == route
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = label,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        },
                        selected = isSelected,
                        icon = {
                            Icon(
                                imageVector = if (isSelected) Icons.Filled.RadioButtonChecked else Icons.Outlined.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            unselectedContainerColor = Color.Transparent
                        ),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        onClick = {
                            currentRoute = route
                            navController.navigate(route)
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Clínica Salud+",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menú", tint = MaterialTheme.colorScheme.primary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
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
                        citasGlobales.add(0, nuevaCita)
                    }
                }
            }
        }
    }
}
