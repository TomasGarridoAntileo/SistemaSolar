# Encargo EP2 (DSY1105): ideas y plan de la app

## 1. Lo que pide la pauta (resumen para no olvidar nada)

| Indicador | Peso | Qué revisan |
|---|---|---|
| IE 2.1.1 Interfaz y navegación | 15% | Material 3, jerarquía visual, navegación fluida entre vistas |
| IE 2.1.2 Formularios | 15% | **Todos** los formularios, validación **por campo** con ícono + mensaje |
| IE 2.2.1 Lógica desacoplada | 10% | Validaciones fuera del Composable (ViewModel + `util/Validaciones.kt`) |
| IE 2.2.2 Animaciones | 10% | Que den fluidez o retroalimentación, no decorativas |
| IE 2.3.1 Modularidad + persistencia | 15% | Carpetas por responsabilidad, MVVM, Room funcionando |
| IE 2.3.2 GitHub + Trello | **20%** | Commits de **ambos** integrantes, técnicos y repartidos en las semanas; tareas en Trello |
| IE 2.4.1 Recursos nativos | 15% | Mínimo 2, con permisos bien manejados y coherentes con la UI |

Entrega: repo GitHub **público** (con acceso al docente), README (descripción, nombres, funcionalidades, pasos para ejecutar), mismo código subido al AVA grupal por un integrante, y cada uno activa el AVA individual. **No tocar el repo después de la fecha de entrega** (1.0 automático).

## 2. La app: MiSolar

Problemática: muchas casas tienen paneles solares, pero los dueños no saben cuánto producen ni cuándo limpiarlos o revisarlos, y pierden rendimiento sin darse cuenta. MiSolar permite registrar la instalación, anotar la producción diaria (kWh) con foto del inversor, llevar un historial de mantenciones y ver el ahorro estimado.

Recursos nativos (4, el mínimo es 2):
- **Cámara**: foto del display del inversor o del panel sucio/limpio (TakePicture + FileProvider, clase 06).
- **Galería**: elegir una foto ya tomada (Photo Picker, clase 06).
- **Ubicación GPS**: guardar dónde está la instalación (permiso en tiempo de ejecución).
- **Sensor de luz**: medir la luminosidad actual (lux) y decir si es buen momento de producción. Muy coherente con el tema y fácil de mostrar en la defensa.

> Trabajas solo: la pauta habla de parejas, así que conviene confirmar con el profe que acepta entrega individual y cómo evalúa la parte de "commits distribuidos".

## 3. Plan

### Pantallas y navegación
```
Login ──► Registro
  │
  ▼
Panel (resumen: kWh del mes, ahorro estimado, luz actual del sensor)
  ├──► Instalación (formulario: datos de los paneles + ubicación GPS)
  ├──► Producción (lista de lecturas) ──► NuevaLectura (formulario + foto)
  ├──► Mantenciones (lista) ──► NuevaMantencion (formulario + foto antes/después)
  └──► Perfil (datos del usuario, cerrar sesión)
```
- `Scaffold` + `TopAppBar` con flecha atrás (`popBackStack()`), `NavigationBar` inferior (Panel, Producción, Mantenciones, Perfil), `FloatingActionButton` "+" en las listas.
- Las pantallas reciben lambdas (`onIrANuevaLectura: () -> Unit`), no el `navController`, como enseñó el profe.

### Formularios y validaciones (todas en `util/Validaciones.kt`)
| Formulario | Campos y reglas |
|---|---|
| Login | correo (con `@`, termina en `.cl`), contraseña (≥ 8) |
| Registro | nombre, apellido, correo, contraseña, repetir contraseña (iguales), aceptar términos |
| Instalación | nombre (ej. "Casa"), cantidad de paneles (entero 1 a 100), potencia por panel en W (100 a 800), fecha de instalación (dd/mm/aaaa, no futura), precio kWh en CLP (> 0), ubicación (obligatoria) |
| Nueva lectura | fecha (no futura, no repetida), kWh producidos (número > 0 y razonable según la potencia instalada), foto del inversor (opcional), nota (máx. 100) |
| Nueva mantención | tipo (Limpieza, Revisión, Reparación), fecha, descripción (≥ 10), foto (obligatoria) |

Cada campo: `isError`, `supportingText` con el mensaje y `trailingIcon` (ícono de error rojo o check verde).

