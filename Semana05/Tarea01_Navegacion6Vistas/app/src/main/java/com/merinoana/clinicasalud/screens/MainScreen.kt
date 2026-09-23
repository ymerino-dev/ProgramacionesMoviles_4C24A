package com.merinoana.clinicasalud.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var currentRoute by remember { mutableStateOf(Screen.Inicio.route) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Clínica Salud+",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineMedium
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    selected = currentRoute == Screen.Inicio.route,
                    onClick = {
                        currentRoute = Screen.Inicio.route
                        navController.navigate(Screen.Inicio.route)
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Mis citas") },
                    selected = currentRoute == Screen.MisCitas.route,
                    onClick = {
                        currentRoute = Screen.MisCitas.route
                        navController.navigate(Screen.MisCitas.route)
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Historial médico") },
                    selected = currentRoute == Screen.Historial.route,
                    onClick = {
                        currentRoute = Screen.Historial.route
                        navController.navigate(Screen.Historial.route)
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
            // Reemplaza el Box con el NavHost


                    NavHost(
                        navController = navController,
                        startDestination = Screen.Inicio.route,
                        modifier = Modifier.padding(innerPadding) // Padding obligatorio del Scaffold
                    ) {
                        composable(Screen.Inicio.route) { InicioScreen(navController) }
                        composable(Screen.MisCitas.route) { Text("Pantalla de Mis Citas", modifier = Modifier.padding(16.dp)) }
                        composable(Screen.Historial.route) { Text("Pantalla de Historial", modifier = Modifier.padding(16.dp)) }

                        // Rutas con extracción de parámetros
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
                            ConfirmacionScreen(navController, medico, fecha, hora)
                        }
                    }
        }
    }
}