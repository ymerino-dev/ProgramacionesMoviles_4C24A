# Registro de Prompts - Fase 2 con IA (conia)

## Consulta 1
- Fecha: 2026-11-11
- Herramienta: Android Studio AI / Claude / Gemini
- Prompt: "¿Cómo estructurar las pruebas unitarias para un ViewModel con Kotlin Coroutines y Flow usando un repositorio falso (FakeItemRepository) para cumplir con el requisito R12?"
- Qué respondió (resumen): Sugirió utilizar `StandardTestDispatcher` y `TestScope`, junto con `runTest`, para controlar la ejecución de corrutinas en los tests unitarios. Indicó crear una implementación de `ItemRepository` en memoria que devuelva `MutableStateFlow` para simular la emisión de items.
- Qué usamos / qué cambiamos y por qué: Usamos el enfoque de `FakeItemRepository` implementando `ItemRepository` con `MutableStateFlow` para emitir listas de prueba y registrar llamadas a `refrescar()` o `alternarFavorito()`. Esto permitió verificar el comportamiento reactivo de `ListaViewModel` sin depender de Room ni Retrofit reales.
- Qué aprendimos: Aprendimos a configurar `Dispatchers.setMain` y `Dispatchers.resetMain` para pruebas de ViewModels que utilizan `viewModelScope`.

## Consulta 2
- Fecha: 2026-11-11
- Herramienta: Android Studio AI / Claude / Gemini
- Prompt: "¿Cómo estructurar el archivo PROMPTS.md y el plan de commits para la Fase 2 (conia) del laboratorio integrador siguiendo las directrices de la rúbrica?"
- Qué respondió (resumen): Proporcionó la estructura exacta basada en la plantilla del documento de laboratorio, exigiendo al menos 3 consultas documentadas y commits claros en español con prefijos convencionales (`test:`, `docs:`, `refactor:`).
- Qué usamos / qué cambiamos y por qué: Adoptamos la plantilla formal con los apartados solicitados y establecimos los commits detallados para documentar la IA y las pruebas unitarias.
- Qué aprendimos: La importancia de registrar detalladamente el prompt, la respuesta, las decisiones de diseño y el aprendizaje obtenido para cumplir con los estándares académicos y de ingeniería.

## Consulta 3
- Fecha: 2026-11-11
- Herramienta: Android Studio AI / Claude / Gemini
- Prompt: "¿Cómo integrar y sincronizar la rama conia con los cambios del repositorio principal y asegurar que compila correctamente?"
- Qué respondió (resumen): Recomendar sincronizar el código base de `sinia` en `conia`, agregar las dependencias de `kotlinx-coroutines-test`, implementar los tests unitarios y verificar la compilación mediante gradle.
- Qué usamos / qué cambiamos y por qué: Sincronizamos `sinia` en `conia`, añadimos la librería de pruebas y validamos el funcionamiento correcto de la app y las pruebas unitarias.
- Qué aprendimos: Mantener la consistencia entre las ramas `sinia` y `conia` facilita la transición de la Fase 1 a la Fase 2 con IA.
