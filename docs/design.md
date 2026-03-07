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