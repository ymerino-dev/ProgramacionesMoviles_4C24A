# 📱 CentroTareas — Programación Concurrente en Android

![Kotlin](https://img.shields.io/badge/Kotlin-2.0+-7F52FF?style=flat&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?style=flat&logo=jetpackcompose&logoColor=white)
![Coroutines](https://img.shields.io/badge/Kotlin-Coroutines%20%26%20Flow-0095D5?style=flat)
![Architecture](https://img.shields.io/badge/Arquitectura-MVVM%20%2B%20Repository-2E7D32?style=flat)

Aplicación móvil Android desarrollada en **Kotlin** con **Jetpack Compose** y **Material 3** que demuestra cómo ejecutar operaciones prolongadas, cálculos intensivos de CPU y cargas concurrentes en segundo plano sin bloquear jamás el hilo principal (*Main Thread*).

* **Curso:** Programación en Móviles — Laboratorio N° 14
* **Docente:** Juan León S.
* **Autora:** Ana Yanira Merino Ramos

---

## 🏗️ Arquitectura MVVM + Repository

La aplicación implementa **Flujo Unidireccional de Datos (UDF)** y separación estricta de responsabilidades entre capas:

```text
┌─────────────────────────────────────────────────────────────────────────┐
│                           VIEW (PantallaTareas)                         │
│  • Observa los StateFlow mediante collectAsState()                      │
│  • Renderiza la UI declarativa y envía eventos de usuario al ViewModel  │
└───────────────────┬─────────────────────────────────▲───────────────────┘
                    │ Eventos (clics, switch)         │ StateFlow<UiState>
                    ▼                                 │
┌─────────────────────────────────────────────────────────────────────────┐
│                       VIEWMODEL (TareasViewModel)                       │
│  • Lanza corrutinas atadas al ciclo de vida con viewModelScope          │
│  • Gestiona cancelaciones con Job y concurrencia con async/await        │
└───────────────────┬─────────────────────────────────▲───────────────────┘
                    │ Llamadas a funciones suspend    │ Resultados / Excepciones
                    ▼                                 │
┌─────────────────────────────────────────────────────────────────────────┐
│                  REPOSITORY (TareasRepositoryLocal)                     │
│  • Implementa el contrato de la interfaz TareasRepository               │
│  • Ejecuta cálculos pesados en Dispatchers.Default y cargas simuladas   │
└─────────────────────────────────────────────────────────────────────────┘
```

### 📂 Estructura de Paquetes

```text
com.merino.centrotareas/
├── MainActivity.kt               # Punto de entrada e inicialización del ViewModel
├── model/
│   └── Estados.kt                # Interfaces selladas (PrimosUi, CargaUi)
├── data/
│   ├── TareasRepository.kt       # Contrato (interface) de acceso a datos
│   └── TareasRepositoryLocal.kt  # Lógica de cálculo de primos y cargas simuladas
├── viewmodel/
│   ├── TareasViewModel.kt        # Gestión de estado reactivo y control de corrutinas
│   └── TareasViewModelFactory.kt # Fábrica para inyección del repositorio
└── ui/
    ├── PantallaTareas.kt         # Interfaz Jetpack Compose con las 3 tarjetas
    └── theme/                    # Configuración de tema, colores y tipografía
```

---

## 🔬 Observación del Paso 1 (Bloqueo del Hilo Principal)

Durante la implementación inicial con `Thread.sleep(4000)` ejecutado directamente sobre el hilo principal, se comprobaron los siguientes efectos:

* **¿Qué pasa con la pantalla?**  
  Al pulsar el botón **Tarea larga (MAL)** y luego intentar presionar **Tócame**, la aplicación queda completamente congelada durante 4 segundos. Ningún componente visual responde a los toques en tiempo real; los eventos quedan encolados en el sistema y recién se reflejan de golpe al terminar la espera.

* **¿Por qué no se ve `"Trabajando..."`?**  
  Porque modificar el valor de un `StateFlow` no pinta la interfaz de manera inmediata, sino que agenda una recomposición en el *Main Thread*. Al secuestrar dicho hilo inmediatamente después con `Thread.sleep(4000)`, el motor gráfico de Compose no puede renderizar el *frame* intermedio antes de que el estado vuelva a cambiar a `"Terminó"`.

* **¿Qué mostraría Android si la espera fuera de 10 segundos?**  
  Al superar el umbral de 5 segundos con el hilo de interfaz bloqueado sin procesar eventos de entrada, Android desplegaría el cuadro de diálogo crítico **ANR (*Application Not Responding*)**, permitiendo al usuario forzar el cierre de la aplicación.

---

## 📸 Capturas de Ejecución

### 1. Pantalla Congelada / Tarea con Progreso
> *Coloca aquí tu captura de pantalla:*

![Tarea con progreso](capturas/01_progreso.png)

### 2. Tarea Cancelada a la Mitad
> *Coloca aquí tu captura de pantalla:*

![Tarea cancelada](capturas/02_cancelado.png)

### 3. Cálculo de Números Primos
> *Coloca aquí tu captura de pantalla:*

![Cálculo de primos](capturas/03_primos.png)

### 4. Comparación de Cargas: Una por una vs. A la vez (`async`)
> *Coloca aquí tu captura de pantalla:*

![Cargas en paralelo](capturas/04_cargas.png)

### 5. Error Simulado y Botón Reintentar
> *Coloca aquí tu captura de pantalla:*

![Error simulado](capturas/05_error.png)

---

## ✅ Pruebas de Aceptación Verificadas

- [x] **Tarea larga:** El botón *Tócame* sigue sumando mientras la tarea trabaja en segundo plano.
- [x] **Logcat (`HILO`):** Muestra `main` antes y después de `withContext`, y `DefaultDispatcher-worker` dentro.
- [x] **Barra de progreso:** Avanza de `0 %` a `100 %` en ~4 segundos.
- [x] **Cancelación:** Al pulsar *Cancelar* a mitad de camino, la barra se detiene, muestra `Cancelado` y permite reiniciar.
- [x] **Protección contra doble clic:** El botón *Iniciar* permanece deshabilitado mientras la corrutina está activa.
- [x] **Números primos:** Para `N = 1000` devuelve `168`; para `N = 500000` devuelve `41538` sin congelar la UI y permitiendo cancelar.
- [x] **Cargas secuenciales vs. paralelas:** *Una por una* tarda ~5000 ms; *A la vez* tarda ~3000 ms.
- [x] **Manejo de errores:** Con *Simular error* activo muestra el recuadro rojo; al desactivarlo y pulsar *Reintentar* carga con éxito.
- [x] **Rotación de pantalla:** Al girar el dispositivo durante una ejecución, la tarea continúa y el progreso no se pierde.
- [x] **Auditoría de código:** Verificado con `git grep` sin presencia de `GlobalScope`, `runBlocking` ni `Thread.sleep`.

---

## 🧠 Para Pensar (Preguntas y Respuestas)

1. **¿Por qué no se veía `"Trabajando..."` en el Paso 1?**  
   Porque cambiar el valor de un `StateFlow` no dibuja la interfaz de forma síncrona, sino que agenda una recomposición en el hilo principal (*Main Thread*) para el siguiente ciclo de dibujo (*frame*). Al ejecutar `Thread.sleep(4000)` en la línea siguiente sobre ese mismo hilo, el motor gráfico quedó bloqueado y no pudo renderizar el estado intermedio antes de que cambiara a `"Terminó"`.

2. **¿Qué diferencia hay entre `Thread.sleep` y `delay`?**  
   `Thread.sleep` bloquea físicamente el hilo del sistema operativo, dejándolo inactivo e incapaz de atender la interfaz gráfica o eventos táctiles durante ese tiempo. En cambio, `delay` es una función `suspend` que pausa únicamente la corrutina sin bloquear el hilo subyacente, liberándolo para atender otras tareas y reanudando la ejecución cuando expira el tiempo.

3. **¿Por qué `Dispatchers.Default` para los primos y `Dispatchers.IO` para la red?**  
   `Dispatchers.Default` utiliza un *pool* de hilos dimensionado según la cantidad de núcleos de la CPU, optimizado para cálculos matemáticos intensivos de procesador como verificar números primos. Por su parte, `Dispatchers.IO` dispone de un *pool* elástico (hasta 64 hilos por defecto) pensado para operaciones de entrada/salida (peticiones de red, lectura de disco o bases de datos) donde los hilos pasan la mayor parte del tiempo esperando respuestas externas sin saturar la CPU.

4. **¿Qué pasaría con la corrutina si el usuario cierra la pantalla? ¿Qué papel cumple `viewModelScope`?**  
   Si la corrutina se lanzara en un ámbito desligado del ciclo de vida, seguiría ejecutándose en segundo plano tras cerrar la pantalla, causando fugas de memoria (*memory leaks*) o fallos al intentar actualizar una vista destruida. `viewModelScope` vincula las corrutinas al ciclo de vida del `ViewModel`: las mantiene activas durante cambios de configuración (como rotar la pantalla) y las cancela automáticamente cuando el usuario abandona definitivamente la pantalla (`onCleared()`).

5. **¿Por qué `async` tarda 3 s y no 5 s? ¿Qué pasa si una de las dos cargas falla?**  
   Porque `async` lanza ambas tareas de forma concurrente en paralelo; por ello, el tiempo total de espera está determinado por la tarea más lenta ($\max(2\text{ s}, 3\text{ s}) \approx 3\text{ s}$) en lugar de la suma secuencial ($2\text{ s} + 3\text{ s} = 5\text{ s}$). Además, al ejecutarse dentro de un bloque `coroutineScope` (concurrencia estructurada), si una de las dos llamadas lanza una excepción (`IOException`), el *scope* cancela inmediatamente la tarea hermana en curso y propaga el error hacia el bloque `try/catch` exterior.