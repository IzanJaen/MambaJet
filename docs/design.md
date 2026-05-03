# 🛠️ Fundamentos Arquitectónicos de MambaJet

## 🏗️ Estructura del Proyecto (Patrón MVVM)

Para asegurar que MambaJet sea una plataforma robusta y fácilmente mantenible a largo plazo, hemos adoptado el patrón de diseño **MVVM (Model-View-ViewModel)**. Esto nos permite aislar de forma estricta la interfaz de usuario de las reglas lógicas del sistema.

- **Capa Visual (`ui/`):** Desarrollada 100% de forma declarativa utilizando **Jetpack Compose**. Hemos exprimido el sistema **Material 3** para implementar nuestra estética "Cyber-Clean", garantizando una transición impecable y nativa entre los modos Claro y Oscuro. Para evitar código duplicado y mantener la coherencia, elementos clave como las barras de navegación, los botones flotantes y las tarjetas de itinerario han sido modularizados como componentes reutilizables.
- **Capa Lógica (`domain/`):** El verdadero motor de la aplicación. Aquí residen nuestros modelos de datos y la lógica pura del negocio. Al construir esta capa de forma completamente independiente de las librerías de Android (framework-agnostic), nos aseguramos de que implementar tests unitarios o conectar la app a un backend en la nube (como Firebase) en futuros sprints sea un proceso directo y sin fricciones.

El flujo de pantallas se rige por una arquitectura *Single-Activity*. Toda la navegación se orquesta desde el `MainActivity` mediante **Jetpack Navigation Compose**, utilizando un `NavHost` unificado que conecta de manera fluida el Dashboard principal, el panel de detalles de la misión, la galería visual y las preferencias del sistema.

## 🗄️ Esquema de Datos: Escalabilidad y Lógica Avanzada

El modelo de dominio no es un simple contenedor de variables; está estructurado para soportar el roadmap completo de MambaJet, priorizando el rendimiento, la logística y el control financiero del usuario.

Nuestras principales decisiones de ingeniería incluyen:
- **Desacoplamiento de Seguridad (`User` vs `Authentication`)**: Aplicando el Principio de Responsabilidad Única (SRP), hemos blindado la aplicación separando radicalmente la gestión de credenciales y tokens de sesión de la información pública y estética del perfil del usuario.
- **Motor Financiero Integrado en `Trip`**: La entidad del viaje procesa su estado económico de forma autónoma. Funciones proyectadas como `calculateDailyBudgetAllowance` actuarán como un salvavidas financiero, recalculando en tiempo real cuánto puede gastar el usuario al día sin salirse de su presupuesto.
- **Modelo Híbrido en `Activity` (con `PlanType`)**: En lugar de fragmentar la base de datos creando modelos distintos para vuelos, hoteles o restaurantes, utilizamos una única clase flexible apoyada por un Enum. Esto simplifica enormemente la arquitectura y permite aplicar funciones globales, como exportar el evento al calendario del móvil (`addToDeviceCalendar`) o disparar alertas de proximidad (`isUpcoming`).
- **Ecosistema de Radar (`TripMap`)**: Diseñado para ir mucho más allá de un simple mapa estático. Su estructura está preparada para trazar los waypoints del itinerario, calcular rutas logísticas y filtrar Puntos de Interés (POIs) clave en el destino.
- **Cimientos para la Inteligencia Artificial (`AIRecommendation`)**: Desde el día uno, la estructura de datos cuenta con el nodo necesario para recibir y procesar las futuras sugerencias generadas por la IA, permitiendo optimizar presupuestos sobrantes y generar rutas inteligentes automáticamente.


### 🗺️ Diagrama de Clases (UML)

![Domain_model](domain_model.png)

---

## 🗃️ Base de Datos Local — Room Database (Sprint 03)

### Librería y configuración

MambaJet utiliza **Room** (Jetpack) como capa de abstracción sobre SQLite. La inyección de dependencias se gestiona con **Hilt**. La base de datos se declara en `AppDatabase` y actualmente está en la **versión 2**, con una migración explícita desde la versión 1.

