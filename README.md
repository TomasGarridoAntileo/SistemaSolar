# MiSolar ☀️

App Android para dueños de paneles solares domésticos: registra tu instalación, anota la producción diaria (kWh) con foto del inversor, lleva el historial de mantenciones y revisa el ahorro estimado del mes.

Proyecto de la **Evaluación Parcial 2** de *DSY1105 Desarrollo de Aplicaciones Móviles* (Duoc UC).

**Autor:** Tomás (trabajo individual)

## Funcionalidades

| Funcionalidad | Estado |
|---|---|
| Tema Material 3 con paleta solar | ✅ |
| Navegación entre pantallas con barra inferior, flecha de volver y transiciones animadas | ✅ |
| Login y registro con validaciones por campo (ícono y mensaje) | ✅ |
| Persistencia local con Room (usuarios y sesión) | ✅ |
| Registro y edición de la instalación (paneles, potencia, fecha, precio kWh) | ✅ |
| Ubicación GPS de la instalación, con permiso pedido al momento de usarla | ✅ |
| Lecturas de producción diaria: lista, nueva lectura validada y eliminar con confirmación | ✅ |
| Foto del inversor en la lectura (cámara con FileProvider / galería con Photo Picker, se muestra con Coil) | ✅ |
| Historial de mantenciones (limpieza, revisión, reparación) con foto obligatoria y eliminar con confirmación | ✅ |
| Panel con kWh del mes y ahorro estimado | ✅ |
| Animaciones: Crossfade en la instalación, color del rendimiento con animateColorAsState y detalles con AnimatedVisibility | ✅ |

Los requisitos completos están en [docs/requisitos-alcance.md](docs/requisitos-alcance.md).

## Tecnologías

Kotlin 2.2.10 · Jetpack Compose (BOM 2026.02.01) · Material 3 · Navigation Compose 2.9.8 · Room 3.0.3 + KSP 2.3.4 · Coil 3.3.0 · AGP 9.0.1 · Gradle 9.5.0 · minSdk 24 / targetSdk 36.

Arquitectura **MVVM**: pantallas Compose (`ui`), ViewModels con `UiState` + `StateFlow`, `Repository` + Room (`data`), entidades (`model`) y validaciones (`util`).

## Cómo ejecutar

1. Clonar el repositorio o descargar el ZIP.
2. Abrir Android Studio y elegir **File > Open**, seleccionando la carpeta `MiSolar`.
3. Esperar la sincronización de Gradle (necesita internet la primera vez). Si aparece el aviso, presionar **Sync Now**.
4. Crear un emulador **Pixel 8 con API 36 (Android 16)** en *Device Manager*.
5. Presionar **Run ▶** con el módulo `app` seleccionado.
