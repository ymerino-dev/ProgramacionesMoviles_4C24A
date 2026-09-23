# Clínica Salud+

## 🏥 Descripción Profesional
**Clínica Salud+** es una aplicación móvil moderna desarrollada para la gestión eficiente de citas médicas y consulta de especialistas de salud. Diseñada bajo rigurosos estándares de experiencia de usuario (UX/UI), la aplicación ofrece una interfaz intuitiva, elegante y fluida con estética Material Design 3.

## 🛠️ Tecnologías y Librerías
- **Lenguaje:** Kotlin 100% nativo.
- **Framework UI:** Jetpack Compose (Declarative UI).
- **Sistema de Diseño:** Material Design 3 (M3) con paleta de colores personalizada en tonos morados profesionales (`#512DA8`).
- **Navegación:** Jetpack Navigation Compose con paso seguro de argumentos entre pantallas (`perfil/{medico}`, `agendar/{medico}`, `confirmacion/{medico}/{fecha}/{hora}`).

## 🏗️ Arquitectura y Gestión de Estado (Sin ViewModels / Sin Room)
Siguiendo estrictamente las restricciones académicas del proyecto:
- **Estado Dinámico Local y Global:** Se utiliza intensivamente `remember`, `mutableStateOf` y `mutableStateListOf` en contenedores de estado reactivos. Las citas confirmadas se propagan y actualizan en tiempo real mediante listas mutables compartidas entre `MainScreen` y `MisCitasScreen`.
- **Navegación Secuencial Parametrizada:** La navegación entre vistas fluye de forma secuencial, pasando los parámetros del médico seleccionado, fecha y hora directamente a través de las rutas de Jetpack Navigation sin desacoplar en arquitecturas MVVM o persistencia local con Room.

## 👩‍💻 Autora
- **Ana Yanira Merino Ramos**
- **Curso:** Programación Móvil / 4C24A
