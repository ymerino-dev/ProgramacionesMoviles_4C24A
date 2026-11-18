# 🤖 Registro de Ingeniería de Prompts — CentroTareas (`conia`)

![Kotlin](https://img.shields.io/badge/Kotlin-2.0+-7F52FF?style=flat&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?style=flat&logo=jetpackcompose&logoColor=white)
![Architecture](https://img.shields.io/badge/Arquitectura-MVVM%20%2B%20Repository-2E7D32?style=flat)
![AI Assisted](https://img.shields.io/badge/IA-Gemini%20en%20Android%20Studio-8E75B2?style=flat)

Este documento registra la metodología de desarrollo asistido por Inteligencia Artificial utilizada durante la **Fase 2** del Laboratorio N° 14 para evolucionar **CentroTareas** (`com.merino.centrotareas`), garantizando la integridad de la arquitectura **MVVM + Repository**, el flujo unidireccional de datos (UDF) y el manejo seguro de corrutinas.

* **Curso:** Programación en Móviles — Laboratorio N° 14
* **Docente:** Juan León S.
* **Autora:** Ana Yanira Merino Ramos
* **Rama de desarrollo:** `conia`

---

## 🛡️ Reglas de Auditoría Aplicadas en cada Iteración

Antes de aceptar cualquier bloque de código generado por Gemini, se verificaron estrictamente las siguientes restricciones técnicas:

1. **Cero bloqueos:** Ausencia total de `GlobalScope`, `runBlocking` y `Thread.sleep` (verificado con `git grep`).
2. **Cancelación estructurada:** Propagación obligatoria de `CancellationException` y `TimeoutCancellationException` en todos los bloques `try/catch`.
3. **Aislamiento de capas:** El `ViewModel` nunca recibe referencias de `Context`; la persistencia con `DataStore` se inyecta exclusivamente en `TareasRepositoryLocal`.
4. **Control de recomposición:** Emisión optimizada de estados en `StateFlow` para evitar saturación en Jetpack Compose.

---

## 📊 Matriz de Prompts y Decisiones Técnicas

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
| :--- | :--- | :--- | :--- |
| **1. UI Modular + Progreso en Repository**<br>«Rediseña la pantalla por pestañas/chips y mueve la tarea simulada de 100 pasos al Repository exponiendo un `Flow<Float>` con `flowOn(Dispatchers.Default)`» | Propuso una barra de navegación superior con `FilterChip` para separar las vistas y creó `ejecutarTareaConProgreso(): Flow<Float>` usando `flow { ... }.flowOn(Dispatchers.Default)` recolectado en `viewModelScope`. | Descarté usar `launchIn(viewModelScope)` y mantuve la recolección dentro de `viewModelScope.launch` con un bloque `try/finally` explícito. | Conservar la referencia directa del `Job` permite cancelar el flujo inmediatamente al pulsar *Cancelar* y garantiza que el estado `corriendo` regrese siempre a `false`. |
| **2. Primos con Porcentaje en Vivo**<br>«Actualiza `PrimosUi.Calculando` para recibir el progreso `Float`, repórtalo desde `contarPrimos` en `Dispatchers.Default` y muéstralo con `LinearProgressIndicator`» | Sugirió añadir un callback `onProgreso: (Float) -> Unit` dentro del ciclo `for (n in 2..hasta)` junto con `ensureActive()` y dibujar una barra lineal en Compose. | Limité las actualizaciones del progreso a intervalos de 1 % (`val paso = (hasta / 100).coerceAtLeast(1)`) en lugar de emitir en cada iteración del bucle. | Emitir 500 000 actualizaciones seguidas de `StateFlow` degrada el tiempo de cómputo de la CPU y provoca recomposiciones innecesarias en la interfaz. |
| **3. Tabla Comparativa, Timeout y Reintentos**<br>«Agrupa los interruptores de prueba e implementa `ejecutarConReintentos` (3 intentos con espera creciente), `withTimeout` configurable a 2 s y una tabla acumulada de tiempos» | Generó la función genérica `ejecutarConReintentos`, envolvió las cargas en `withTimeout` y creó `RegistroTiempo` para renderizar una tabla comparativa en pantalla. | Añadí un filtro explícito `if (e is CancellationException) throw e` dentro de `ejecutarConReintentos` y agregué un botón *Limpiar tabla*. | Evita que el mecanismo de reintento capture por error una cancelación del usuario o un vencimiento de `withTimeout` antes de agotar los intentos. |
| **4. Historial Persistente con DataStore**<br>«Crea `HistorialDataStore` para guardar los últimos 5 resultados exitosos usando `JSONArray`, inyéctalo en `TareasRepositoryLocal` y muéstralo en la pestaña Historial» | Implementó `preferencesDataStore` con serialización mediante `org.json.JSONArray`, expuso un `Flow<List<String>>` desde el Repository y creó las tarjetas de historial en Compose. | Inyecté `applicationContext` en `TareasRepositoryLocal` desde `MainActivity` sin pasarlo al `ViewModel`, y configuré `.stateIn(..., SharingStarted.WhileSubscribed(5000), emptyList())`. | Mantiene el `ViewModel` libre de dependencias del framework de Android, evita fugas de memoria (*memory leaks*) y conserva los datos al rotar la pantalla. |

---

## 📝 Catálogo Detallado de Prompts Utilizados

### 1️⃣ Prompt 1: Reorganización Modular de UI + Progreso desde Repository (`Flow`)

> **Prompt ejecutado:**
> ```text
> Actúa como un Senior Android Developer experto en Kotlin, Jetpack Compose (Material 3) y arquitectura MVVM + Repository.
> 
> Sobre mi proyecto actual "CentroTareas" (paquete com.merino.centrotareas), realiza dos tareas integradas sin romper la arquitectura ni usar GlobalScope, runBlocking o Thread.sleep:
> 
> 1. REORGANIZACIÓN DE INTERFAZ Y DISTRIBUCIÓN (PantallaTareas.kt):
> - Rediseña la pantalla como un verdadero "Centro de Tareas" modular y ordenado.
> - Agrega un menú superior de selección con PrimaryTabRow (o FilterChip desplazables) con 4 categorías claras:
>   1) "Progreso" (Tarea simulada con Flow y botón Tócame)
>   2) "Primos" (Cálculo intensivo en CPU)
>   3) "Cargas" (Concurrencia, Timeout y Reintentos)
>   4) "Historial" (Resultados guardados)
> - Muestra en el cuerpo principal únicamente la tarjeta correspondiente a la sección seleccionada, con un encabezado descriptivo breve que explique qué prueba de concurrencia se está ejecutando en esa vista.
> 
> 2. PROGRESO DESDE EL REPOSITORY (TareasRepository.kt, TareasRepositoryLocal.kt y TareasViewModel.kt):
> - Mueve la tarea simulada de 100 pasos al Repository creando `fun ejecutarTareaConProgreso(): Flow<Float>` usando `flow { ... }` con `delay(40)` y `.flowOn(Dispatchers.Default)`.
> - En `TareasViewModel`, refactoriza `tareaLarga()` para recolectar ese `Flow` dentro de `viewModelScope.launch`, guardando el `Job` para cancelar y asegurando `_corriendo.value = false` en `finally`.
> ```
* **Commit asociado:** `feat(ui-flow): organiza interfaz por secciones y mueve progreso al Repository con Flow`

---

### 2️⃣ Prompt 2: Cálculo de Números Primos con Porcentaje en Vivo

> **Prompt ejecutado:**
> ```text
> Continuando con "CentroTareas" (com.merino.centrotareas) y manteniendo la distribución modular por secciones, implementa la mejora "Primos con progreso" en la pestaña "Primos":
> 
> 1. MODELO (Estados.kt):
> - Modifica `PrimosUi.Calculando` a `data class Calculando(val progreso: Float = 0f) : PrimosUi`.
> 
> 2. REPOSITORY (TareasRepository.kt y TareasRepositoryLocal.kt):
> - Actualiza `suspend fun contarPrimos(hasta: Int, onProgreso: (Float) -> Unit = {}): Int` ejecutado en `Dispatchers.Default`.
> - Dentro del ciclo `for (n in 2..hasta)`, conserva `ensureActive()` y reporta el avance con `onProgreso(n.toFloat() / hasta)` únicamente cada 1% (`val paso = (hasta / 100).coerceAtLeast(1)`) para no saturar la recomposición de la interfaz.
> 
> 3. VIEWMODEL Y VIEW (TareasViewModel.kt y PantallaTareas.kt):
> - En `TareasViewModel.contar(hasta)`, actualiza `_primos` con el porcentaje en tiempo real y asegúrate de relanzar `CancellationException` si el usuario cancela.
> - En la vista de la sección "Primos", coloca el campo numérico y los botones "Contar" / "Cancelar" arriba, y debajo muestra una `LinearProgressIndicator(progress = { estado.progreso })` junto al porcentaje exacto.
> ```
* **Commit asociado:** `feat(primos): muestra porcentaje de avance en tiempo real durante el calculo`

---

### 3️⃣ Prompt 3: Tabla Comparativa, `withTimeout` y Reintento Automático

> **Prompt ejecutado:**
> ```text
> En la sección "Cargas" de "CentroTareas" (com.merino.centrotareas), mejora la distribución de controles e implementa estas tres mejoras de resiliencia y rendimiento sin romper MVVM:
> 
> 1. DISTRIBUCIÓN DE CONTROLES EN LA SECCIÓN "CARGAS" (PantallaTareas.kt):
> - Agrupa los interruptores de prueba ("Simular error de red" y "Forzar Timeout 2s") dentro de un contenedor limpio tipo `Surface` o `Card` secundaria con título "Condiciones de prueba", separado de los botones de acción ("Una por una" y "A la vez").
> 
> 2. REINTENTO AUTOMÁTICO CON ESPERA CRECIENTE (TareasViewModel.kt):
> - Implementa `suspend fun <T> ejecutarConReintentos(maxIntentos: Int = 3, bloque: suspend (intento: Int) -> T): T` que reintente ante `IOException` con espera creciente (`500L * intento`), mostrando un mensaje de estado en pantalla antes de emitir el error definitivo. Asegúrate de no atrapar `CancellationException` ni `TimeoutCancellationException`.
> 
> 3. TIEMPO MÁXIMO CON withTimeout (TareasViewModel.kt):
> - Envuelve la ejecución de `cargarUnaPorUna()` y `cargarALaVez()` dentro de `withTimeout(if (forzarTimeout) 2000L else 6000L)`. Si excede el límite, captura `TimeoutCancellationException` y muestra el estado `CargaUi.Error("Tardó demasiado")`.
> 
> 4. TABLA COMPARATIVA DE TIEMPOS (Estados.kt, TareasViewModel.kt y PantallaTareas.kt):
> - Crea `data class RegistroTiempo(val intento: Int, val modo: String, val milisegundos: Long)` y expón `val historialTiempos: StateFlow<List<RegistroTiempo>>` para renderizar una tabla comparativa con botón "Limpiar tabla".
> ```
* **Commit asociado:** `feat(cargas): agrega tabla comparativa, withTimeout y reintentos automaticos`

---

### 4️⃣ Prompt 4: Persistencia de los Últimos 5 Resultados con Jetpack DataStore

> **Prompt ejecutado:**
> ```text
> Implementa la mejora "Historial de resultados con DataStore" en la cuarta pestaña ("Historial") de "CentroTareas" (com.merino.centrotareas) siguiendo estrictamente la arquitectura MVVM + Repository:
> 
> 1. DEPENDENCIA Y DATASTORE (build.gradle.kts y data/HistorialDataStore.kt):
> - Agrega `androidx.datastore:datastore-preferences:1.1.1` en `app/build.gradle.kts`.
> - Crea `HistorialDataStore(context: Context)` en el paquete `data` usando `preferencesDataStore(name = "historial_tareas")` para guardar una lista con los últimos 5 resultados ejecutados serializados de forma segura con `org.json.JSONArray`.
> 
> 2. INTEGRACIÓN LIMPIA SIN FILTRAR CONTEXT AL VIEWMODEL:
> - Inyecta `HistorialDataStore` en `TareasRepositoryLocal` (pasando `applicationContext` desde `MainActivity.kt`, nunca hacia `TareasViewModel`).
> - Expón `val ultimosResultados: Flow<List<String>>` hacia `TareasViewModel` usando `.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())`.
> - Guarda automáticamente cada resultado exitoso al finalizar `contar()`, `cargarUnaPorUna()` y `cargarALaVez()`, conservando siempre los últimos 5.
> 
> 3. INTERFAZ DE LA PESTAÑA "HISTORIAL" (PantallaTareas.kt):
> - Muestra una lista limpia con tarjetas individuales para cada uno de los últimos 5 resultados guardados, un botón "Borrar historial" y un mensaje vacío amigable si aún no hay registros.
> ```
* **Commit asociado:** `feat(datastore): persiste los ultimos 5 resultados en la seccion Historial`

---

## ✅ Verificación Final de la Rama `conia`

```bash
# 1. Auditoría de primitivas prohibidas (debe devolver vacío)
git grep -E "GlobalScope|runBlocking|Thread\.sleep"

# 2. Subida final de documentación y código
git add PROMPTS.md
git commit -m "docs: agrega PROMPTS.md con registro de mejoras asistidas por IA"
git push origin conia
```