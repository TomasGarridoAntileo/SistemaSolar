package cl.duoc.misolar.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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

// Se recomienda limpiar los paneles cada 3 meses
const val DIAS_ENTRE_LIMPIEZAS = 90

// Dias entre una fecha yyyy-MM-dd y hoy
fun diasDesde(fechaIso: String, hoy: Date = Date()): Int? {
    val formato = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    formato.isLenient = false
    val fecha = try {
        formato.parse(fechaIso)
    } catch (e: ParseException) {
        null
    } ?: return null
    val milisegundosPorDia = 24 * 60 * 60 * 1000L
    return ((hoy.time - fecha.time) / milisegundosPorDia).toInt()
}