```
AppDatabase (version = 2)
├── TripEntity        → tabla: trips
├── ActivityEntity    → tabla: activities
├── UserEntity        → tabla: users        (nuevo en Sprint 03 — T4.1)
└── AccessLogEntity   → tabla: access_log   (nuevo en Sprint 03 — T4.4)
```

---

### 📐 Esquema de tablas

#### Tabla `trips`

| Columna       | Tipo    | Restricción     | Descripción                                      |
|---------------|---------|-----------------|--------------------------------------------------|
| `id`          | TEXT    | PRIMARY KEY     | UUID generado en la capa de dominio              |
| `userId`      | TEXT    | NOT NULL        | FK lógica → `users.id` (Firebase UID)            |
| `title`       | TEXT    | NOT NULL        | Nombre del viaje                                 |
| `startDate`   | TEXT    | NOT NULL        | Fecha inicio en formato `dd/MM/yyyy`             |
| `endDate`     | TEXT    | NOT NULL        | Fecha fin en formato `dd/MM/yyyy`                |
| `description` | TEXT    | NOT NULL        | Descripción del viaje                            |
| `totalBudget` | REAL    | DEFAULT 0.0     | Presupuesto total en euros                       |
| `spentBudget` | REAL    | DEFAULT 0.0     | Presupuesto ya gastado                           |
| `createdAt`   | INTEGER | NOT NULL        | Timestamp Unix (millis) de creación              |

> **T4.2** — La consulta principal filtra por `userId` (`getTripsByUser`), de modo que cada usuario solo ve sus propios viajes.

---

#### Tabla `activities`

| Columna       | Tipo    | Restricción     | Descripción                                      |
|---------------|---------|-----------------|--------------------------------------------------|
| `id`          | TEXT    | PRIMARY KEY     | UUID generado en la capa de dominio              |
| `tripId`      | TEXT    | NOT NULL        | FK lógica → `trips.id`                           |
| `title`       | TEXT    | NOT NULL        | Nombre de la actividad                           |
| `description` | TEXT    | NOT NULL        | Descripción                                      |
| `date`        | TEXT    | NOT NULL        | Fecha en formato `dd/MM/yyyy`                    |
| `time`        | TEXT    | NOT NULL        | Hora en formato `HH:mm`                          |
| `cost`        | REAL    | NOT NULL        | Coste económico de la actividad                  |
| `type`        | TEXT    | NOT NULL        | Nombre del enum `PlanType` (ej. `EXPLORATION`)   |
| `createdAt`   | INTEGER | NOT NULL        | Timestamp Unix (millis) — campo datetime         |

---

#### Tabla `users` *(T4.1 — nuevo en Sprint 03)*

| Columna        | Tipo    | Restricción       | Descripción                                        |
|----------------|---------|-------------------|----------------------------------------------------|
| `id`           | TEXT    | PRIMARY KEY       | Firebase UID (sincronizado con Firebase Auth)      |
| `email`        | TEXT    | NOT NULL          | Login — email de Firebase                          |
| `username`     | TEXT    | NOT NULL, UNIQUE* | Nombre de usuario único en la app                  |
| `birthdate`    | INTEGER | NOT NULL          | Fecha de nacimiento — timestamp Unix (millis)      |
| `address`      | TEXT    | DEFAULT ''        | Dirección postal                                   |
| `country`      | TEXT    | DEFAULT ''        | País de residencia                                 |
| `phone`        | TEXT    | DEFAULT ''        | Teléfono de contacto                               |
| `acceptEmails` | INTEGER | DEFAULT 0         | Booleano: acepta recibir emails (0=no, 1=sí)       |
| `createdAt`    | INTEGER | NOT NULL          | Timestamp Unix (millis) de creación del perfil     |

> *La unicidad del username se comprueba en la capa repositorio mediante `UserDao.countByUsername()` antes de guardar, para retornar un error de negocio claro en lugar de depender de una constraint de BD.

---

#### Tabla `access_log` *(T4.4 — nuevo en Sprint 03)*

