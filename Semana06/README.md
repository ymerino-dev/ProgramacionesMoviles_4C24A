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

## 🤔 Preguntas de Reflexión (Fase 1)

1. **¿Qué ventajas ofrece `ModalNavigationDrawer` frente a una navegación tradicional por pestañas o pantallas independientes en Jetpack Compose?**
    * Permite centralizar la navegación global de la aplicación de manera fluida y limpia, optimizando el espacio visual en pantallas móviles y manteniendo la consistencia de la identidad de usuario en todo el ciclo de vida de la app.

2. **¿Por qué es fundamental el uso de estados reactivos (`mutableStateListOf`, `remember`) al sincronizar el menú contextual (`DropdownMenu`) con la pantalla de Favoritos?**
    * Garantiza que la interfaz se recomponga de forma automática e inmediata cada vez que el usuario agregue o elimine un elemento, manteniendo la fuente de la verdad sincronizada en tiempo real sin necesidad de recargar la actividad.

3. **¿Cómo influye una correcta estructuración de ramas y commits en Git para el desarrollo de proyectos móviles colaborativos?**
    * Facilita el rastreo de errores, permite aislar nuevas características (como la transición a la Fase 2 con IA) y asegura un historial de cambios limpio y profesional alineado con las buenas prácticas de la industria.


