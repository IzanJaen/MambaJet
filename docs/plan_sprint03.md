# Sprint 03 – Planning Document

## 1. Sprint Goal
El objetivo principal del Sprint 03 es integrar la persistencia de datos utilizando SQLite para almacenar los detalles de viajes e itinerarios. Esto reemplazará el almacenamiento en memoria del Sprint 02 por una base de datos Room, asegurando que las operaciones CRUD sean persistentes. Además, se integrará la autenticación mediante Firebase y se implementará la persistencia local con SQLite para almacenar los datos de usuario.

---

## 2. Sprint Backlog

| ID | Tarea | Responsable | Estimación (h) | Prioridad |
|----|-------|-------------|----------------|-----------|
| T1.1 | Crear clase Room Database | Izan | 1 | Alta |
| T1.2 | Definir Entidades para Trip e ItineraryItem | Izan | 1.5 | Alta |
| T1.3 | Crear Data Access Objects (DAOs) | Izan | 1 | Alta |
| T1.4 | Implementar operaciones CRUD usando DAO | Izan | 2 | Alta |
| T1.5 | Modificar ViewModels para usar Room Database | Izan | 2.5 | Alta |
| T1.6 | Asegurar actualización de UI con cambios en base de datos | Izan | 1.5 | Alta |
| T2.1 | Conectar la app a Firebase | Izan | 1 | Alta |
| T2.2 | Diseñar pantalla de Android (formulario de login) | Izan | 2 | Alta |
| T2.3 | Implementar inicio de sesión con Firebase | Izan | 2.5 | Alta |
| T2.4 | Crear acción para cerrar sesión (log out) | Izan | 1 | Alta |
| T2.5 | Usar Logcat para rastrear operaciones y errores | Izan | 1 | Baja |
| T3.1 | Diseñar pantalla de Android (formulario de registro) | Izan | 2 | Alta |
| T3.2 | Implementar registro con Firebase y verificación de email | Izan | 3.5 | Alta |
| T3.3 | Implementar vista y acción para recuperar contraseña | Izan | 2 | Media |
| T4.1 | Persistir información de usuario en BD local | Izan | 2 | Alta |
| T4.2 | Cambiar estructura de tabla de viajes para múltiples usuarios | Izan | 1.5 | Alta |
| T4.3 | Actualizar documentación con esquema de base de datos | Izan | 1 | Media |
| T4.4 | Persistir accesos de la aplicación (login/logout) | Izan | 1.5 | Media |
| T5.1 | Escribir pruebas unitarias para DAOs e interacciones BD | Izan | 3 | Alta |
| T5.2 | Implementar validación de datos | Izan | 1.5 | Alta |
| T5.3 | Usar Logcat para rastrear operaciones de base de datos | Izan | 1 | Baja |

---

## 3. Definition of Done (DoD)

- [ ] Repositorio subido a GitHub con todas las tareas completadas.
- [ ] Documento Sprint.md actualizado con las asignaciones de tareas.
- [ ] Release v3.x.x publicada en GitHub.
- [ ] Vídeo demostrativo grabado y guardado en la carpeta /docs o documentation/evidence/v3.x.x.

---

## 4. Riesgos identificados

- Errores de validación al manejar operaciones asíncronas y de bases de datos.
- Problemas al manejar las migraciones de la base de datos Room.