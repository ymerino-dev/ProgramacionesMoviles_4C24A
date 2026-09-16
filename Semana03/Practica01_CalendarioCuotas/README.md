# Práctica 01: Calendario de Cuotas (Sistema de Pagos)

**Alumno:** Ana Yanira Merino Ramos
**Docente:** Juan José León Suiyon
**Salón:** C24A

## Descripción del Proyecto
Esta es una aplicación de consola desarrollada en Kotlin que simula un sistema de pagos generando un cronograma mensual automatizado.

Características técnicas de la lógica implementada:
* **Ingreso y Validación:** Solicita los datos del producto y utiliza un ciclo `while` para validar estrictamente que el usuario solo pueda elegir 6, 12 o 24 cuotas.
* **Cálculo de Interés:** Aplica un interés dinámico (20%, 40% o 60%) dependiendo de las cuotas elegidas utilizando la estructura de decisión múltiple `when`.
* **Generación de Calendario:** Utiliza la librería `LocalDate` de Java/Kotlin junto con un ciclo `for` para generar una tabla de amortización (fechas y montos). El bucle calcula automáticamente la reducción del saldo pendiente mes a mes usando la herramienta `String.format` para alinear las columnas del reporte.