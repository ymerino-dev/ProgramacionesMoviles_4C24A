package com.merinoana.clinicasalud.screens

sealed class Screen(val route: String) {
    object Inicio : Screen("inicio")
    object MisCitas : Screen("miscitas")
    object Historial : Screen("historial")

    object MiPerfil : Screen("miperfil")

    // Rutas con paso de parámetros requeridos por la rúbrica
    object Perfil : Screen("perfil/{medico}") {
        fun createRoute(medico: String) = "perfil/$medico"
    }
    object Agendar : Screen("agendar/{medico}") {
        fun createRoute(medico: String) = "agendar/$medico"
    }
    object Confirmacion : Screen("confirmacion/{medico}/{fecha}/{hora}") {
        fun createRoute(medico: String, fecha: String, hora: String) = "confirmacion/$medico/$fecha/$hora"
    }
}