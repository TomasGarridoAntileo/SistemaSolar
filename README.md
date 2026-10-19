# MiSolar ☀️

App Android para dueños de paneles solares domésticos. Permite registrar la instalación, anotar la producción diaria (kWh) con foto del inversor, llevar el historial de mantenciones con evidencia fotográfica y revisar el ahorro y el rendimiento del mes. Todo se guarda en el teléfono.

**Problema que resuelve:** muchas casas tienen paneles solares, pero sus dueños no saben cuánto producen ni cuándo limpiarlos, y pierden rendimiento sin darse cuenta.

Proyecto de la **Evaluación Parcial 2** de *DSY1105 Desarrollo de Aplicaciones Móviles* (Duoc UC).

**Autor:** Tomás Garrido (trabajo individual, autorizado por el docente)

## Funcionalidades

| Funcionalidad | Estado |
|---|---|
| Tema Material 3 con paleta solar (azul panel, amarillo sol, verde energía) | ✅ |
| Navegación con barra inferior, flecha de volver y transiciones animadas | ✅ |
| Registro e inicio de sesión con validaciones por campo (ícono y mensaje) | ✅ |
| Sesión guardada: al abrir la app de nuevo entra directo al Panel | ✅ |
| Registro y edición de la instalación (paneles, potencia, fecha, precio kWh) | ✅ |
| Ubicación GPS de la instalación, con permiso pedido al momento de usarla | ✅ |
| Lecturas de producción diaria: lista, nueva lectura validada y eliminar con confirmación | ✅ |
| Foto del inversor en la lectura (cámara o galería) | ✅ |
| Historial de mantenciones (limpieza, revisión, reparación) con foto obligatoria | ✅ |
| Panel con kWh del mes, ahorro estimado, rendimiento y días desde la última limpieza | ✅ |
| Animaciones con propósito (Crossfade, animateColorAsState, AnimatedVisibility) | ✅ |
| Pruebas unitarias de validaciones y cálculos (32 pruebas) | ✅ |

Los requisitos completos están en [docs/requisitos-alcance.md](docs/requisitos-alcance.md) y el plan en [docs/plan-app.md](docs/plan-app.md).

## Pantallas

| Pantalla | Qué hace |
|---|---|
| Login / Registro | Crear cuenta e ingresar. No permite correos repetidos. |
| Panel | Resumen de la instalación, producción y ahorro del mes, rendimiento y limpieza. |
| Instalación | Formulario de los paneles y botón de ubicación GPS. |
| Producción / Nueva lectura | Lista de lecturas diarias y formulario con foto opcional. |
| Mantenciones / Nueva mantención | Historial con foto y formulario con foto obligatoria. |
| Perfil | Datos del usuario y cerrar sesión. |

## Formularios y validaciones

Todas las reglas están en `util/Validaciones.kt`, fuera de los Composables. Cada campo muestra un ícono de error o un check, y el mensaje debajo.

| Formulario | Reglas |
|---|---|
| Registro | Nombre y apellido (solo letras), correo válido y no repetido, contraseña de 8 o más caracteres con un número, repetir contraseña, aceptar términos |
| Login | Correo válido y contraseña obligatoria; mensaje si los datos no coinciden |
| Instalación | Nombre (3 a 30), paneles (1 a 100), potencia (100 a 800 W), fecha dd/mm/aaaa no futura, precio kWh (1 a 1000 CLP) |
| Nueva lectura | Fecha no futura, no anterior a la instalación y no repetida; kWh mayor a 0 y menor que potencia × 12 h; nota de 100 caracteres máximo |
| Nueva mantención | Tipo obligatorio, fecha no futura, descripción de 10 a 200 caracteres, foto obligatoria |

## Recursos nativos

| Recurso | Cómo se usa | Permiso |
|---|---|---|
| Cámara | `TakePicture()` + `FileProvider` (`res/xml/file_paths.xml`) | No necesita: se usa la app de cámara del sistema |
| Galería | Photo Picker (`PickVisualMedia`) | No necesita: acceso mínimo |
| Ubicación GPS | `LocationManager` con `RequestMultiplePermissions()` | `ACCESS_FINE_LOCATION` y `ACCESS_COARSE_LOCATION`, pedidos solo al tocar el botón |

Las fotos se copian a la carpeta interna de la app y se muestran con Coil (`AsyncImage`).

## Arquitectura

Patrón **MVVM**:
- Pantallas Compose.
- ViewModels con `UiState` + `StateFlow`.
- Repositorios con Room.

```
cl/duoc/misolar/
├── MainActivity.kt
├── model/        Entidades de Room: Usuario, Sesion, Instalacion, Lectura, Mantencion, TipoMantencion
├── data/         AppDatabase (singleton), DAOs y Repositorios
├── ui/
│   ├── navegacion/   Rutas, NavHost y pantalla inicial según la sesión
│   ├── componentes/  BarraSuperior, BarraInferior, CampoValidado, SelectorFoto
│   ├── login/ registro/ panel/ instalacion/ produccion/ mantencion/ perfil/
│   └── theme/        Colores, tipografía y tema
└── util/         Validaciones, Formato, CalculosSolares, CamaraUtils, UbicacionUtils
```

## Tecnologías

Kotlin 2.2.10 · Jetpack Compose (BOM 2026.02.01) · Material 3 · Navigation Compose 2.9.8 · Room 3.0.3 + KSP 2.3.4 · Coil 3.3.0 · AGP 9.0.1 · Gradle 9.5.0 · minSdk 24 / targetSdk 36.

Son las mismas versiones de los proyectos del curso, para que funcione en los computadores del instituto. No necesita plugins de Android Studio.

## Cómo ejecutar

1. Clonar el repositorio o descargar el ZIP.
2. Abrir Android Studio, elegir **File > Open** y seleccionar la carpeta del proyecto.
3. Esperar la sincronización de Gradle, que necesita internet la primera vez. Si aparece el aviso, presionar **Sync Now**.
4. Crear un emulador **Pixel 8 con API 36** en *Device Manager*.
5. Presionar **Run ▶** con el módulo `app` seleccionado.

### Probar en el emulador

- **Cámara:** abre una sala virtual 3D. Se toma la foto y se confirma con ✓.
- **Galería:** si está vacía, primero se saca una foto con la app Cámara del emulador.
- **GPS:** en los **⋯** de la barra del emulador, ir a **Location**, elegir un punto y presionar **Set location**. Luego, en Instalación, tocar **Usar mi ubicación actual**.

### Ejecutar las pruebas

En Android Studio, clic derecho sobre `app/src/test/java/cl/duoc/misolar/util` y elegir **Run 'Tests in util'**.