| Columna     | Tipo    | Restricción            | Descripción                                         |
|-------------|---------|------------------------|-----------------------------------------------------|
| `id`        | INTEGER | PRIMARY KEY AUTOINCREMENT | Identificador auto-incremental                   |
| `userId`    | TEXT    | NOT NULL               | FK lógica → `users.id`                              |
| `event`     | TEXT    | NOT NULL               | Tipo de evento: `"LOGIN"` o `"LOGOUT"`              |
| `timestamp` | INTEGER | NOT NULL               | Timestamp Unix (millis) del evento                  |

> Cada vez que el usuario inicia o cierra sesión, se inserta una nueva fila. Esto permite auditar el historial completo de accesos.

---

### 🔄 Estrategia de migración

La base de datos pasó de la versión **1 → 2** añadiendo las tablas `users` y `access_log`. La migración se define en `AppDatabase.MIGRATION_1_2` y se ejecuta automáticamente mediante Room al detectar la versión anterior. No se modificó el esquema de `trips` ni `activities` en esta migración.

```kotlin
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `users` (...)")
        database.execSQL("CREATE TABLE IF NOT EXISTS `access_log` (...)")
    }
}
```

---

### 🧩 Patrón de acceso a datos (Repository + DAO)

El acceso a la base de datos sigue el patrón **Repository** con inyección de dependencias via **Hilt**:

```
UI / ViewModel
    └── Repository (interfaz en domain/)
            └── RepositoryImpl (en data/repository/)
                    └── DAO (en data/local/db/dao/)
                            └── Room / SQLite
```

Los mappers en `data/local/db/mapper/` se encargan de convertir entre las entidades de Room (`*Entity`) y los modelos de dominio puro, manteniendo la capa de dominio libre de dependencias de Android.

| Repositorio                  | DAO usado         | Tabla          |
|------------------------------|-------------------|----------------|
| `TripRepositoryImpl`         | `TripDao`         | `trips`        |
| `ActivityRepositoryImpl`     | `ActivityDao`     | `activities`   |
| `UserRepositoryImpl`         | `UserDao`         | `users`        |
| `AccessLogRepositoryImpl`    | `AccessLogDao`    | `access_log`   |

---

### ✅ Validaciones implementadas (T5.2)

Las validaciones se aplican **antes** de llegar al DAO, en los ViewModels y en los repositorios:

| Validación                              | Dónde se aplica             |
|-----------------------------------------|-----------------------------|
| Título, fecha inicio y fin no vacíos    | `TripListViewModel`         |
| Fecha fin no anterior a fecha inicio    | `TripListViewModel`         |
| Fecha de actividad dentro del viaje     | `ActivityViewModel`         |
| Título, fecha y hora de actividad vacíos | `ActivityViewModel`        |
| Username único (no duplicado)           | `UserRepositoryImpl` + `UserDao.countByUsername()` |
| Nombre de viaje duplicado para usuario  | `TripRepositoryImpl.addTrip()` con `checkDuplicateTripName()` |

---

### 🪵 Logging con Logcat (T5.3)

Todos los repositorios y ViewModels utilizan `android.util.Log` con etiquetas descriptivas para rastrear las operaciones de base de datos:

| TAG                        | Nivel   | Cuándo                                       |
|----------------------------|---------|----------------------------------------------|
| `TripRepositoryImpl`       | `D`     | Insert, update, query por usuario            |
| `TripRepositoryImpl`       | `W`     | Delete, nombre duplicado detectado           |
| `TripRepositoryImpl`       | `E`     | Error al insertar por nombre duplicado       |
| `ActivityRepositoryImpl`   | `D`     | Insert, update, delete de actividades        |
| `UserRepositoryImpl`       | `D`     | Guardar/actualizar usuario, búsqueda         |
| `AccessLogRepositoryImpl`  | `D`     | Registro de login/logout                     |
| `TripListViewModel`        | `D`/`E` | Validaciones, cambio de usuario activo       |
| `AuthViewModel`            | `D`/`E` | Login, logout, registro Firebase             |

Ejemplo de filtro en Logcat de Android Studio:
```
tag:TripRepositoryImpl | tag:TripListViewModel | tag:AuthViewModel
```
