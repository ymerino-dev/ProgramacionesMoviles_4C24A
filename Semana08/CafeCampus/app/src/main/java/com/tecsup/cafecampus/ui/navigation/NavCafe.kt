package com.tecsup.cafecampus.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.tecsup.cafecampus.data.prefs.PreferenciasStore
import com.tecsup.cafecampus.ui.admin.*
import com.tecsup.cafecampus.ui.cliente.*
import com.tecsup.cafecampus.viewmodel.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
@Composable
fun NavCafe(
    prefs: PreferenciasStore,
    sesionVm: SesionViewModel,
    menuVm: MenuViewModel,
    pedidosClienteVm: PedidosClienteViewModel,
    panelAdminVm: PanelAdminViewModel,
    pedidosAdminVm: PedidosAdminViewModel,
    menuAdminVm: MenuAdminViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry?.destination?.route

    val usuarioActual by sesionVm.usuarioActual.collectAsState()
    val perfilGuardado by prefs.perfilActivo.collectAsState(initial = "")
    val rolActivo = if (perfilGuardado.isNotEmpty()) perfilGuardado else (usuarioActual?.rol ?: "")

    val rutasSinBarraInferior = listOf(
        Rutas.Splash.ruta, Rutas.Login.ruta, Rutas.ClienteDetalleProducto.ruta, Rutas.AdminFormulario.ruta
    )

    Scaffold(
        bottomBar = {
            if (rutaActual != null && !rutasSinBarraInferior.contains(rutaActual)) {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    if (rolActivo == "CLIENTE") {
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.RestaurantMenu, "Menú") },
                            label = { Text("Menú") },
                            selected = rutaActual == Rutas.ClienteMenu.ruta,
                            onClick = { navController.navigate(Rutas.ClienteMenu.ruta) }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Receipt, "Pedidos") },
                            label = { Text("Mis Pedidos") },
                            selected = rutaActual == Rutas.ClienteMisPedidos.ruta,
                            onClick = { navController.navigate(Rutas.ClienteMisPedidos.ruta) }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Person, "Perfil") },
                            label = { Text("Perfil") },
                            selected = rutaActual == Rutas.ClientePerfil.ruta,
                            onClick = { navController.navigate(Rutas.ClientePerfil.ruta) }
                        )
                    } else if (rolActivo == "ADMIN") {
                        // SOLO 4 BOTONES REQUERIDOS POR EL PDF (Sin perfil redundante)
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Dashboard, "Panel") },
                            label = { Text("Panel") },
                            selected = rutaActual == Rutas.AdminPanel.ruta,
                            onClick = { navController.navigate(Rutas.AdminPanel.ruta) }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.ListAlt, "Pedidos") },
                            label = { Text("Pedidos") },
                            selected = rutaActual == Rutas.AdminPedidos.ruta,
                            onClick = { navController.navigate(Rutas.AdminPedidos.ruta) }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.MenuBook, "Menú") },
                            label = { Text("Menú") },
                            selected = rutaActual == Rutas.AdminMenu.ruta,
                            onClick = { navController.navigate(Rutas.AdminMenu.ruta) }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Settings, "Ajustes") },
                            label = { Text("Ajustes") },
                            selected = rutaActual == Rutas.AdminAjustes.ruta,
                            onClick = { navController.navigate(Rutas.AdminAjustes.ruta) }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Rutas.Splash.ruta,
            modifier = Modifier.padding(paddingValues),
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) },
            popEnterTransition = { fadeIn(animationSpec = tween(300)) },
            popExitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            composable(Rutas.Splash.ruta) {
                SplashScreen {
                    if (rolActivo.isNotEmpty()) {
                        val destino = if (rolActivo == "CLIENTE") Rutas.ClienteMenu.ruta else Rutas.AdminPanel.ruta
                        navController.navigate(destino) { popUpTo(0) }
                    } else {
                        navController.navigate(Rutas.Login.ruta) { popUpTo(0) }
                    }
                }
            }

            composable(Rutas.Login.ruta) {
                LoginScreen(
                    vm = sesionVm,
                    onLoginSuccess = { rol ->
                        val destino = if (rol == "CLIENTE") Rutas.ClienteMenu.ruta else Rutas.AdminPanel.ruta
                        navController.navigate(destino) { popUpTo(0) }
                    }
                )
            }

            // --- RUTAS DEL CLIENTE ---
            composable(Rutas.ClienteMenu.ruta) {
                val usuarioActualState by sesionVm.usuarioActual.collectAsState()
                MenuScreen(
                    usuario = usuarioActualState,
                    vm = menuVm,
                    onVerDetalle = { id -> navController.navigate(Rutas.ClienteDetalleProducto.crearRuta(id)) },
                    onVerCarrito = { navController.navigate(Rutas.ClienteConfirmar.ruta) },
                    onIrMisPedidos = { navController.navigate(Rutas.ClienteMisPedidos.ruta) },
                    onIrPerfil = { navController.navigate(Rutas.ClientePerfil.ruta) }
                )
            }

            composable(Rutas.ClientePerfil.ruta) {
                val usuarioActualState by sesionVm.usuarioActual.collectAsState()
                PerfilClienteScreen(
                    usuario = usuarioActualState,
                    prefs = prefs,
                    onCambiarPerfil = {
                        val nuevoRol = if (rolActivo == "ADMIN") "CLIENTE" else "ADMIN"
                        kotlinx.coroutines.GlobalScope.launch(kotlinx.coroutines.Dispatchers.IO) {
                            prefs.guardarPerfil(nuevoRol)
                        }
                        val destinoSiguiente = if (nuevoRol == "CLIENTE") Rutas.ClienteMenu.ruta else Rutas.AdminPanel.ruta
                        navController.navigate(destinoSiguiente) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onCerrarSesion = {
                        sesionVm.logout()
                        navController.navigate(Rutas.Login.ruta) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable(Rutas.ClienteDetalleProducto.ruta, arguments = listOf(navArgument("id") { type = NavType.IntType })) { backStack ->
                val id = backStack.arguments?.getInt("id") ?: 0
                val productos by menuVm.productos.collectAsState()
                val p = productos.find { id == it.id }
                if (p != null) {
                    DetalleProductoScreen(producto = p, vm = menuVm, onVolver = { navController.popBackStack() })
                }
            }

            composable(Rutas.ClienteConfirmar.ruta) {
                val carrito by menuVm.carrito.collectAsState()
                ConfirmarPedidoScreen(
                    clienteId = usuarioActual?.id ?: 1,
                    carrito = carrito,
                    total = menuVm.total(),
                    vmPedidos = pedidosClienteVm,
                    onSeguirPidiendo = { navController.popBackStack() },
                    onConfirmado = { nuevoId ->
                        menuVm.vaciar()
                        navController.navigate(Rutas.ClienteSeguimiento.crearRuta(nuevoId.toInt())) {
                            popUpTo(Rutas.ClienteMenu.ruta)
                        }
                    }
                )
            }

            composable(Rutas.ClienteMisPedidos.ruta) {
                val usuarioActualState by sesionVm.usuarioActual.collectAsState()

                // Sincronizamos el ID del cliente. Si estamos en modo práctica (usuario null), usamos 1 por defecto
                LaunchedEffect(usuarioActualState) {
                    val idCliente = usuarioActualState?.id ?: 1
                    pedidosClienteVm.setClienteId(idCliente)
                }

                MisPedidosScreen(
                    vmPedidos = pedidosClienteVm,
                    onVerDetalle = { id -> navController.navigate(Rutas.ClienteSeguimiento.crearRuta(id)) },
                    onRepetirPedido = { pedido -> menuVm.repetirPedido(pedido) },
                    onIrAlCarrito = { navController.navigate(Rutas.ClienteConfirmar.ruta) }
                )
            }

            composable(Rutas.ClienteSeguimiento.ruta, arguments = listOf(navArgument("id") { type = NavType.IntType })) { backStack ->
                SeguimientoScreen(
                    pedidoId = backStack.arguments?.getInt("id") ?: 0,
                    vmPedidos = pedidosClienteVm,
                    onVolver = { navController.navigate(Rutas.ClienteMenu.ruta) { popUpTo(0) } }
                )
            }

            // --- RUTAS DEL ADMIN ---
            composable(Rutas.AdminPanel.ruta) {
                val nuevos by pedidosAdminVm.pedidos.collectAsState()
                PanelScreen(
                    vmPanel = panelAdminVm,
                    nuevosPedidos = nuevos,
                    onPreparar = { id, est -> pedidosAdminVm.avanzar(id, est) },
                    onVerTodos = { navController.navigate(Rutas.AdminPedidos.ruta) },
                    onVerDetallePedido = { idPedido ->
                        navController.navigate("admin_detalle_pedido/$idPedido")
                    }
                )
            }

            composable(Rutas.AdminPedidos.ruta) {
                PedidosAdminScreen(
                    vmAdmin = pedidosAdminVm,
                    onVerDetallePedido = { idPedido ->
                        navController.navigate("admin_detalle_pedido/$idPedido")
                    }
                )
            }

            // NUEVA RUTA: Detalle individual del pedido para el Administrador
            composable("admin_detalle_pedido/{id}", arguments = listOf(navArgument("id") { type = NavType.IntType })) { backStack ->
                val idPedido = backStack.arguments?.getInt("id") ?: 0
                DetallePedidoAdminScreen(
                    pedidoId = idPedido,
                    vmAdmin = pedidosAdminVm,
                    onVolver = { navController.popBackStack() }
                )
            }
            composable(Rutas.AdminMenu.ruta) {
                MenuAdminScreen(vmAdmin = menuAdminVm, onNuevoProducto = { navController.navigate(Rutas.AdminFormulario.crearRuta(0)) }, onEditarProducto = { id -> navController.navigate(Rutas.AdminFormulario.crearRuta(id)) })
            }

            composable(Rutas.AdminFormulario.ruta, arguments = listOf(navArgument("id") { type = NavType.IntType; defaultValue = 0 })) { backStack ->
                val id = backStack.arguments?.getInt("id") ?: 0
                val productos by menuAdminVm.productos.collectAsState()
                FormularioProductoScreen(productoAEditar = productos.find { id == it.id }, vmAdmin = menuAdminVm, onVolver = { navController.popBackStack() })
            }

            composable(Rutas.AdminAjustes.ruta) {
                AjustesScreen(
                    usuario = usuarioActual,
                    prefs = prefs,
                    onCambiarPerfil = {
                        // 1. Matamos la sesión actual en memoria del ViewModel
                        sesionVm.logout()

                        // 2. Determinamos el rol al que queremos saltar
                        val nuevoRol = if (rolActivo == "ADMIN") "CLIENTE" else "ADMIN"

                        // 3. Guardamos el nuevo perfil en las preferencias
                        kotlinx.coroutines.GlobalScope.launch(kotlinx.coroutines.Dispatchers.IO) {
                            prefs.guardarPerfil(nuevoRol)
                        }

                        // 4. Redirigimos de inmediato a la pantalla principal correspondiente
                        val destino = if (nuevoRol == "CLIENTE") Rutas.ClienteMenu.ruta else Rutas.AdminPanel.ruta
                        navController.navigate(destino) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onCerrarSesion = {
                        sesionVm.logout()
                        navController.navigate(Rutas.Login.ruta) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}