# Laboratorio 11-12: Compose + MVVM + Retrofit + Room + FirebaseAuth

**Aplicación Android - Tema: Productos (ShopifyApp)**  
**Repositorio GitHub:** [ymerino-dev/ProgramacionesMoviles_4C24A](https://github.com/ymerino-dev/ProgramacionesMoviles_4C24A.git)  
**Rama:** `conia`  
**Usuario:** `ymerino-dev`  

---

## 📱 Descripción del Proyecto

Aplicación móvil Android desarrollada en **Kotlin** aplicando el patrón de arquitectura **MVVM** con **Jetpack Compose** y **Material 3**. La aplicación incluye autenticación con dos roles (**Usuario** y **Administrador**), datos remotos desde una API REST mediante **Retrofit**, persistencia local y almacenamiento de favoritos offline con **Room**, gestión de roles con **Firebase Authentication** y **Firestore**, y **Notificaciones Locales**.

---

## 🛠️ Tecnologías y Arquitectura

- **UI / Layout:** Jetpack Compose + Material 3 (Diseño minimalista estilo ShopifyApp en verde corporativo `#263B36`).
- **Arquitectura:** MVVM (Model-View-ViewModel) + StateFlow + UiState.
- **Red:** Retrofit 2 + Kotlinx Serialization Json + OkHttpClient (con timeouts de 10s).
- **Base de Datos Local:** Room Database 2.6.1 + KSP.
- **Autenticación y Nube:** Firebase Auth + Firestore (`usuarios/{uid}` con el campo `rol`).
- **Notificaciones:** NotificationCompat + NotificationChannel (`POST_NOTIFICATIONS`).
- **Carga de Imágenes:** Coil Compose (`AsyncImage`).
- **Pruebas Unitarias:** JUnit 4 + Kotlinx Coroutines Test.

---

## 🔑 Usuarios de Prueba

| Rol | Correo Electrónico | Descripción de Acceso |
| :--- | :--- | :--- |
| **Administrador** | `admin@tecsup.edu.pe` | Acceso completo al Catálogo, Favoritos, Perfil y **Panel Administrador** |
| **Usuario Normal** | `usuario@tecsup.edu.pe` | Acceso al Catálogo, Detalle, Favoritos y Perfil (sin acceso al panel) |

---

## 🤖 Registro de Prompts - Fase 2 con IA

### Consulta 1
- **Fecha:** 2026-11-11
- **Herramienta:** Android Studio AI / Claude / Gemini
- **Prompt:** "¿Cómo estructurar las pruebas unitarias para un ViewModel con Kotlin Coroutines y Flow usando un repositorio falso (FakeItemRepository) para cumplir con el requisito R12?"
- **Qué respondió (resumen):** Sugirió utilizar `StandardTestDispatcher` y `TestScope`, junto con `runTest`, para controlar la ejecución de corrutinas en los tests unitarios. Indicó crear una implementación de `ItemRepository` en memoria que devuelva `MutableStateFlow` para simular la emisión de items.
- **Qué usamos / qué cambiamos y por qué:** Usamos el enfoque de `FakeItemRepository` implementando `ItemRepository` con `MutableStateFlow` para emitir listas de prueba y registrar llamadas a `refrescar()` o `alternarFavorito()`. Esto permitió verificar el comportamiento reactivo de `ListaViewModel` sin depender de Room ni Retrofit reales.
- **Qué aprendimos:** Aprendimos a configurar `Dispatchers.setMain` y `Dispatchers.resetMain` para pruebas de ViewModels que utilizan `viewModelScope`.

### Consulta 2
- **Fecha:** 2026-11-11
- **Herramienta:** Android Studio AI / Claude / Gemini
- **Prompt:** "¿Cómo estructurar el archivo PROMPTS.md y el plan de commits para la Fase 2 (conia) del laboratorio integrador siguiendo las directrices de la rúbrica?"
- **Qué respondió (resumen):** Proporcionó la estructura exacta basada en la plantilla del documento de laboratorio, exigiendo al menos 3 consultas documentadas y commits claros en español con prefijos convencionales (`test:`, `docs:`, `refactor:`).
- **Qué usamos / qué cambiamos y por qué:** Adoptamos la plantilla formal con los apartados solicitados y establecimos los commits detallados para documentar la IA y las pruebas unitarias.
- **Qué aprendimos:** La importancia de registrar detalladamente el prompt, la respuesta, las decisiones de diseño y el aprendizaje obtenido para cumplir con los estándares académicos y de ingeniería.

### Consulta 3
- **Fecha:** 2026-11-11
- **Herramienta:** Android Studio AI / Claude / Gemini
- **Prompt:** "¿Cómo integrar y sincronizar la rama conia con los cambios del repositorio principal y asegurar que compila correctamente?"
- **Qué respondió (resumen):** Recomendar sincronizar el código base de `sinia` en `conia`, agregar las dependencias de `kotlinx-coroutines-test`, implementar los tests unitarios y verificar la compilación mediante gradle.
- **Qué usamos / qué cambiamos y por qué:** Sincronizamos `sinia` en `conia`, añadimos la librería de pruebas y validamos el funcionamiento correcto de la app y las pruebas unitarias.
- **Qué aprendimos:** Mantener la consistencia entre las ramas `sinia` y `conia` facilita la transición de la Fase 1 a la Fase 2 con IA.

### Consulta 4
- **Fecha:** 2026-11-11
- **Herramienta:** Android Studio AI / Claude / Gemini
- **Prompt:** "¿Cómo rediseñar la UI para adoptar la identidad visual e-commerce ShopifyApp con paleta verde, tarjetas en 2 columnas, modo oscuro y notificaciones en tiempo real al agregar favoritos?"
- **Qué respondió (resumen):** Sugirió definir una paleta de colores personalizada en `Color.kt` y `Theme.kt`, implementar un `LazyVerticalGrid` en `ListaScreen.kt`, un `Switch` de Modo Oscuro en `PerfilScreen.kt`, e integrar `Notificador` en `ListaViewModel` y `DetalleViewModel`.
- **Qué usamos / qué cambiamos y por qué:** Aplicamos el esquema verde corporativo (`#263B36`), grilla de productos de 2 columnas, alternancia de favoritos con notificaciones locales inmediatas y navegación en la barra inferior para todos los roles.
- **Qué aprendimos:** A diseñar componentes declarativos adaptables y reactivos con Jetpack Compose y Material 3.

---

## 📋 Casos de Aceptación Cumplidos

- **PA-01:** Ingresar con el usuario normal entra al catálogo sin acceso al panel admin.
- **PA-02:** Ingresar con el usuario admin otorga acceso al panel administrador.
- **PA-03:** Credenciales incorrectas muestran mensaje de error claro.
- **PA-04:** Validación de campos vacíos sin llamar a Firebase.
- **PA-05:** Carga inicial desde API a Room con notificación al finalizar.
- **PA-06:** Detalle de producto muestra datos completos con botón de favoritos.
- **PA-07:** Los favoritos marcados persisten en Room tras cerrar la app.
- **PA-08:** Modo sin conexión muestra productos guardados con aviso.
- **PA-09:** Modo sin conexión sin datos muestra pantalla de error con botón Reintentar.
- **PA-10:** Los favoritos se conservan intactos al sincronizar los productos desde la API.
- **PA-11:** Limpiar caché borra productos en Room y actualizar totales en 0. Al sincronizar vuelven los datos.
- **PA-12:** Cerrar sesión elimina el estado y regresa al login pidiendo credenciales.
- **PA-13:** Los giros de pantalla mantienen el estado sin volver a descargar.
- **PA-14:** La app opera con normalidad si se niegan los permisos de notificación.
