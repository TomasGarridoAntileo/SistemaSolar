package cl.duoc.misolar.ui.instalacion

// Los numeros se guardan como texto mientras se escriben
data class InstalacionUiState(
    val nombre: String = "",
    val cantidadPaneles: String = "",
    val potenciaPanel: String = "",
    val fechaInstalacion: String = "",
    val precioKwh: String = "",

    val errorNombre: String? = null,
    val errorCantidadPaneles: String? = null,
    val errorPotenciaPanel: String? = null,
    val errorFechaInstalacion: String? = null,
    val errorPrecioKwh: String? = null,

    // Ubicacion GPS (opcional)
    val latitud: Double? = null,
    val longitud: Double? = null,
    val buscandoUbicacion: Boolean = false,
    val mensajeUbicacion: String? = null,

    val esEdicion: Boolean = false,
    val guardando: Boolean = false,
    val guardadoExitoso: Boolean = false
) {
    val potenciaTotalKw: Double?
        get() {
            val paneles = cantidadPaneles.toIntOrNull() ?: return null
            val potencia = potenciaPanel.toIntOrNull() ?: return null
            return paneles * potencia / 1000.0
        }
}
