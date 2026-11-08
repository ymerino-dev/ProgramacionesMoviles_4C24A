package com.tecsup.app.ui.navigation

import androidx.compose.runtime.Composable
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
            // Creamos el ViewModel inyectándole el repositorio de nuestro AppContainer
            val viewModel: ListaViewModel = ListaViewModel(container.itemRepository)
            ListaScreen(viewModel = viewModel)
        }
    }
}