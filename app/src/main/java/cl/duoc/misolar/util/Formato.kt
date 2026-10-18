package cl.duoc.misolar.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

// Room guarda la fecha como yyyy-MM-dd para ordenar, la pantalla muestra dd/MM/yyyy

fun fechaAIso(texto: String): String? {
    val fecha = convertirFecha(texto) ?: return null
    return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(fecha)
}

fun isoAFecha(iso: String): String {
    val partes = iso.split("-")
    if (partes.size != 3) return iso
    return "${partes[2]}/${partes[1]}/${partes[0]}"
}

fun hoyTexto(): String {
    return SimpleDateFormat("dd/MM/yyyy", Locale.US).format(Date())
}

// Ej: "2026-10" para buscar las lecturas del mes
fun mesActualIso(): String {
    return SimpleDateFormat("yyyy-MM", Locale.US).format(Date())
}

// Ahorro = kWh producidos x precio del kWh
fun calcularAhorro(kwh: Double, precioKwh: Int): Int {
    return (kwh * precioKwh).roundToInt()
}

// Ej: 12.5 -> "12,5"
fun formatearKwh(kwh: Double): String {
    return String.format(Locale.forLanguageTag("es-CL"), "%.1f", kwh)
}

// Ej: 15300 -> "$15.300"
fun formatearPesos(monto: Int): String {
    return "$" + String.format(Locale.forLanguageTag("es-CL"), "%,d", monto)
}
