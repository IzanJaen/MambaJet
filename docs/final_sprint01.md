# Sprint 01 – Execution & Review

## 1. Resultados obtenidos

**Comparación con Sprint Goal:**
El objetivo principal del Sprint 01 se ha cumplido con éxito en su totalidad. Se ha creado el esqueleto completo de "MambaJet" (Travel Planner), configurando el repositorio y estableciendo la arquitectura MVVM. Se han implementado todas las pantallas principales (Home, Detalles, Galería, Ajustes, IA y Mapas) con una navegación fluida mediante Jetpack Compose Navigation. Además, el modelo de dominio está completamente estructurado y documentado para soportar las futuras fases de desarrollo.

---

## 2. Tareas completadas

| ID | Completada | Comentarios |
|----|------------|----------------------------|
| T1.1 | Sí | Definido "MambaJet" |
| T1.2 | Sí | Integrado como `Image` en las barras superiores. |
| T1.3 | Sí | Setup inicial completado. |
| T2.1 | Sí | Repositorio activo y estructurado. |
| T2.2 | Sí | `design.md` y `color-palette.md` creados. |
| T3.1 | Sí | Implementadas con estética "Cyber-Clean" en Material 3. |
| T3.1.1 | Sí | Sin problemas |
| T3.1.2 | Sí | Refinada para mostrar barras de progreso financiero. |
| T3.1.3 | Sí | Añadidos accesos directos a Mapa e IA. Ocultado el botón de eliminar viaje por seguridad. |
| T3.1.4 | Sí | Una vez hecho el diseño, vuelto a hacer a una nueva versión más minimalista, sin tantos botones. |
| T3.1.5 | Sí | Simplificado. Se eliminaron iconos superpuestos y botones que me salian achafados. |
| T3.2 | Sí | Pasar parámetros (destinos) a través de las rutas del `NavHost` sin que la app crashee. |
| T3.3 | Sí | Generado con éxito utilizando sintaxis Mermaid en Markdown. |
| T3.4 | Sí | Error, por culpa del modo oscuro. |
| T3.4.1 a T3.4.5 | Sí | Modelos creados con anotaciones `@TODO` para futuros sprints. |
| T4.1 | Sí | Problemas iniciales en el recorte de la splash screen, finalmente ajustado. |
| T4.2 | Sí | Integración de versión, información del sprint y logo correctamente. |
| T4.3 | Sí | Implementada con scroll dinámico y formato legible. |
| T4.4 | Sí | Implementado switch funcional para Modo Oscuro y selectores de idioma nativos. |

---

## 3. Desviaciones
- **Rediseño del Itinerario:** Se dedicó más tiempo del estimado en cómo diseñar la vista de itinerario. Empecé con un diseño que no me acababa de convencer, ya que para ver tu itinerario de manera completa tenías que seleccionar más botones de los que me gustaría. Se pivotó hacia un *Timeline* vertical más limpio.
- **Colisión de Firmas en Kotlin:** Al implementar el dominio, la JVM detectó firmas duplicadas (`isDarkTheme()`) generadas automáticamente por el `data class`, lo que obligó a investigar y limpiar la estructura del modelo.

---

## 4. Retrospectiva

### Qué funcionó bien
- La adopción del patrón **MVVM** desde el principio ha mantenido el código de UI muy limpio.
- El uso de **Jetpack Compose** ha permitido iterar y cambiar diseños complejos (como los Bottom Sheets o los carruseles LazyRow) de forma muy rápida.
- La estética y los colores elegidos transmiten perfectamente la sensación de una app "Elite".

### Qué no funcionó
- Olvidar importar iconos extendidos de Material, lo que provocó crasheos temporales al navegar a la pantalla de Ajustes.

### Qué mejoraremos en el próximo sprint
- Mejorar la previsión de lo que hay que hacer exactamente.

---

## 5. Autoevaluación del equipo (0-10)
**Nota:** 9

**Justificación:** A pesar de ser el primer sprint y enfrentarme a la curva de aprendizaje de Jetpack Compose y la navegación avanzada, he logrado entregar una aplicación funcional a nivel visual, completamente navegable, con un diseño que parece de producción y una arquitectura robusta preparada para el Sprint 2. 