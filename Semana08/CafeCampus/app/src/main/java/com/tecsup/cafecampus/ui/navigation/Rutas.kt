package com.tecsup.cafecampus.ui.navigation

sealed class Rutas(val ruta: String) {
    object Splash : Rutas("splash")
    object Login : Rutas("login")

    // Rutas del Cliente
    object ClienteMenu : Rutas("cliente_menu")
    object ClienteDetalleProducto : Rutas("cliente_detalle_producto/{id}") {
        fun crearRuta(id: Int) = "cliente_detalle_producto/$id"
    }
    object ClienteConfirmar : Rutas("cliente_confirmar")
    object ClienteMisPedidos : Rutas("cliente_mis_pedidos")
    object ClienteSeguimiento : Rutas("cliente_seguimiento/{id}") {
        fun crearRuta(id: Int) = "cliente_seguimiento/$id"
    }

    // Rutas del Administrador
    object AdminPanel : Rutas("admin_panel")
    object AdminPedidos : Rutas("admin_pedidos")
    object AdminMenu : Rutas("admin_menu")
    object AdminFormulario : Rutas("admin_formulario?id={id}") {
        fun crearRuta(id: Int = 0) = "admin_formulario?id=$id"
    }
    object AdminAjustes : Rutas("admin_ajustes")
}