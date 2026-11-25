<div align="center">

# 📱 AvisosCampus

**Gestión inteligente de notificaciones locales, recordatorios asíncronos y anuncios del sistema en Android**

![Kotlin](https://img.shields.io/badge/Kotlin-2.0+-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Android](https://img.shields.io/badge/Android-API%2026%20--%2036-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Architecture](https://img.shields.io/badge/Arquitectura-MVVM%20%2B%20UDF-FF6F00?style=for-the-badge)

</div>

---

## 📋 Descripción del proyecto

**AvisosCampus** es una aplicación móvil nativa desarrollada en **Kotlin** con **Jetpack Compose** bajo el patrón arquitectónico **MVVM (Model-View-ViewModel)** y flujo de datos unidireccional (**UDF**).

El proyecto implementa el ciclo completo de notificaciones locales en Android moderno (Android 13 / API 33+): registro de canales de notificación, solicitud y manejo resiliente de permisos en tiempo de ejecución, navegación directa mediante `PendingIntent`, temporizadores cancelables con corrutinas (`Job`) y escucha dinámica de eventos del sistema operativo (`BroadcastReceiver`) vinculada al ciclo de vida de la interfaz.

---

## ✨ Características principales

| Módulo | Funcionalidad | Implementación técnica |
| :--- | :--- | :--- |
| **Control de Permisos** | Solicitud de `POST_NOTIFICATIONS` en tiempo de ejecución y recuperación ante denegación mediante acceso directo a Ajustes. | `ActivityResultContracts.RequestPermission` + `Settings.ACTION_APP_NOTIFICATION_SETTINGS` |
| **Notificaciones Locales** | Creación de canal dedicado al arrancar la app y envío de notificaciones táctiles que abren `MainActivity`. | `NotificationChannel` (`AvisosApp`) + `NotificationCompat.Builder` + `PendingIntent` |
| **Recordatorios** | Cuenta regresiva de 10 segundos con estado en tiempo de ejecución y cancelación limpia sin fugas ni emisiones residuales. | `viewModelScope.launch` + `Job` + bloque `try / finally` |
| **Anuncios del Sistema** | Detección en tiempo real cuando el usuario activa o desactiva el modo avión del dispositivo. | `BroadcastReceiver` (`ACTION_AIRPLANE_MODE_CHANGED`) + `DisposableEffect` |
| **Historial Reactivo** | Listado cronológico inverso (los 20 más recientes arriba) con insignia de origen y marca de hora persistente ante rotaciones. | `StateFlow<List<Aviso>>` + `Column` con `forEach` |

---

## 🗂️ Estructura del proyecto (Árbol de paquetes)

```text
app/src/main/java/com/merino/avisoscampus/
├── 📁 data/
│   └── AvisosRepository.kt          # Fuente de verdad en memoria; registra avisos con ID único y hora
├── 📁 model/
│   ├── Aviso.kt                     # Entidad de dominio (id, titulo, texto, origen, hora)
│   ├── Origen.kt                    # Enum de orígenes: BOTON, RECORDATORIO, MODO_AVION
│   └── PermisoUi.kt                 # Estados sellados de UI: SinPedir, Concedido, Denegado
├── 📁 notificacion/
│   └── Notificador.kt               # Gestión del canal 'avisos', validación de permiso y disparo con PendingIntent
├── 📁 receiver/
│   └── ModoAvionReceiver.kt         # BroadcastReceiver dinámico para Intent.ACTION_AIRPLANE_MODE_CHANGED
├── 📁 ui/
│   ├── 📁 theme/                    # Configuración visual Material Design 3 (Color, Theme, Type)
│   ├── HistorialAvisos.kt           # Componente desacoplado que renderiza los últimos 20 avisos
│   └── PantallaAvisos.kt            # Vista principal, observadores de estado y ciclo de vida del receiver
├── 📁 viewmodel/
│   ├── AvisosViewModel.kt           # Lógica de presentación, corrutinas, StateFlow y SharedFlow
│   └── AvisosViewModelFactory.kt    # Inyección manual de AvisosRepository hacia el ViewModel
├── AvisosApp.kt                     # Subclase de Application; inicializa el canal al abrir la app
└── MainActivity.kt                  # Actividad anfitriona que monta PantallaAvisos
```

---

## 🏗️ Arquitectura MVVM y Flujo de Datos

El `ViewModel` permanece **100 % libre de referencias a `Context` o componentes de Android**, comunicando acciones únicas (disparo de notificaciones) mediante `SharedFlow` y estados persistentes mediante `StateFlow`:

```text
╔═══════════════════════════════════════════════════════════════════════════╗
║                           CAPA DE INTERFAZ (Compose)                      ║
║                                                                           ║
║   ┌───────────────────────────┐         ┌─────────────────────────────┐   ║
║   │     PantallaAvisos.kt     │ ──────► │     HistorialAvisos.kt      │   ║
║   └─────┬───────────────▲─────┘         └─────────────────────────────┘   ║
║         │               │                                                 ║
║         │               ├── collectAsState() (permiso, faltan, avisos...) ║
║         │               └── collect() (SharedFlow<Aviso>)                 ║
║         │                      └─► Notificador.mostrar(context, aviso)    ║
║         │                            └─ si false ──► vm.noSePudoMostrar() ║
║         │                                                                 ║
║         ├── DisposableEffect(escuchando)                                  ║
║         │     └─► ModoAvionReceiver ──► vm.alCambiarModoAvion(activo)     ║
║         │                                                                 ║
║         └── Acciones de usuario (Clicks / Switch / Ajustes) ──────────┐   ║
╚═══════════════════════════════════════════════════════════════════════╪═══╝
                                                                        │
                                                                        ▼
╔═══════════════════════════════════════════════════════════════════════════╗
║                        CAPA DE PRESENTACIÓN (ViewModel)                   ║
║                            AvisosViewModel.kt                             ║
║                                                                           ║
║  • StateFlow: permiso | faltan | escuchandoAvion | mensaje | avisos       ║
║  • SharedFlow: eventos (emisión puntual de notificaciones)                ║
║  • Corrutinas: recordarEn(10) con Job cancelable y limpieza en finally    ║
╚═══════════════════════════════════════════════════════════════════════╤═══╝
                                                                        │
                                                                        ▼
╔═══════════════════════════════════════════════════════════════════════════╗
║                           CAPA DE DATOS (Repository)                      ║
║                           AvisosRepository.kt                             ║
║                                                                           ║
║  • Almacena y ordena los avisos generados (más recientes primero)         ║
╚═══════════════════════════════════════════════════════════════════════════╝
```

---

## 📸 Capturas de pantalla

| 1. Notificación en el panel | 2. Recordatorio (Cuenta regresiva) | 3. Receptor de Modo Avión |
| :---: | :---: | :---: |
| <img src="capturas/01_notificacion_panel.png" width="240" alt="Notificación en el panel"/> | <img src="capturas/02_recordatorio.png" width="240" alt="Recordatorio en 10 segundos"/> | <img src="capturas/03_modo_avion.png" width="240" alt="Modo avión"/> |

| 4. Historial de avisos | 5. Manejo de permiso denegado |
| :---: | :---: |
| <img src="capturas/04_historial.png" width="240" alt="Historial de avisos"/> | <img src="capturas/05_permiso_denegado.png" width="240" alt="Permiso denegado"/> |

---

## ✅ Pruebas de aceptación verificadas

| Prueba ejecutada | Resultado esperado | Estado |
| :--- | :--- | :---: |
| Abrir *Ajustes → Apps → AvisosCampus → Notificaciones* | Existe el canal **Avisos** | ✅ |
| Pulsar **Permitir notificaciones** | Muestra el diálogo de Android y la etiqueta cambia a **Permitido** | ✅ |
| Pulsar **Mostrar aviso** | Aparece la notificación en la barra superior | ✅ |
| Pulsar **Mostrar aviso** dos veces | Aparecen dos notificaciones independientes (IDs distintos) | ✅ |
| Tocar la notificación en el panel | Abre `MainActivity` y la notificación desaparece (`autoCancel`) | ✅ |
| Pulsar **Recordar en 10 s** | Cuenta regresiva de 10 a 1 y notificación al llegar a 0 | ✅ |
| Recordar y minimizar la app | El aviso llega igualmente en segundo plano | ✅ |
| Recordar y pulsar **Cancelar** | Se detiene el contador y no se emite ningún aviso | ✅ |
| Switch activado y cambiar Modo avión | Emite aviso **Activado** y luego **Desactivado** | ✅ |
| Switch desactivado y cambiar Modo avión | No se registra ni se muestra ningún aviso | ✅ |
| Permiso denegado y pulsar **Mostrar aviso** | Muestra mensaje de error, guarda en historial y la app no se cierra | ✅ |
| Rotar la pantalla durante el recordatorio | El historial y la cuenta regresiva continúan sin perder estado | ✅ |
| Revisión de arquitectura del código | Cero referencias a `Context` en `viewmodel/` y limpieza en `onDispose` | ✅ |

---

## 🧠 Para pensar

### 1. ¿Por qué una notificación necesita un canal y un permiso? ¿Qué pasa si falta cada uno?
* **Canal (`NotificationChannel`):** Introducido en Android 8.0 (API 26), agrupa las notificaciones por categoría temática para que el usuario decida de forma granular qué alertas desea silenciar, vibrar o bloquear sin desactivar toda la app. **Si falta el canal** (o se envía un ID de canal no registrado), el sistema operativo descarta silenciosamente la notificación y nunca la muestra.
* **Permiso (`POST_NOTIFICATIONS`):** Introducido en Android 13 (API 33) como permiso en tiempo de ejecución para proteger al usuario frente a notificaciones no deseadas. **Si falta el permiso**, Android bloquea la publicación de la notificación en la barra de estado (y si el código no lo valida previamente, puede provocar una `SecurityException`).

### 2. ¿Por qué el ViewModel emite un evento y no muestra la notificación él mismo?
Mostrar una notificación exige una instancia de `Context` de Android (`NotificationManagerCompat.from(context)`). Pasar o almacenar un `Context` ligado a una `Activity` dentro de un `ViewModel` provoca **fugas de memoria (*memory leaks*)** al rotar la pantalla o destruir la vista, además de acoplar la lógica de negocio al framework de Android y dificultar las pruebas unitarias. Al emitir un evento mediante `SharedFlow<Aviso>`, el `ViewModel` solo comunica *qué* ocurrió y la capa de UI ejecuta el efecto secundario de forma segura.

### 3. ¿Qué pasa si usas el mismo `id` en dos notificaciones?
Android identifica cada notificación activa mediante la clave compuesta `(paquete, id)`. Si se dispara una segunda notificación reutilizando el mismo `id` de una notificación que aún sigue en la barra de estado, **la nueva notificación sobrescribe y reemplaza a la anterior** en lugar de mostrarse como una entrada adicional.

### 4. ¿Por qué `onReceive` debe ser corto y por qué hay que quitar el receptor al salir?
* **Por qué `onReceive` debe ser corto:** El método `onReceive` se ejecuta en el hilo principal (*Main Thread*) y Android le asigna un tiempo máximo de ejecución muy breve (~10 segundos). Ejecutar operaciones pesadas o bloqueantes allí congelaría la interfaz y provocaría un error **ANR (*Application Not Responding*)**.
* **Por qué quitar el receptor al salir (`unregisterReceiver`):** Un `BroadcastReceiver` registrado dinámicamente queda enlazado al `Context` que lo registró. Si no se libera dentro de `onDispose`, el receptor permanece activo en memoria recibiendo eventos del sistema cuando la pantalla ya no existe, generando fugas de memoria y consumo innecesario de batería.

### 5. ¿En qué casos usarías una notificación local y en cuáles una push de Firebase?
* **Notificación local:** Cuando el disparador nace dentro del propio dispositivo o depende del estado local del usuario sin necesitar conexión a un servidor (por ejemplo: alarmas, temporizadores, recordatorios de estudio programados localmente o cambios de hardware/conectividad como el modo avión).
* **Notificación push (Firebase Cloud Messaging):** Cuando el evento se origina en un servidor remoto o por acción de otro usuario y debe entregarse en tiempo real incluso con la aplicación cerrada (por ejemplo: mensajes de chat, publicación de calificaciones en el portal académico, alertas institucionales del campus o cambios de aula realizados por un docente).