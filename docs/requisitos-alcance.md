# MiSolar: requisitos y alcance

**Asignatura:** DSY1105 Desarrollo de Aplicaciones Móviles · Evaluación Parcial 2 (encargo)
**Autor:** Tomás (trabajo individual, autorizado por el docente)
**Plataforma:** Android, Kotlin + Jetpack Compose (Material 3)

## 1. Problemática

Cada vez más hogares instalan paneles solares, pero la mayoría de los dueños no lleva un control de cuánto producen ni de cuándo limpiarlos o revisarlos. Un panel sucio o con fallas puede perder una parte importante de su rendimiento sin que nadie lo note, y el dueño tampoco sabe cuánto está ahorrando realmente.

## 2. Objetivo

Desarrollar una app móvil que permita al dueño de una instalación solar doméstica registrar su instalación, anotar la producción diaria de energía, llevar un historial de mantenciones con evidencia fotográfica y ver un resumen del ahorro estimado, todo guardado en el dispositivo.

## 3. Alcance

### 3.1 Incluido en esta entrega (EP2)
- Registro e inicio de sesión de usuarios locales (sin servidor).
- Registro de **una** instalación solar por usuario (cantidad de paneles, potencia, fecha, precio del kWh y ubicación GPS).
- Registro, listado y eliminación de lecturas de producción diaria (kWh), con foto opcional del inversor.
- Registro, listado y eliminación de mantenciones (limpieza, revisión, reparación) con foto obligatoria.
- Panel principal con kWh del mes, ahorro estimado en CLP y nivel de luz actual medido con el sensor del teléfono.
- Persistencia local con Room (SQLite).
- Recursos nativos: cámara, galería (Photo Picker), ubicación GPS y sensor de luz.
- Animaciones de retroalimentación y transiciones entre pantallas.

### 3.2 Fuera de alcance (posibles mejoras para la EFT)
- Sincronización con un servidor o API externa (backend, nube).
- Conexión directa con el inversor (Bluetooth / Wi-Fi) para leer la producción automáticamente.
- Varias instalaciones por usuario.
- Gráficos históricos avanzados y exportación a PDF/Excel.
- Notificaciones programadas de recordatorio de limpieza.
- Pronóstico del clima o radiación solar por internet.
- Cifrado de contraseñas y recuperación de cuenta.

### 3.3 Supuestos y restricciones
- La app funciona sin internet; todos los datos quedan en el dispositivo.
- La producción la ingresa el usuario a mano (leyendo el inversor).
- El ahorro es una estimación: kWh × precio del kWh ingresado por el usuario.
- Debe compilar y ejecutarse con las versiones de herramientas usadas en el curso (ver sección 6).

## 4. Requisitos funcionales

| ID | Requisito | Prioridad |
|---|---|---|
| RF01 | El usuario puede registrarse con nombre, apellido, correo, contraseña, repetir contraseña y aceptar términos. | Alta |
| RF02 | El sistema valida cada campo del registro y muestra un ícono y un mensaje de error bajo el campo inválido (correo con `@` y dominio válido, contraseña de 8 o más caracteres, contraseñas iguales, términos aceptados). | Alta |
| RF03 | El sistema no permite registrar dos usuarios con el mismo correo. | Alta |
| RF04 | El usuario puede iniciar sesión con correo y contraseña; si son incorrectos se muestra un mensaje claro. | Alta |
| RF05 | La sesión se mantiene al cerrar y abrir la app, y el usuario puede cerrarla desde Perfil. | Media |
| RF06 | El usuario puede registrar y editar su instalación: nombre, cantidad de paneles (1 a 100), potencia por panel en W (100 a 800), fecha de instalación (no futura) y precio del kWh en CLP (mayor a 0), todo validado por campo. | Alta |
| RF07 | Al registrar la instalación, el usuario puede obtener su ubicación GPS; la app pide el permiso y explica qué pasa si lo rechaza. | Alta |
| RF08 | El usuario puede registrar una lectura de producción con fecha (no futura ni repetida), kWh (mayor a 0 y coherente con la potencia instalada), nota opcional (máx. 100 caracteres) y foto opcional. | Alta |
| RF09 | El usuario puede ver sus lecturas en una lista ordenada por fecha, y eliminar una con confirmación. | Alta |
| RF10 | El usuario puede registrar una mantención con tipo (Limpieza, Revisión, Reparación), fecha, descripción (10 o más caracteres) y foto obligatoria. | Alta |
| RF11 | El usuario puede ver el historial de mantenciones con su foto, y eliminar una con confirmación. | Media |
| RF12 | La foto de una lectura o mantención se puede tomar con la cámara o elegir desde la galería, con vista previa y opción de quitarla. | Alta |
| RF13 | El panel principal muestra los kWh producidos en el mes, el ahorro estimado en CLP y los días desde la última limpieza. | Alta |
| RF14 | El panel principal muestra el nivel de luz actual (lux) leído del sensor del teléfono, con un indicador animado y un mensaje (baja, media o alta producción esperada). Si el teléfono no tiene sensor, se informa sin fallar. | Media |
| RF15 | La app navega entre Login, Registro, Panel, Instalación, Producción, Nueva lectura, Mantenciones, Nueva mantención y Perfil, con barra inferior y botón de volver. | Alta |

