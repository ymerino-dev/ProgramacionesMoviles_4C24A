package com.tecsup.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.app.AppContainer
import com.tecsup.app.domain.model.Rol
import com.tecsup.app.ui.UiState
import com.tecsup.app.ui.admin.AdminScreen
import com.tecsup.app.ui.admin.AdminViewModel
import com.tecsup.app.ui.detalle.DetalleScreen
import com.tecsup.app.ui.detalle.DetalleViewModel
import com.tecsup.app.ui.favoritos.FavoritosScreen
import com.tecsup.app.ui.favoritos.FavoritosViewModel
import com.tecsup.app.ui.lista.ListaScreen
import com.tecsup.app.ui.lista.ListaViewModel
import com.tecsup.app.ui.login.LoginScreen
import com.tecsup.app.ui.login.LoginViewModel
import com.tecsup.app.ui.perfil.PerfilScreen

@Composable
fun AppNav(
    container: AppContainer,
    isDarkTheme: Boolean = false,
    onToggleDarkTheme: () -> Unit = {}
) {
    val navController = rememberNavController()
    var userRole by remember { mutableStateOf(Rol.USUARIO) }

    NavHost(navController = navController, startDestination = "login") {

        // Ruta de Login
        composable("login") {
            val viewModel: LoginViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return LoginViewModel(container.authRepository) as T
                    }
                }
            )
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = { sesion ->
                    userRole = sesion.rol
                    if (sesion.rol == Rol.ADMIN) {
                        navController.navigate("admin") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        navController.navigate("lista") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            )
        }

        // Ruta del Catálogo de Productos
        composable("lista") {
            val viewModel: ListaViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return ListaViewModel(container.itemRepository, container.notificador) as T
                    }
                }
            )
            ListaScreen(
                viewModel = viewModel,
                userRole = userRole,
                onItemClick = { itemId ->
                    navController.navigate("detalle/$itemId")
                },
                onFavoritesClick = {
                    navController.navigate("favoritos")
                },
                onProfileClick = {
                    navController.navigate("perfil")
                },
                onAdminClick = {
                    navController.navigate("admin")
                },
                onLogoutClick = {
                    container.authRepository.cerrarSesion()
                    navController.navigate("login") {
                        popUpTo("lista") { inclusive = true }
                    }
                }
            )
        }

        // Ruta de Detalle
        composable(
            route = "detalle/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0
            val viewModel: DetalleViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return DetalleViewModel(itemId, container.itemRepository, container.notificador) as T
                    }
                }
            )
            val state by viewModel.uiState.collectAsState()

            when (val currentState = state) {
                is UiState.Cargando -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is UiState.Exito -> {
                    DetalleScreen(
                        item = currentState.datos,
                        isFavorite = currentState.datos.esFavorito,
                        onToggleFavorite = { viewModel.alternarFavorito() },
                        onBackClick = { navController.popBackStack() }
                    )
                }
                is UiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = currentState.mensaje)
                    }
                }
            }
        }

        // Ruta de Favoritos
        composable("favoritos") {
            val viewModel: FavoritosViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return FavoritosViewModel(container.itemRepository) as T
                    }
                }
            )
            FavoritosScreen(
                viewModel = viewModel,
                onHomeClick = {
                    navController.navigate("lista") {
                        popUpTo("lista") { inclusive = true }
                    }
                },
                onProfileClick = {
                    navController.navigate("perfil")
                }
            )
        }

        // Ruta de Perfil
        composable("perfil") {
            PerfilScreen(
                userRole = userRole,
                isDarkTheme = isDarkTheme,
                onToggleDarkTheme = onToggleDarkTheme,
                onBackClick = { navController.popBackStack() },
                onLogoutClick = {
                    container.authRepository.cerrarSesion()
                    navController.navigate("login") {
                        popUpTo("perfil") { inclusive = true }
                    }
                }
            )
        }

        // Ruta de Panel Admin
        composable("admin") {
            if (userRole == Rol.ADMIN) {
                val viewModel: AdminViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return AdminViewModel(container.itemRepository, container.notificador) as T
                        }
                    }
                )
                val totalGuardados by viewModel.totalGuardados.collectAsState()
                val totalFavoritos by viewModel.totalFavoritos.collectAsState()

                AdminScreen(
                    totalGuardados = totalGuardados,
                    totalFavoritos = totalFavoritos,
                    onSincronizar = { viewModel.sincronizar() },
                    onLimpiarCache = { viewModel.limpiarCache() },
                    onAgregarProducto = { title, price, category, desc, img ->
                        viewModel.agregarProducto(title, price, category, desc, img)
                    },
                    onVerCatalogo = {
                        navController.navigate("lista")
                    },
                    onFavoritosClick = {
                        navController.navigate("favoritos")
                    },
                    onPerfilClick = {
                        navController.navigate("perfil")
                    },
                    onLogoutClick = {
                        container.authRepository.cerrarSesion()
                        navController.navigate("login") {
                            popUpTo("admin") { inclusive = true }
                        }
                    }
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate("lista") {
                        popUpTo("admin") { inclusive = true }
                    }
                }
            }
        }
    }
}
