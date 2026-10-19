package cl.duoc.misolar.util

import kotlin.math.roundToInt

// Horas de sol pico promedio al dia en Chile central
const val HORAS_SOL_PICO = 4.0

enum class NivelRendimiento(val mensaje: String) {
    SIN_DATOS("Aún no hay lecturas este mes"),
    BAJO("Rendimiento bajo: revisa o limpia tus paneles"),
    NORMAL("Rendimiento normal"),
    EXCELENTE("¡Rendimiento excelente!")
}

// Porcentaje de lo producido contra lo esperado (potencia x horas de sol pico x dias)
fun calcularRendimiento(kwhMes: Double, dias: Int, potenciaKw: Double): Int? {
    if (dias <= 0 || potenciaKw <= 0) return null
    val esperado = potenciaKw * HORAS_SOL_PICO * dias
    return (kwhMes / esperado * 100).roundToInt()
}

fun nivelRendimiento(porcentaje: Int?): NivelRendimiento {
    return when {
        porcentaje == null -> NivelRendimiento.SIN_DATOS
        porcentaje >= 80 -> NivelRendimiento.EXCELENTE
        porcentaje >= 50 -> NivelRendimiento.NORMAL
        else -> NivelRendimiento.BAJO
    }
}
