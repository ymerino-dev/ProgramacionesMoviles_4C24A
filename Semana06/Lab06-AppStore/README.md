cat << 'EOF' > README.md
# 🛒 Mi Bodega / TECSUP Store - Lab 06

Aplicación móvil desarrollada en **Android Studio** utilizando **Jetpack Compose**, orientada a la gestión de un carrito de compras interactivo con menús contextuales y navegación estructurada por cajón lateral (`NavigationDrawer`).

---

## 👨‍💻 Datos del Estudiante
* **Estudiante:** Ana Yanira Merino Ramos
* **Correo institucional:** ana.merino@tecsup.edu.pe
* **Curso:** Programación en Móviles (4to Ciclo)
* **Institución:** TECSUP

---

## 🚀 Características Principales (Fase 1 - Sin IA)

1. **Gestión de Productos:**
    * Formulario interactivo para agregar productos con nombre, precio y cantidad.
    * Cálculo dinámico de subtotal, IGV (18%) y total general.
    * Estado vacío interactivo cuando el carrito no tiene elementos.
2. **Menú Contextual por Tarjeta (`DropdownMenu`):**
    * Botón de 3 puntos en cada tarjeta de producto.
    * Opciones personalizadas con iconos y divisores (*Favoritos*, *Compartir*, *Reportar*).
3. **Navegación Lateral (`NavigationDrawer`):**
    * Cajón lateral deslizante (`ModalDrawerSheet`) con encabezado personalizado del usuario (Iniciales, Nombre y Correo).
    * Destinos funcionales: *Inicio*, *Mis pedidos*, *Favoritos*, *Perfil* y *Cerrar sesión*.
    * Indicador visual de elemento activo.
4. **Vistas Especializadas:**
    * **Pantalla de Perfil:** Vista dedicada con información institucional y datos reales del estudiante.
    * **Pantalla de Favoritos:** Listado reactivo de elementos guardados desde el menú contextual.

---

## 🛠️ Tecnologías y Componentes Utilizados
* **Lenguaje:** Kotlin
* **UI Toolkit:** Jetpack Compose (Material Design 3)
* **Control de Versiones:** Git y GitHub
* **Arquitectura de UI:** Componentes reactivos (`remember`, `mutableStateListOf`, `LazyColumn`).

---

