# Sprint 02 – Execution & Review

## 1. Resultados obtenidos

**Comparación con Sprint Goal:**
El objetivo principal del Sprint 02 se ha cumplido y superado con creces. La aplicación "MambaJet" ya es completamente funcional en memoria. Se ha establecido una Arquitectura Clean impecable con separación de responsabilidades clara (`DataSource` -> `RepositoryImpl` -> `ViewModel`). Las preferencias del usuario persisten correctamente, la aplicación es 100% bilingüe (ES, EN, CA) y reactiva: cualquier gasto nuevo en el itinerario se refleja dinámicamente en el presupuesto de la pantalla de inicio. Por último, la aplicación cuenta con un blindaje sólido de validación de datos respaldado por Tests Unitarios e Instrumentados.

---

## 2. Tareas completadas

| ID | Completada | Comentarios |
|----|------------|----------------------------|
| T1.1 | Sí | Implementado a través de `FakeTripDataSource`. |
| T1.2 | Sí | Implementado a través de `FakeActivityDataSource`. |
| T1.3 | Sí | Modificados los TextField para usar exclusivamente `DatePickerDialog` de Material 3. Fechas límite controladas. |
| T1.4 | Sí | `SharedPreferences` persistiendo Nombre, Fecha de nacimiento, Tema Oscuro y Lenguaje al iniciar la app (`MainActivity`). |
| T1.5 | Sí | Creados diccionarios `strings.xml` (en, ca, es). La UI responde al cambio instantáneamente. |
| T2.1 | Sí | Capas de dominio, datos (repository/source) y UI (ViewModel) fuertemente acopladas mediante interfaces. |
| T2.2 | Sí | Flujos de navegación completos y robustos con paso de parámetros y actualización de tarjetas. |
| T2.3 | Sí | `StateFlow` recalculando la barra de progreso de presupuesto en tiempo real al añadir planes. |
| T3.1 | Sí | Muro de seguridad en UI (botones deshabilitados) y en VM (bloqueo y `Log.e` al recibir fechas ilógicas). |
| T3.2 | Sí | 6/6 Unit Tests superados para los CRUD de viajes e itinerario. |
| T3.3 | Sí | 1/1 UI Test superado (`UserInteractionTest`) simulando escritura y comprobando avisos de seguridad. |
| T3.4 | Sí | Documento técnico formal generado y adjuntado al repositorio. |
| T3.5 | Sí | Añadidos `companion object` para etiquetas de Log y comentarios KDoc en la capa de datos. |

---

## 3. Desviaciones y Soluciones
- **Modo Oscuro** Al iniciar la app y cambiar al modo oscuro o viceversa la app dejaba de responder, pq tenia que poner cada pantalla que el color fuera el que pasaramos por la app, no por defecto el blanco, que eso causaba que se quedara pillado.
- **Test Instrumentado vs Multi-idioma:** El robot de la prueba UI no encontraba el campo de texto porque lo buscaba explícitamente en español, mientras el emulador estaba en inglés. Se solucionó obteniendo el `R.string.trip_name` dinámicamente a través del `InstrumentationRegistry`.
- **Kotlin strictness con Constantes:** Un error de sintaxis impidió compilar al usar `const val` dentro de clases. Se corrigió rápidamente moviendo las etiquetas (TAG) de los logs a bloques `companion object`.

---

## 4. Retrospectiva

### Qué funcionó bien
- Implementar todas las funciones de viaje y actividades no dieron casi ningún problema.

### Qué no funcionó
- Asumir que los tests unitarios eran totalmente independientes sin tener en cuenta que un `object` en Kotlin comparte su estado en toda la JVM durante la ejecución de los tests. Costó un poco de investigación solucionar los cruces de datos.

### Qué mejoraremos en el próximo sprint
- Mirar los siguientes sprints para poder adaptar ya las clases y pantallas a los siguientes y no tener que cambiar todas las pantallas para poder adaptarlas al modo oscuro y el multilenguaje pq con eso perdi bastante tiempo

---

## 5. Autoevaluación del equipo (0-10)
**Nota:** 9,5

**Justificación:** El segundo sprint ha ido mucho mejor que el primero y todas las funciones tanto de actividades como de viaje han ido según lo esperado, quitando algunos pequeños errores, sobretodo con el modo oscuro