## 5. Requisitos no funcionales

| ID | Categoría | Requisito |
|---|---|---|
| RNF01 | Usabilidad | La interfaz usa Material 3 (TopAppBar, NavigationBar, Card, OutlinedTextField, FAB), con jerarquía tipográfica y una paleta de colores consistente (amarillo solar y azul). |
| RNF02 | Usabilidad | Todo error se comunica en pantalla, nunca solo por consola; cada campo indica su estado con ícono y texto. |
| RNF03 | Usabilidad | Las animaciones (aparición de errores, vista previa de foto, indicador de luz, transiciones de pantalla) duran entre 200 y 600 ms y aportan retroalimentación. |
| RNF04 | Arquitectura | El proyecto sigue MVVM: pantallas Compose (View), ViewModels con UiState expuesto por StateFlow, y Repository + Room como modelo de datos. |
| RNF05 | Mantenibilidad | Las validaciones están en `util/Validaciones.kt` y en los ViewModels, no dentro de los Composables. |
| RNF06 | Mantenibilidad | El código se organiza en paquetes por responsabilidad: `model`, `data`, `ui/<pantalla>`, `util`. |
| RNF07 | Persistencia | Los datos se guardan con Room y se mantienen al cerrar la app y reiniciar el teléfono. Las escrituras usan corrutinas (`suspend`) y las lecturas `Flow`. |
| RNF08 | Rendimiento | La UI no se bloquea al guardar o leer datos; las listas usan `LazyColumn`. |
| RNF09 | Robustez | El estado de los formularios sobrevive a la rotación de pantalla (ViewModel / `rememberSaveable`). |
| RNF10 | Seguridad | La app pide solo los permisos necesarios (ubicación) y en el momento en que se usan. La galería usa Photo Picker, sin permiso de almacenamiento, y la cámara usa FileProvider, sin exponer rutas de archivos. |
| RNF11 | Seguridad | El sensor de luz se registra solo mientras la pantalla está visible y se libera al salir. |
| RNF12 | Compatibilidad | Funciona desde Android 7.0 (API 24) hasta Android 16 (API 36), probado en el emulador Pixel 8 API 36. |
| RNF13 | Portabilidad | Compila en los computadores del instituto con las mismas versiones de Android Studio, Gradle y librerías usadas en clases. |
| RNF14 | Control de versiones | El código está en un repositorio público de GitHub con commits frecuentes y mensajes técnicos. |
| RNF15 | Documentación | El README describe el proyecto, autor, funcionalidades y pasos para ejecutar. |

## 6. Entorno de desarrollo (mismas versiones del curso)

Tomadas del proyecto del profesor `primer-mvvm-actividad03` (`gradle/libs.versions.toml` y `gradle-wrapper.properties`), que es el que corre en los computadores del instituto:

| Herramienta | Versión |
|---|---|
| Android Gradle Plugin (AGP) | 9.0.1 |
| Gradle (wrapper) | 9.5.0 |
| Kotlin | 2.2.10 |
| KSP | 2.3.4 |
| Compose BOM | 2026.02.01 |
| Navigation Compose | 2.9.8 |
| Room 3 (`androidx.room3`) | 3.0.3 |
| Coil 3 (`coil-compose`) | 3.3.0 |
| Java (compileOptions) | 11 |
| compileSdk / targetSdk / minSdk | 36 / 36 / 24 |
| Emulador | Pixel 8, API 36 (Android 16) |

No se necesita ningún plugin ni extensión de Android Studio: todas las librerías se descargan con Gradle (*Sync Now*). La ubicación se obtiene con `LocationManager` y el sensor de luz con `SensorManager`, ambos del propio Android, para no agregar dependencias nuevas.
