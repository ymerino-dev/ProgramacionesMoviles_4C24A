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

@Composable
fun AppNav(container: AppContainer) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "lista") {
        composable("lista") {
            // Creamos el ViewModel usando un Factory para inyectar el repositorio correctamente
            val viewModel: ListaViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return ListaViewModel(container.itemRepository) as T
                    }
                }
            )
            ListaScreen(viewModel = viewModel)
        }
    }
}