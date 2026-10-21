# 🤖 Registro de Prompts - Fase 2 (Rama `mejora-ia` / `conia`)

* **Curso:** Programaciones Móviles (Sección C24-A)
* **Docente:** Juan Jose León Suiyon
* **Estudiante:** Ana Yanira Merino Ramos

A continuación se documentan los prompts utilizados con la Inteligencia Artificial para implementar las mejoras de la Fase 2 en **CafeCampus**, especificando el objetivo, las capas modificadas y la validación técnica realizada.

---

## 1. Mejora: Buscar en el menú
* **Capas modificadas:** `ViewModel` + `View`
* **Commit asociado:** `ia: implementar barra de busqueda en tiempo real para filtrar productos del menu por nombre`

### Prompt utilizado:
> "Actúa como desarrollador Android Senior en Kotlin y Jetpack Compose. Necesito implementar la mejora 'Buscar en el menú' modificando únicamente las capas ViewModel y View, manteniendo intacto el diseño visual, colores y tipografías actuales de CafeCampus. En el ViewModel del menú/catálogo, agrega un `MutableStateFlow` para `searchQuery` y combínalo (`combine`) con el flujo actual de productos y categoría seleccionada para filtrar los productos por nombre en tiempo real (ignorando mayúsculas/minúsculas) mientras el usuario escribe. En la pantalla del menú (View), vincula un `OutlinedTextField` con el estilo actual del proyecto e ícono de búsqueda/limpiar que actualice `searchQuery` en el ViewModel."

### Resultado obtenido:
Se integró el estado reactivo `searchQuery` en el `ViewModel` combinado con la lista de productos de Room, permitiendo el filtrado instantáneo desde la barra superior del catálogo sin recargar la pantalla ni alterar el diseño de las tarjetas.

---

## 2. Mejora: Repetir pedido
* **Capas modificadas:** `ViewModel` + `View`
* **Commit asociado:** `ia: agregar boton repetir pedido en el historial para recargar productos al carrito`

### Prompt utilizado:
> "Necesito implementar la mejora 'Repetir pedido' modificando las capas ViewModel y View sin alterar el diseño general de las tarjetas actuales. En el ViewModel que gestiona el carrito e historial de pedidos del estudiante, crea una función `repetirPedido` que tome los ítems y cantidades de un pedido anterior del historial y los cargue automáticamente al estado actual del carrito de compras. En la pantalla de historial de pedidos (View), agrega un botón 'Repetir pedido' dentro de cada tarjeta de pedido que ejecute esta acción y lleve al usuario al carrito actualizado."

### Resultado obtenido:
El historial del estudiante ahora incluye la acción directa para volcar los productos de cualquier orden previa hacia el carrito activo, recalculando los subtotales automáticamente.

---

## 3. Mejora: Animaciones y estados vacíos
* **Capas modificadas:** `View`
* **Commit asociado:** `ia: incorporar transiciones de pantalla, animacion en stepper y vista de estados vacios`

### Prompt utilizado:
> "Necesito implementar la mejora 'Animaciones y estados vacíos' en la capa View conservando la paleta de colores y estética original. Realiza tres ajustes en Jetpack Compose: 1) Agrega transiciones suaves (`fadeIn`/`fadeOut`) entre pantallas en la navegación. 2) En el selector de cantidad de productos (stepper `+` y `-`), envuelve el contador en un `AnimatedContent` para animar el incremento/decremento. 3) Cuando la lista del historial de pedidos o los resultados de búsqueda estén vacíos, muestra un estado vacío con ícono ilustrativo y el mensaje 'No hay pedidos' o 'Sin resultados'."

### Resultado obtenido:
Se mejoró la experiencia de usuario (UX) añadiendo feedback visual animado en los contadores de unidades, transiciones fluidas y pantallas informativas cuando el estudiante aún no registra pedidos.

---

## 4. Mejora: Reporte del día y Gráfico real por hora
* **Capas modificadas:** `DAO` + `Repository` + `ViewModel` + `View`
* **Commit asociado:** `ia: conectar consultas reales de Room para reporte del dia y metricas del panel administrativo`

### Prompt utilizado:
> "Necesito implementar las mejoras 'Gráfico real de pedidos por hora' y 'Reporte del día' en las capas DAO, Repository, ViewModel y View. En el DAO de Room, crea consultas `@Query` que devuelvan mediante `Flow` el conteo de pedidos agrupados por hora, el producto más vendido y la suma total agrupada por método de pago. Expón estos flujos en el Repository y en el ViewModel administrativo con `StateFlow`, y conéctalos en la vista del panel de administración reemplazando los valores estáticos."

### Resultado obtenido:
El panel del perfil administrador ahora refleja métricas 100% dinámicas calculadas directamente por SQLite (Room) en tiempo real cada vez que ingresa un nuevo pedido.
