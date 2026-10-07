package com.merino.carritotecsup

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch

@Composable
fun AppNavegacion(
    isDarkTheme: Boolean = false,
    onToggleDarkTheme: (Boolean) -> Unit = {}
) {
    val navController = rememberNavController()

    // Catálogo general de productos (con estado de favorito editable)
    val listaProductos = remember {
        mutableStateListOf(
            Producto(1, "Inca Kola 1.5L", 8.50, "Bebidas", "Gaseosa sabor nacional helada 1.5 litros.", R.drawable.inca_kola),
            Producto(2, "Coca Cola 1.5L", 8.50, "Bebidas", "Bebida gaseosa refrescante 1.5 litros.", R.drawable.coca_cola),
            Producto(3, "Arroz Costeño 5kg", 21.90, "Abarrotes", "Arroz superior extra seleccionado grano entero.", R.drawable.arroz_costeno),
            Producto(4, "Aceite Primor 1L", 11.20, "Abarrotes", "Aceite vegetal comestible soya y girasol.", R.drawable.aceite_primor),
            Producto(5, "Galletas Soda Field", 3.50, "Snacks", "Paquete de galletas de soda saladas crujientes.", R.drawable.galletas_soda),
            Producto(6, "Papas Lays 200g", 7.00, "Snacks", "Papas fritas crocantes sabor original con sal.", R.drawable.papas_lays),
            Producto(7, "Detergente Bolívar 1kg", 9.80, "Limpieza", "Detergente en polvo aroma floral lavanda.", R.drawable.detergente_bolivar),
            Producto(8, "Leche Gloria Azul 400g", 4.70, "Abarrotes", "Leche evaporada entera enriquecida con vitaminas.", R.drawable.leche_gloria)
        )
    }

    // Estado compartido del carrito de compras en memoria
    val carritoProductos = remember { mutableStateListOf<Producto>() }

    // Historial compartido de pedidos confirmados
    val listaPedidos = remember { mutableStateListOf<Pedido>() }

    // Estado del Drawer lateral
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Conocer la ruta actual
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val routeActual = navBackStackEntry?.destination?.route ?: Screen.Inicio.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = (routeActual != Screen.Login.route && routeActual != Screen.CrearCuenta.route),
        drawerContent = {
            AppDrawer(
                routeActual = routeActual,
                onNavigate = { destino ->
                    if (destino == "login") {
                        carritoProductos.clear()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    } else {
                        navController.navigate(destino) {
                            popUpTo(Screen.Inicio.route) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
                onCloseDrawer = {
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { 500 }, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -500 }, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300)) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -500 }, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { 500 }, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300)) }
        ) {
            // 1. Pantalla de Login
            composable(Screen.Login.route) {
                PantallaLogin(
                    onNavigateToCrearCuenta = {
                        navController.navigate(Screen.CrearCuenta.route)
                    },
                    onNavigateToInicio = {
                        navController.navigate(Screen.Inicio.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }

            // 2. Pantalla Crear Cuenta
            composable(Screen.CrearCuenta.route) {
                PantallaCrearCuenta(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onRegisterSuccess = {
                        navController.navigate(Screen.Inicio.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }

            // 3. Pantalla Inicio (Catálogo con buscador, filtros, ordenamiento y badge)
            composable(Screen.Inicio.route) {
                PantallaInicio(
                    listaProductos = listaProductos,
                    cantidadCarrito = carritoProductos.sumOf { it.cantidad },
                    onNavigateToDetalle = { productoId ->
                        navController.navigate(Screen.DetalleProducto.createRoute(productoId))
                    },
                    onNavigateToCarrito = {
                        navController.navigate(Screen.Carrito.route)
                    },
                    onToggleFavorito = { prod ->
                        val idx = listaProductos.indexOfFirst { it.id == prod.id }
                        if (idx != -1) {
                            listaProductos[idx] = listaProductos[idx].copy(esFavorito = !listaProductos[idx].esFavorito)
                        }
                    },
                    onAgregarRapido = { producto ->
                        val existente = carritoProductos.find { it.id == producto.id }
                        if (existente != null) {
                            existente.cantidad++
                        } else {
                            carritoProductos.add(producto.copy(cantidad = 1))
                        }
                    },
                    onOpenDrawer = {
                        scope.launch { drawerState.open() }
                    }
                )
            }

            // 4. Pantalla Detalle de Producto
            composable(
                route = Screen.DetalleProducto.route,
                arguments = listOf(navArgument("productoId") { type = NavType.IntType })
            ) { backStackEntry ->
                val productoId = backStackEntry.arguments?.getInt("productoId") ?: 0
                PantallaDetalleProducto(
                    productoId = productoId,
                    listaProductos = listaProductos,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onToggleFavorito = { prod ->
                        val idx = listaProductos.indexOfFirst { it.id == prod.id }
                        if (idx != -1) {
                            listaProductos[idx] = listaProductos[idx].copy(esFavorito = !listaProductos[idx].esFavorito)
                        }
                    },
                    onAgregarAlCarrito = { producto, cantidad ->
                        val existente = carritoProductos.find { it.id == producto.id }
                        if (existente != null) {
                            existente.cantidad += cantidad
                        } else {
                            carritoProductos.add(producto.copy(cantidad = cantidad))
                        }
                    }
                )
            }

            // 5. Pantalla Carrito
            composable(Screen.Carrito.route) {
                PantallaCarrito(
                    carritoProductos = carritoProductos,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onProceedToCheckout = {
                        navController.navigate(Screen.DatosEntrega.route)
                    }
                )
            }

            // 6. Pantalla Datos de Entrega y Pago
            composable(Screen.DatosEntrega.route) {
                val subtotal = carritoProductos.sumOf { it.precio * it.cantidad }
                PantallaDatosEntrega(
                    subtotal = subtotal,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onConfirmarPedido = { tipoEntrega, metodoPago, direccion, total ->
                        // Guardamos el pedido en el historial de "Mis Pedidos"
                        val nuevoPedido = Pedido(
                            productos = carritoProductos.toList(),
                            tipoEntrega = tipoEntrega,
                            metodoPago = metodoPago,
                            total = total
                        )
                        listaPedidos.add(0, nuevoPedido)

                        navController.navigate(Screen.Confirmacion.route) {
                            popUpTo(Screen.Inicio.route) { inclusive = false }
                        }
                    }
                )
            }

            // 7. Pantalla Confirmación de Pedido
            composable(Screen.Confirmacion.route) {
                PantallaConfirmacion(
                    onVolverInicio = {
                        carritoProductos.clear()
                        navController.navigate(Screen.Inicio.route) {
                            popUpTo(Screen.Inicio.route) { inclusive = true }
                        }
                    }
                )
            }

            // 8. Pantalla Mis Pedidos (Historial de compras)
            composable(Screen.Pedidos.route) {
                PantallaPedidos(
                    listaPedidos = listaPedidos,
                    onOpenDrawer = {
                        scope.launch { drawerState.open() }
                    }
                )
            }

            // 9. Pantalla Favoritos
            composable(Screen.Favoritos.route) {
                PantallaFavoritos(
                    listaProductos = listaProductos,
                    onNavigateToDetalle = { productoId ->
                        navController.navigate(Screen.DetalleProducto.createRoute(productoId))
                    },
                    onToggleFavorito = { prod ->
                        val idx = listaProductos.indexOfFirst { it.id == prod.id }
                        if (idx != -1) {
                            listaProductos[idx] = listaProductos[idx].copy(esFavorito = !listaProductos[idx].esFavorito)
                        }
                    },
                    onAgregarRapido = { producto ->
                        val existente = carritoProductos.find { it.id == producto.id }
                        if (existente != null) {
                            existente.cantidad++
                        } else {
                            carritoProductos.add(producto.copy(cantidad = 1))
                        }
                    },
                    onOpenDrawer = {
                        scope.launch { drawerState.open() }
                    }
                )
            }

            // 10. Pantalla Perfil y Modo Oscuro
            composable(Screen.Perfil.route) {
                PantallaPerfil(
                    isDarkTheme = isDarkTheme,
                    onToggleDarkTheme = onToggleDarkTheme,
                    onOpenDrawer = {
                        scope.launch { drawerState.open() }
                    }
                )
            }
        }
    }
}
