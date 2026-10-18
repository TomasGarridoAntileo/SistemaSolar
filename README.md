# MiSolar ☀️

App Android para dueños de paneles solares domésticos: registra tu instalación, anota la producción diaria (kWh) con foto del inversor, lleva el historial de mantenciones y revisa el ahorro estimado del mes.

Proyecto de la **Evaluación Parcial 2** de *DSY1105 Desarrollo de Aplicaciones Móviles* (Duoc UC).

**Autor:** Tomás (trabajo individual)

## Funcionalidades

| Funcionalidad | Estado |
|---|---|
| Tema Material 3 con paleta solar | ✅ |
| Navegación entre pantallas con barra inferior, flecha de volver y transiciones animadas | ✅ |
| Login y registro con validaciones por campo | ⏳ |
| Persistencia local con Room | ⏳ |
| Registro de la instalación con ubicación GPS | ⏳ |
| Lecturas de producción con foto (cámara / galería) | ⏳ |
| Historial de mantenciones con foto | ⏳ |
| Panel con kWh del mes, ahorro estimado y sensor de luz | ⏳ |

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
