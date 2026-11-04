package com.tecsup.authfirebaseapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object Destinations {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home" //[cite: 13]
}

@Composable
fun AuthApp() {
    val navController = rememberNavController() //[cite: 13]
    AuthNavGraph(navController)
}

@Composable
fun AuthNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Destinations.LOGIN //[cite: 13]
    ) {
        composable(Destinations.LOGIN) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Destinations.REGISTER)
                },
                onLoginSuccess = {
                    navController.navigate(Destinations.HOME) {
                        popUpTo(Destinations.LOGIN) { inclusive = true } //[cite: 13]
                    }
                }
            )
        }
        composable(Destinations.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Destinations.HOME) {
                        popUpTo(Destinations.LOGIN) { inclusive = true } //[cite: 13]
                    }
                },
                onNavigateBack = {
                    navController.popBackStack() //[cite: 13]
                }
            )
        }
        composable(Destinations.HOME) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Destinations.LOGIN) {
                        popUpTo(Destinations.HOME) { inclusive = true } //[cite: 13]
                    }
                }
            )
        }
    }
}