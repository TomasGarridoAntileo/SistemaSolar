package cl.duoc.misolar.ui.produccion

import cl.duoc.misolar.util.hoyTexto

// La fecha parte con el dia de hoy
data class NuevaLecturaUiState(
    val fecha: String = hoyTexto(),
    val kwh: String = "",
    val nota: String = "",

    val errorFecha: String? = null,
    val errorKwh: String? = null,
    val errorNota: String? = null,
    val errorGeneral: String? = null,

    // Maximo de kWh al dia segun la instalacion
    val maximoKwh: Double? = null,
    val guardando: Boolean = false,
    val guardadoExitoso: Boolean = false
)
