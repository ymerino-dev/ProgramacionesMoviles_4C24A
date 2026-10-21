package com.tecsup.cafecampus.data.local

import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.local.entity.Usuario

object DatosIniciales {
    suspend fun sembrar(db: CafeDatabase) {
        val daoUsuario = db.usuarioDao()
        val daoProducto = db.productoDao()

        // Si no hay usuarios, significa que la app está recién instalada
        if (daoUsuario.contar() == 0) {
            // Insertamos los 2 usuarios del laboratorio (clave 1234 y admin)
            daoUsuario.insertar(Usuario(nombre = "Juan", correo = "juan@campus.test", clave = "1234", rol = "CLIENTE"))
            daoUsuario.insertar(Usuario(nombre = "Admin", correo = "admin@cafe.test", clave = "admin", rol = "ADMIN"))

            // Insertamos los 6 productos iniciales con los íconos permitidos
            daoProducto.insertar(Producto(nombre = "Café americano", descripcion = "Tradicional", categoria = "Bebidas", precio = 4.00, icono = "cafe"))
            daoProducto.insertar(Producto(nombre = "Sándwich de pollo", descripcion = "Clásico", categoria = "Desayunos", precio = 9.00, icono = "pan"))
            daoProducto.insertar(Producto(nombre = "Menú del día", descripcion = "Almuerzo", categoria = "Almuerzos", precio = 12.00, icono = "plato"))
            daoProducto.insertar(Producto(nombre = "Jugo de naranja", descripcion = "Fresco", categoria = "Bebidas", precio = 5.00, icono = "jugo"))
            daoProducto.insertar(Producto(nombre = "Empanada", descripcion = "De carne", categoria = "Snacks", precio = 4.50, icono = "dulce"))
            daoProducto.insertar(Producto(nombre = "Chicha morada", descripcion = "Helada", categoria = "Bebidas", precio = 3.50, icono = "botella"))
        }
    }
}