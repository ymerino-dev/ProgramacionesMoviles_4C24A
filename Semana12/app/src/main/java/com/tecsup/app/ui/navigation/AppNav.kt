package com.tecsup.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.app.AppContainer
import com.tecsup.app.ui.lista.ListaScreen
import com.tecsup.app.ui.lista.ListaViewModel
import com.tecsup.app.ui.login.LoginScreen
import com.tecsup.app.ui.login.LoginViewModel

@Composable
fun AppNav(container: AppContainer) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {

        // Ruta de Inicio de Sesión
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
                onLoginSuccess = {
                    navController.navigate("lista") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // Ruta del Catálogo de Productos
        composable("lista") {
            val viewModel: ListaViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return ListaViewModel(container.itemRepository) as T
                    }
                }
            )
            ListaScreen(viewModel = viewModel)
        }

        composable("detalle/{itemId}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")?.toIntOrNull() ?: 0
            val viewModel: DetalleViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return DetalleViewModel(itemId, container.itemRepository) as T
                    }
                }
            )
            val state by viewModel.uiState.collectAsState()

            when (val currentState = state) {
                is UiState.Cargando -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is UiState.Exito -> {
                    DetalleScreen(
                        item = currentState.datos,
                        isFavorite = false,
                        onToggleFavorite = { viewModel.alternarFavorito() },
                        onBackClick = { navController.popBackStack() }
                    )
                }
                is UiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                        Text(text = currentState.mensaje)
                    }
                }
            }
        }
    }
}