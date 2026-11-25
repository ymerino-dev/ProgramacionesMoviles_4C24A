# Registro de Prompts y Criterio Técnico - MuroCampus

Este documento registra los prompts utilizados durante la implementación de las tres mejoras principales en la aplicación **MuroCampus**, detallando el análisis del soporte proporcionado por la IA, las decisiones de arquitectura tomadas y la justificación técnica de cada cambio.

---

## 🏗️ Principios Arquitectónicos Aplicados

Para mantener una arquitectura limpia y desacoplada (Clean Architecture / MVVM + Repository):
- **Capa de Red (`data.remote`)**: Maneja OkHttp, Retrofit, `PostApi` y las excepciones HTTP (`HttpException`, `Response<T>`).
- **Capa del Repositorio (`data`)**: Transforma respuestas de red y excepciones en tipos neutros de Kotlin (`Result<T>`). No propaga `HttpException` fuera de esta capa.
- **Capa de Presentación (`viewmodel`)**: `PostsViewModel` gestiona únicamente estados de UI (`UiState<T>`) y flujos reactivos (`StateFlow`). **Prohibido importar Retrofit, PostApi, HttpException o Context**.
- **Capa de UI (`ui`)**: Composables reactivos con Jetpack Compose y Material 3. Consumen únicamente `StateFlow` y **no realizan llamadas de red ni manejan excepciones HTTP directamente**.

---

## 📊 Registro de Prompts y Decisiones Técnicas

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
| :--- | :--- | :--- | :--- |
| **Mejora 1: OkHttp Interceptor**<br>`Actúa como un arquitecto Android senior. En com.merino.murocampus.data.remote.RetrofitClient, agrega un Interceptor personalizado de OkHttp antes del HttpLoggingInterceptor que añada automáticamente el encabezado "Accept: application/json" a todas las peticiones HTTP salientes usando chain.request().newBuilder().header("Accept", "application/json").build(). Mantén intacta la configuración de Json { ignoreUnknownKeys = true } y la URL base HTTPS.` | Propuso incluir la lambda `.addInterceptor { chain -> ... }` en el builder de `OkHttpClient` ubicado antes de `.addInterceptor(logging)`. | Se aceptó la implementación tal cual fue propuesta. | Garantiza la adición de la cabecera `Accept: application/json` en todas las peticiones salientes antes de ser registradas por `HttpLoggingInterceptor`, manteniendo la trazabilidad correcta en logs sin alterar la serialización de Kotlinx Serialization. |
| **Mejora 2: Eliminación con @DELETE**<br>`Actúa como un experto en Android con Jetpack Compose, Material 3 y arquitectura MVVM + Repository. Implementa la funcionalidad de Eliminar publicación respetando estrictamente las reglas de arquitectura (prohibido importar Retrofit, PostApi, HttpException o Context dentro de PostsViewModel o en los Composables):`<br>`1. En PostApi.kt, agrega @DELETE("posts/{id}") suspend fun eliminar(@Path("id") id: Int): Response<Unit>.`<br>`2. En PostRepository.kt y PostRepositoryRemote, agrega suspend fun eliminar(id: Int): Result<Unit>. Verifica response.isSuccessful; si falla, lanza HttpException(response) dentro del bloque llamar { ... } para devolver Result.failure.`<br>`3. En PostViewModel.kt, agrega StateFlow<UiState<Unit>?> llamado eliminacion, eliminarPost(id: Int) y limpiarEstadoEliminacion(). Si tiene éxito, remueve el post de _lista.`<br>`4. En PantallaDetalle.kt, agrega un botón Material 3 con confirmación AlertDialog y regresa a PantallaLista.` | Generó la interfaz `@DELETE` devolviendo `Response<Unit>`, el repositorio capturando `HttpException(response)` dentro del wrapper `llamar { ... }`, el `PostsViewModel` administrando `_eliminacion` y `PantallaDetalle` invocando la acción con un `AlertDialog`. | Se refinó la limpieza de estado al navegar hacia atrás (`BackHandler`) y la actualización de `postsOriginales` en memoria dentro del ViewModel al eliminar un post. | Previene fuga de excepciones HTTP hacia el ViewModel o Composables, asegurando que los errores se mapeen a `UiState.Error` mediante `mensajeDe(e)` y que la UI se actualice de forma instantánea sin requerir descargas adicionales. |
| **Mejora 3: Búsqueda Local por Título**<br>`Actúa como un experto en Jetpack Compose y Material 3. Implementa un buscador local por título en MuroCampus sin realizar llamadas adicionales a la API:`<br>`1. En PostViewModel.kt, respalda en memoria la lista descargada en una propiedad privada (postsOriginales: List<Post>) cada vez que cargar() tenga éxito. Expón StateFlow<String> llamado busqueda y una función buscar(texto: String) que actualice _busqueda y filtre postsOriginales por title (ignoreCase = true), emitiendo el nuevo UiState.Exito en _lista. Reinicia _busqueda a "" cuando se cambie de usuario en filtrar(userId).`<br>`2. En PantallaLista.kt, agrega un OutlinedTextField de Material 3 encima de la LazyRow de chips con placeholder "Buscar por título...", ícono Search al inicio y IconButton con Clear al final.` | Diseñó el estado `busqueda: StateFlow<String>`, el respaldo `postsOriginales`, la función `buscar(texto)` en `PostsViewModel` y el campo de texto `OutlinedTextField` con soporte de limpieza rápida en `PantallaLista.kt`. | Se aseguró que al filtrar por usuario en `filtrar(userId)` se resetee `_busqueda` a `""` y que la eliminación borre el post tanto de `postsOriginales` como de la vista filtrada actual. | Evita peticiones innecesarias al backend (ahorro de ancho de banda y latencia), manteniendo la búsqueda fluida en tiempo real y la consistencia del estado del filtro local al cambiar de vista o eliminar elementos. |

---

## 🔒 Validaciones Arquitectónicas Cumplidas

1. **Aislamiento de Retrofit y HttpException**:
   - `RetrofitClient`, `PostApi` y `HttpException` permanecen encapsulados en el paquete `data`.
   - `PostsViewModel` trabaja exclusivamente con la abstracción `PostRepository` y devuelve `Result<T>` mapeado a `UiState<T>`.

2. **Cero Importaciones de Context en ViewModel o UI**:
   - `PostsViewModel` y Composables no hacen referencia a `android.content.Context`.

3. **Sin Consumo Innecesario de Red**:
   - La búsqueda opera sobre `postsOriginales` en memoria RAM.