### Arquitectura (igual a `primer-mvvm-actividad03`)
```
cl/duoc/misolar/
  MainActivity.kt
  model/        Usuario.kt, Instalacion.kt, Lectura.kt, Mantencion.kt (@Entity), TipoMantencion.kt (enum)
  data/         AppDatabase.kt (singleton), UsuarioDao.kt, InstalacionDao.kt, LecturaDao.kt, MantencionDao.kt,
                y un Repository por cada uno
  ui/navegacion/Navegacion.kt
  ui/login/     LoginScreen.kt, LoginViewModel.kt, LoginUiState.kt
  ui/registro/  RegistroScreen.kt, RegistroViewModel.kt, RegistroUiState.kt
  ui/panel/     PanelScreen.kt, PanelViewModel.kt
  ui/instalacion/ InstalacionScreen.kt, InstalacionViewModel.kt, InstalacionUiState.kt
  ui/produccion/  ProduccionScreen.kt, NuevaLecturaScreen.kt, ProduccionViewModel.kt, NuevaLecturaUiState.kt
  ui/mantencion/  MantencionesScreen.kt, NuevaMantencionScreen.kt, MantencionViewModel.kt, NuevaMantencionUiState.kt
  ui/perfil/    PerfilScreen.kt
  ui/componentes/ CampoValidado.kt, TarjetaLectura.kt, SelectorFoto.kt
  ui/theme/     Color.kt (paleta amarillo solar + azul), Type.kt, Theme.kt
  util/         Validaciones.kt, CamaraUtils.kt, UbicacionUtils.kt, SensorLuz.kt, CalculosSolares.kt
```
- ViewModels con `MutableStateFlow` + `update { it.copy(...) }`, UI con `collectAsStateWithLifecycle()`.
- Room 3 + KSP. Lecturas con `Flow` + `stateIn`; el Panel suma kWh del mes con una `@Query` (`SUM(kwh)`).
- Ahorro estimado = kWh del mes × precio kWh (en `CalculosSolares.kt`, testeable).

### Recursos nativos
- Cámara: `TakePicture()` + FileProvider (`res/xml/file_paths.xml`), URI pendiente en `rememberSaveable`.
- Galería: `PickVisualMedia(ImageOnly)`, sin permiso de almacenamiento (acceso mínimo).
- Ubicación: `ACCESS_FINE_LOCATION` con `RequestPermission()`, mensaje claro si lo niegan.
- Sensor de luz: `SensorManager` + `Sensor.TYPE_LIGHT`, registrado en un `DisposableEffect` para soltarlo al salir. No necesita permiso. En el emulador se prueba en *Extended controls > Virtual sensors*.
- Fotos con Coil 3 (`AsyncImage`).

### Animaciones (cada una con propósito)
- Indicador de luz del sensor: `animateFloatAsState` en una barra o sol que crece con los lux, y `animateColorAsState` (gris → amarillo).
- `AnimatedVisibility` para mensajes de error y vista previa de la foto.
- `Crossfade` entre "sin foto" y "foto tomada".
- Transiciones entre pantallas en el `NavHost` (slide + fade).
- Contador animado del ahorro/kWh del mes en el Panel.

### Trello (o GitHub Projects)
Trello es un tablero gratis de tareas en tarjetas (trello.com). Crea un tablero "MiSolar" con columnas **Por hacer / En curso / Hecho** y estas tarjetas, moviéndolas a medida que avanzas (el profe revisa que haya actividad real, así que muévelas cuando de verdad hagas cada cosa):

| Semana | Tarjetas |
|---|---|
| 6 | Crear repo y proyecto base · Tema Material 3 · Diagrama de pantallas · README inicial |
| 7 | Navegación · Login y Registro · Validaciones.kt + UiState |
| 8 | Room (entidades, DAOs, repositorios) · Formulario Instalación |
| 9 | Producción: lista + nueva lectura · Cámara y galería |
| 10 | Mantenciones · Ubicación GPS · Sensor de luz · Animaciones |
| 11 | Pruebas en emulador · README final · Entrega AVA |

Alternativa: **GitHub Projects** (pestaña Projects del repo) sirve igual ("Trello o similar") y queda junto a los commits e issues.

Commits pequeños y técnicos, uno por avance, por ejemplo `feat(produccion): validar kWh segun potencia instalada` o `feat(sensor): leer TYPE_LIGHT en PanelScreen`.
