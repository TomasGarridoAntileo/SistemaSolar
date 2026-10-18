package cl.duoc.misolar.util

// Reglas de validacion de los formularios.
// Cada funcion devuelve el mensaje de error, o null si el dato es valido.
// Asi la pantalla solo muestra el mensaje y no decide nada.

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
