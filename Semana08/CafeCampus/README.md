# ☕ CafeCampus - Gestión de Cafetería Universitaria

Aplicación móvil nativa en **Kotlin** y **Jetpack Compose** para la gestión de pedidos de cafetería universitaria con soporte para dos perfiles (**Estudiante** y **Administrador**), persistencia relacional en **Room Database** y sesión asíncrona con **Preferences DataStore**.

## 📋 Datos Académicos
* **Curso:** Programaciones Móviles
* **Docente:** Juan Jose León Suiyon
* **Sección:** C24-A
* **Estudiante:** Ana Yanira Merino Ramos
* **Ramas del Proyecto:** `main` / `sinia` (Fase 1 - Base) y `mejora-ia` / `conia` (Fase 2 - Mejoras con IA)

---

## 📂 Estructura de Paquetes (Arquitectura MVVM)

```text
com.tecsup.cafecampus
├── data
│   ├── local
│   │   ├── dao/          # Interfaces @Dao con consultas SQLite (Flow y suspend)
│   │   ├── entity/       # Entidades de tablas (@Entity: Usuario, Producto, Pedido, Detalle)
│   │   └── relation/     # Relaciones de datos (PedidoConDetalle)
│   ├── prefs/            # Gestión de sesión y preferencias con Preferences DataStore
│   └── repository/       # Interfaces de repositorio e implementaciones concretas (Room)
├── model/                # Modelos de dominio, estados de UI y clases auxiliares
├── ui
│   ├── admin/            # Panel con gráfico real por hora, Gestión de pedidos y Reporte del día
│   ├── cliente/          # Catálogo con búsqueda, Carrito, Seguimiento e Historial (Repetir pedido)
│   ├── navigation/       # Grafo de navegación (NavHost) con transiciones animadas entre pantallas
│   └── theme/            # Colores, tipografía y configuración de Material Design 3
└── viewmodel/            # ViewModels con StateFlow reactivo por perfil y fábrica de inyección
```

---

## 📖 Descripción del Programa
**CafeCampus** es una aplicación móvil nativa desarrollada para optimizar y digitalizar el proceso de pedidos y gestión en una cafetería estudiantil. El sistema está estructurado bajo una arquitectura limpia **MVVM** (Model-View-ViewModel), utilizando componentes modernos de **Jetpack Compose** para la interfaz gráfica, base de datos local **Room** para la persistencia transaccional y **Preferences DataStore** para el control de estados gerenciales y preferencias del usuario. La aplicación opera bajo dos roles independientes:
- **Módulo Estudiante:** Permite la exploración de un catálogo en tiempo real con filtrado interactivo por búsqueda y categorías, personalización de comandas mediante notas especiales en el carrito, opción de repetir pedidos anteriores desde el historial, selección de horarios de recojo y un seguimiento visual interactivo del estado de los pedidos.
- **Módulo Administrador:** Ofrece un panel de control gerencial con indicadores de ventas, contadores de comandas, reportes en tiempo real del producto más vendido, ventas por método de pago, gráficos estadísticos reales de pedidos por hora y un sistema completo de administración de platos y disponibilidad.

---

## 🤖 Registro de Documentación IA
La documentación completa de los prompts utilizados para cada una de las mejoras de la Fase 2 se encuentra disponible en el archivo [`PROMPTS.md`](./PROMPTS.md).

---

## 🛠️ Tecnologías y Stack Utilizado
- **Lenguaje de Programación:** Kotlin
- **Interfaz de Usuario:** Jetpack Compose & Material Design 3
- **Navegación:** Jetpack Navigation Compose (con transiciones animadas)
- **Base de Datos:** Room Database (SQLite)
- **Persistencia de Sesión/Preferencias:** Jetpack Preferences DataStore
- **Control de Versiones:** Git & GitHub
