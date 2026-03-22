# Sprint 02 – Planning Document

## 1. Sprint Goal
El objetivo principal del Sprint 02 es implementar la lógica de negocio completa de "MambaJet". Pasaremos de un diseño estático a una aplicación completamente funcional (en memoria), estableciendo una Arquitectura Clean (UI → ViewModel → Repository → Data Source). Además, implementaremos validación de datos estricta, guardado de preferencias del usuario, sistema multi-idioma y una batería completa de pruebas (Unitarias e Instrumentadas) para asegurar la robustez del código.

---

## 2. Sprint Backlog

| ID | Tarea | Responsable | Estimación (h) | Prioridad |
|----|-------|-------------|----------------|-----------|
| T1.1 | Implementar CRUD en memoria para Viajes (Trips) | Izan | 2 | Alta |
| T1.2 | Implementar CRUD en memoria para Actividades/Itinerario | Izan | 2 | Alta |
| T1.3 | Validación de datos con DatePickers y fechas lógicas | Izan | 2.5 | Alta |
| T1.4 | Guardar ajustes de usuario con SharedPreferences | Izan | 2 | Alta |
| T1.5 | Implementar Multi-idioma (en, ca, es) | Izan | 1.5 | Alta |
| T2.1 | Estructurar Arquitectura Clean (DataSource -> Repo -> VM) | Izan | 3 | Alta |
| T2.2 | Flujo UI básico para añadir/modificar viajes e itinerario | Izan | 1 | Alta |
| T2.3 | Actualización dinámica de la UI (StateFlow y recálculos) | Izan | 2 | Alta |
| T3.1 | Validación de inputs en la capa UI y ViewModel | Izan | 2 | Alta |
| T3.2 | Escribir Unit Tests para operaciones CRUD (JUnit) | Izan | 3 | Alta |
| T3.3 | Simular interacciones de usuario (UI Tests) y Log de errores | Izan | 2.5 | Alta |
| T3.4 | Actualizar documentación con resultados de pruebas | Izan | 1 | Media |
| T3.5 | Añadir Logs estructurados y comentarios KDoc | Izan | 1 | Media |

---

## 3. Definition of Done (DoD)

- [ ] Todas las operaciones CRUD funcionan y se reflejan dinámicamente en la UI.
- [ ] Las fechas de las actividades no pueden salirse del rango del viaje.


---

## 4. Riesgos identificados

- Curva de aprendizaje al implementar pruebas instrumentadas (Compose UI Testing).
- Posibles fallos de estado global al usar bases de datos "Fake" en memoria durante la ejecución simultánea de los tests.
