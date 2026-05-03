# Sprint 03 – Execution & Review

## 1. Resultados obtenidos

**Comparación con Sprint Goal:**
El objetivo principal del Sprint 03 se ha cumplido en su totalidad. La aplicación "MambaJet" ha pasado de ser una app con datos en memoria a tener una capa de persistencia real y completa. Se ha implementado una base de datos Room (SQLite) con cuatro tablas (`trips`, `activities`, `users`, `access_log`), migración explícita de versión 1 a 2, y acceso mediante DAOs y el patrón Repository. La autenticación vía Firebase (login, registro con verificación de email y recuperación de contraseña) está completamente integrada. Los viajes ahora están vinculados al usuario autenticado, el perfil se persiste localmente, y cada evento de login/logout queda registrado. Todo ello se sustenta con tests instrumentados sobre el DAO y validaciones de datos en la capa lógica.

---

## 2. Tareas completadas

| ID | Completada | Comentarios |
|----|------------|-------------|
| T1.1 | Sí | `AppDatabase` (v2) con cuatro entidades, exportSchema desactivado. |
| T1.2 | Sí | `TripEntity` y `ActivityEntity` con campos datetime (`createdAt` timestamp Long), texto e integer. |
| T1.3 | Sí | `TripDao`, `ActivityDao`, `UserDao` y `AccessLogDao` implementados con operaciones Flow y suspend. |
| T1.4 | Sí | CRUD completo en todos los DAOs; `TripRepositoryImpl` y `ActivityRepositoryImpl` implementados. |
| T1.5 | Sí | `TripListViewModel` y `ActivityViewModel` reemplazados de FakeDataSource a Room; los trips cargan mediante `getTripsByUser`. |
| T1.6 | Sí | UI reactiva con `StateFlow` + `collectAsState`; cualquier cambio en la BD se refleja automáticamente en pantalla. |
| T2.1 | Sí | `google-services.json` integrado, Firebase SDK configurado en `AppModule` y `AuthRepositoryImpl`. |
| T2.2 | Sí | `LoginScreen` con formulario email/contraseña en Material 3. |
| T2.3 | Sí | Login mediante Firebase Auth (`email + password`) con feedback de estado (`Loading`, `Success`, `Error`). |
| T2.4 | Sí | Botón de logout en `UserSettingsScreen`; `AuthViewModel.logout()` registra el evento LOGOUT antes de cerrar sesión. |
| T2.5 | Sí | Todos los eventos de login, logout, registro y errores trazados con `Log.d` / `Log.e` mediante tag `AuthViewModel`. |
| T3.1 | Sí | `RegisterScreen` con campos: email, contraseña, confirmación, username, fecha de nacimiento, dirección, país, teléfono y aceptación de emails. |
| T3.2 | Sí | Registro con Firebase Auth + `sendEmailVerification` + patrón Repository (`AuthRepositoryImpl`). Verifica unicidad de username en BD local antes de crear cuenta. |
| T3.3 | Sí | `ForgotPasswordScreen` con envío de email de recuperación mediante `sendPasswordResetEmail` de Firebase. |
| T4.1 | Sí | Tabla `users` con campos: id (Firebase UID), email, username, birthdate (Long), address, country, phone, acceptEmails. Unicidad de username verificada antes del registro. |
| T4.2 | Sí | `TripEntity` incluye `userId`; `getTripsByUser(uid)` filtra los viajes por usuario; `TripListViewModel.setCurrentUser(uid)` actualiza la query al iniciar sesión. |
| T4.3 | Sí | `design.md` actualizado con esquema completo de las cuatro tablas, diagrama de estructura de BD y estrategia de migración 1→2. |
| T4.4 | Sí | Tabla `access_log` con `userId`, `event` ("LOGIN"/"LOGOUT") y `timestamp`; `AccessLogRepositoryImpl` registra cada evento desde `AuthViewModel`. |
| T5.1 | Sí | `TripDaoTest` (androidTest) con base de datos en memoria: cubre insert, read, update, delete y filtrado por usuario. |
| T5.2 | Sí | Validación de nombres de viaje duplicados en `TripDao` y en `TripListViewModel`; validación de fechas lógicas (inicio ≤ fin) en los formularios. |
| T5.3 | Sí | Logcat con tags dedicados (`TripListViewModel`, `SettingsViewModel`, `AuthViewModel`) para trazabilidad de todas las operaciones de BD. |
| T5.4 | Sí | `design.md` ya incluye el esquema de BD con tipos, restricciones y descripción de cada columna (cubierto junto a T4.3). |

---

## 3. Desviaciones y Soluciones

- **Nombre de usuario desactualizado en HomeScreen tras login:** Al navegar a "home" después de iniciar sesión, el `SettingsViewModel` no recargaba el perfil del nuevo usuario, por lo que el nombre mostrado en el botón de perfil correspondía al usuario anterior. La solución es llamar a `settingsViewModel.loadUserProfile()` en el `LaunchedEffect` del composable "home", pasando el `uid` como clave para que se dispare en cada cambio de usuario. Ver fix en el apartado de correcciones.

- **Migración Room v1 → v2:** La adición de las tablas `users` y `access_log` requirió definir una migración explícita (`MIGRATION_1_2`) en lugar de usar `fallbackToDestructiveMigration`, preservando así los datos de viajes existentes.

- **`UserInteractionTest` y multi-usuario:** Los tests instrumentados del Sprint 02 que asumían un único estado global de trips tuvieron que adaptarse al nuevo filtrado por `userId` para no interferir entre sí.

---

## 4. Retrospectiva

### Qué funcionó bien
- Tener el patrón **Repository + Hilt** ya establecido desde el Sprint 02 hizo que conectar Room al resto de la arquitectura fuera increíblemente limpio: solo hubo que crear los `RepositoryImpl` reales y cambiar el binding en `AppModule`.
- La reactividad de **Flow + StateFlow** en los DAOs de Room hizo que la UI se actualizara sola sin ningún código adicional de notificación.
- Firebase Auth funcionó a la primera gracias a tener la arquitectura de Repository preparada: simplemente se implementó la interfaz `AuthRepository`.

### Qué no funcionó
- La carga del perfil de usuario en la pantalla principal no se disparaba al volver de login, causando que el nombre mostrado quedara desactualizado. Requirió revisar dónde y cuándo llamar a `loadUserProfile()` en el flujo de navegación.
- La primera versión del `RegisterScreen` no pasaba el `birthdate` como Long correctamente (enviaba 0L), lo que requirió revisar el parseo de fecha en el formulario.

### Qué mejoraremos en el próximo sprint
- Revisar el flujo completo de navegación al inicio de cada nueva funcionalidad para detectar antes cuándo los ViewModels necesitan ser recargados.
- Añadir un mecanismo de `onResume` o un `DisposableEffect` para refrescar datos del perfil automáticamente cuando el usuario vuelve a la pantalla principal desde cualquier ruta.

---

## 5. Autoevaluación del equipo (0-10)
**Nota:** 10

**Justificación:** El Sprint 03 ha sido el más técnicamente denso de los tres, integrando Room, Firebase Auth, verificación de email, persistencia de usuario y registro de accesos en un solo sprint. Todo funciona de extremo a extremo: el usuario se registra, verifica su cuenta, inicia sesión, ve solo sus viajes, y cada acceso queda auditado en la BD. Se detectó y corrigió un bug real de UX (nombre de usuario desactualizado) y los tests cubren el DAO principal. 
