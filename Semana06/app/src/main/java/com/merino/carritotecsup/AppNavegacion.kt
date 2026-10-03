package com.merino.carritotecsup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()

    // Estado compartido del carrito de compras en memoria
    val carritoProductos = remember { mutableStateListOf<Producto>() }

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
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

        // 3. Pantalla Inicio (Catálogo con buscador y filtros)
        composable(Screen.Inicio.route) {
            PantallaInicio(
                onNavigateToDetalle = { productoId ->
                    navController.navigate(Screen.DetalleProducto.createRoute(productoId))
                },
                onNavigateToCarrito = {
                    navController.navigate(Screen.Carrito.route)
                },
                onAgregarRapido = { producto ->
                    val existente = carritoProductos.find { it.id == producto.id }
                    if (existente != null) {
                        existente.cantidad++
                    } else {
                        carritoProductos.add(producto.copy(cantidad = 1))
                    }
                }
            )
        }

        // 4. Pantalla Detalle de Producto (Recibe parámetro por ruta)
        composable(
            route = Screen.DetalleProducto.route,
            arguments = listOf(navArgument("productoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getInt("productoId") ?: 0
            PantallaDetalleProducto(
                productoId = productoId,
                onNavigateBack = {
                    navController.popBackStack()
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
            PantallaDatosEntrega(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onConfirmarPedido = {
                    navController.navigate(Screen.Confirmacion.route) {
                        // Limpiamos la pila hasta Inicio para que al presionar atrás no regrese al carrito o pago
                        popUpTo(Screen.Inicio.route) { inclusive = false }
                    }
                }
            )
        }

        // 7. Pantalla Confirmación de Pedido
        composable(Screen.Confirmacion.route) {
            PantallaConfirmacion(
                onVolverInicio = {
                    // Limpiamos carrito al finalizar y volvemos al inicio limpiando pila
                    carritoProductos.clear()
                    navController.navigate(Screen.Inicio.route) {
                        popUpTo(Screen.Inicio.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
