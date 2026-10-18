package cl.duoc.misolar.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// Clase 04: cada funcion devuelve el mensaje de error, o null si esta bien

private val REGEX_CORREO = Regex("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")
private val REGEX_NOMBRE = Regex("^[\\p{L} ]+$")

const val LARGO_MINIMO_PASSWORD = 8

fun validarNombre(nombre: String): String? {
    return when {
        nombre.isBlank() -> "El nombre es obligatorio"
        nombre.trim().length < 2 -> "Debe tener al menos 2 letras"
        !REGEX_NOMBRE.matches(nombre.trim()) -> "Solo se permiten letras"
        else -> null
    }
}

fun validarApellido(apellido: String): String? {
    return when {
        apellido.isBlank() -> "El apellido es obligatorio"
        apellido.trim().length < 2 -> "Debe tener al menos 2 letras"
        !REGEX_NOMBRE.matches(apellido.trim()) -> "Solo se permiten letras"
        else -> null
    }
}

fun validarCorreo(correo: String): String? {
    return when {
        correo.isBlank() -> "El correo es obligatorio"
        !correo.contains("@") -> "Falta el @"
        !REGEX_CORREO.matches(correo.trim()) -> "Ingrese un correo válido, por ejemplo nombre@duocuc.cl"
        else -> null
    }
}

fun validarPassword(password: String): String? {
    return when {
        password.isEmpty() -> "La contraseña es obligatoria"
        password.length < LARGO_MINIMO_PASSWORD -> "Debe contener al menos $LARGO_MINIMO_PASSWORD caracteres"
        !password.any { it.isDigit() } -> "Debe incluir al menos un número"
        else -> null
    }
}

fun validarRepetirPassword(password: String, repetirPassword: String): String? {
    return when {
        repetirPassword.isEmpty() -> "Repita la contraseña"
        password != repetirPassword -> "Las contraseñas no coinciden"
        else -> null
    }
}

fun validarTerminos(aceptaTerminos: Boolean): String? {
    return if (aceptaTerminos) null else "Debe aceptar los términos y condiciones"
}

// Formulario de instalacion

fun validarNombreInstalacion(nombre: String): String? {
    return when {
        nombre.isBlank() -> "Ponle un nombre, por ejemplo Casa"
        nombre.trim().length < 3 -> "Debe tener al menos 3 caracteres"
        nombre.trim().length > 30 -> "Máximo 30 caracteres"
        else -> null
    }
}

// Numero entero dentro de un rango
private fun validarEnteroEnRango(texto: String, campo: String, minimo: Int, maximo: Int, unidad: String = ""): String? {
    val numero = texto.trim().toIntOrNull()
    return when {
        texto.isBlank() -> "$campo es obligatorio"
        numero == null -> "Ingrese solo números enteros"
        numero < minimo || numero > maximo -> "Debe estar entre $minimo y $maximo$unidad"
        else -> null
    }
}

fun validarCantidadPaneles(texto: String): String? =
    validarEnteroEnRango(texto, "La cantidad de paneles", 1, 100)

fun validarPotenciaPanel(texto: String): String? =
    validarEnteroEnRango(texto, "La potencia", 100, 800, " W")

fun validarPrecioKwh(texto: String): String? =
    validarEnteroEnRango(texto, "El precio del kWh", 1, 1000, " CLP")

// Clase 04: fecha con formato dd/mm/aaaa
fun convertirFecha(texto: String): Date? {
    if (!Regex("^\\d{2}/\\d{2}/\\d{4}$").matches(texto.trim())) return null
    val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    formato.isLenient = false // rechaza fechas como 31/02
    return try {
        formato.parse(texto.trim())
    } catch (e: ParseException) {
        null
    }
}

fun validarFechaPasada(texto: String, hoy: Date = Date()): String? {
    if (texto.isBlank()) return "La fecha es obligatoria"
    val fecha = convertirFecha(texto) ?: return "Use el formato dd/mm/aaaa con una fecha real"
    val calendario = Calendar.getInstance().apply { time = fecha }
    return when {
        calendario.get(Calendar.YEAR) < 2000 -> "La fecha debe ser desde el año 2000"
        fecha.after(hoy) -> "La fecha no puede ser futura"
        else -> null
    }
}
