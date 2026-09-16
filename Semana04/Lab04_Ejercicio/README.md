# 🛒 Mi Carrito TECSUP - Laboratorio 04

## 📌 Información General

| Campo                        | Detalle |
|:-----------------------------| :--- |
| **Título del Proyecto**      | Laboratorio 04: Mi Carrito TECSUP |
| **Docente**                  | *(Escribe aquí el nombre de tu docente)* |
| **Sección / Grupo**          | *(Escribe aquí tu sección, p. ej. C24-A)* |
| **Estudiante**               | Ana Merino |
| **Descripción del Proyecto** | Aplicación móvil desarrollada en Jetpack Compose que implementa un carrito de compras dinámico. Permite registrar productos mediante un formulario, visualizarlos en una lista desplazable (`LazyColumn`), eliminar ítems de forma individual, gestionar un estado vacío visual y calcular automáticamente el subtotal, IGV y el total a pagar. |

---

##  Cuestionario Conceptual

| Pregunta                                                                | Respuesta |
|:------------------------------------------------------------------------| :--- |
| **a) ¿Por qué `mutableStateListOf` y no una `MutableList` normal?**     | Porque `mutableStateListOf` es observable por Jetpack Compose. Al agregar o eliminar elementos, dispara la recomposición automática de la interfaz. Una `MutableList` tradicional modifica el contenido en memoria pero no actualiza la pantalla visualmente. |
| **b) ¿Por qué la lista se declara con `val`?**                          | Porque la variable sostiene la referencia fija hacia la instancia del contenedor. Lo que cambia son los datos internos alojados dentro de la lista (se añaden o borran ítems), por lo que no se requiere reasignar la variable con `var`. |
| **c) ¿Qué hace `weight(1f)` en la LazyColumn?**                         | Le otorga a la `LazyColumn` (o al `Box` cuando está vacío) todo el espacio vertical disponible en la pantalla. Esto logra que el panel de totales se mantenga fijo en la parte inferior de la vista independientemente de cuántos elementos existan. |

---

## Capturas del Emulador

| Estado del Emulador | Captura de Pantalla | Descripción |
| :---: | :---: | :--- |
| **Carrito Vacío** | *(Inserta tu imagen aquí)* | Vista inicial con el mensaje central de estado vacío y los montos en S/ 0.00. |
| **Lista con Productos** | *(Inserta tu imagen aquí)* | Vista con productos agregados en la `LazyColumn`, importes calculados y totales actualizados. |