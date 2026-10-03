# 🛒 Mi Bodega / TECSUP Store - Lab 06 & Fase 2 (IA)

Aplicación móvil desarrollada en **Android Studio** utilizando **Jetpack Compose**, orientada a la gestión de un carrito de compras interactivo con 7 pantallas, navegación avanzada con paso de parámetros y `popUpTo`, y filtrado combinado en tiempo real.

---

## 👨‍💻 Datos del Estudiante
* **Estudiante:** Ana Yanira Merino Ramos
* **Correo institucional:** ana.merino@tecsup.edu.pe
* **Curso:** Programación en Móviles (4to Ciclo)
* **Institución:** TECSUP

---

## 🔐 Credenciales de Acceso (Login)
Para ingresar a la aplicación en la **PantallaLogin.kt**, puedes utilizar las siguientes credenciales de prueba:
* **Usuario / Correo:** `ana.merino@tecsup.edu.pe` *(o cualquier texto no vacío)*
* **Contraseña:** `tecsup2026` *(o cualquier texto no vacío)*

---

## 📱 Alcance de las 7 Pantallas e Implementación
1. **PantallaLogin.kt:** Acceso inicial con validación y botones para registro o entrada directa.
2. **PantallaCrearCuenta.kt:** Formulario completo con datos del cliente (Nombre, teléfono, dirección, referencia).
3. **PantallaInicio.kt:** Vista principal con barra superior, **buscador en tiempo real con filtro combinado**, `LazyRow` de categorías y `LazyColumn` de productos destacados.
4. **PantallaDetalleProducto.kt:** Vista ampliada del producto seleccionado (recibiendo `productoId` por ruta), control de cantidad y botón "Agregar al carrito".
5. **PantallaCarrito.kt:** Listado de productos agregados, control de cantidades por ítem, subtotal, costo de envío y cálculo reactivo del total general.
6. **PantallaDatosEntrega.kt:** Formulario de envío y selección de método de pago (Efectivo, Yape, Plin) con confirmación y limpieza de pila (`popUpTo`).
7. **PantallaConfirmacion.kt:** Pantalla de éxito con número de pedido simulado (`#1024`), resumen y botón para volver al inicio.

---

## 🚀 Requerimiento Especial Fase 2 (Filtrado Combinado en Tiempo Real)
En `PantallaInicio.kt`, se implementó un sistema de **filtrado simultáneo**:
* El campo de búsqueda filtra dinámicamente por nombre o descripción mientras el usuario escribe.
* Se combina de forma simultánea con el filtro de categorías de la `LazyRow` (ej. seleccionar "Bebidas" y buscar "1.5L"), mostrando únicamente los productos que cumplen ambas condiciones a la vez sin que un filtro anule al otro.

---

## 🛠️ Tecnologías y Componentes Utilizados
* **Lenguaje:** Kotlin
* **UI Toolkit:** Jetpack Compose (Material Design 3)
* **Navegación:** Jetpack Navigation Compose (NavHost, NavType, arguments, popUpTo)
* **Arquitectura de UI:** Componentes reactivos (`remember`, `mutableStateListOf`, `LazyColumn`, `LazyRow`).
