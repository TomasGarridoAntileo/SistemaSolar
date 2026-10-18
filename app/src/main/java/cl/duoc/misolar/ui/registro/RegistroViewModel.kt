package cl.duoc.misolar.ui.registro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cl.duoc.misolar.util.validarApellido
import cl.duoc.misolar.util.validarCorreo
import cl.duoc.misolar.util.validarNombre
import cl.duoc.misolar.util.validarPassword
import cl.duoc.misolar.util.validarRepetirPassword
import cl.duoc.misolar.util.validarTerminos

// Clase 04: el ViewModel mantiene los datos del formulario, sus errores y la accion registrar()
class RegistroViewModel : ViewModel() {

    var nombre by mutableStateOf("")
        private set
    var apellido by mutableStateOf("")
        private set
    var correo by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var repetirPassword by mutableStateOf("")
        private set
    var aceptaTerminos by mutableStateOf(false)
        private set

    var errorNombre by mutableStateOf<String?>(null)
        private set
    var errorApellido by mutableStateOf<String?>(null)
        private set
    var errorCorreo by mutableStateOf<String?>(null)
        private set
    var errorPassword by mutableStateOf<String?>(null)
        private set
    var errorRepetirPassword by mutableStateOf<String?>(null)
        private set
    var errorTerminos by mutableStateOf<String?>(null)
        private set

    // En cada cambio, si el campo ya tenia error se valida de nuevo para que el error
    // desaparezca apenas el usuario lo corrige
    fun cambiarNombre(nuevoNombre: String) {
        nombre = nuevoNombre
        if (errorNombre != null) errorNombre = validarNombre(nuevoNombre)
    }

    fun cambiarApellido(nuevoApellido: String) {
        apellido = nuevoApellido
        if (errorApellido != null) errorApellido = validarApellido(nuevoApellido)
    }

    fun cambiarCorreo(nuevoCorreo: String) {
        correo = nuevoCorreo
        if (errorCorreo != null) errorCorreo = validarCorreo(nuevoCorreo)
    }

    fun cambiarPassword(nuevoPassword: String) {
        password = nuevoPassword
        if (errorPassword != null) errorPassword = validarPassword(nuevoPassword)
        if (errorRepetirPassword != null) {
            errorRepetirPassword = validarRepetirPassword(nuevoPassword, repetirPassword)
        }
    }

    fun cambiarRepetirPassword(nuevoRepetirPassword: String) {
        repetirPassword = nuevoRepetirPassword
        if (errorRepetirPassword != null) {
            errorRepetirPassword = validarRepetirPassword(password, nuevoRepetirPassword)
        }
    }

    fun cambiarTerminos(nuevoTerminos: Boolean) {
        aceptaTerminos = nuevoTerminos
        if (errorTerminos != null) errorTerminos = validarTerminos(nuevoTerminos)
    }

    // Valida todo el formulario. Devuelve true si se puede registrar.
    // En la etapa 4 aqui se guardara el usuario en Room.
    fun registrar(): Boolean {
        errorNombre = validarNombre(nombre)
        errorApellido = validarApellido(apellido)
        errorCorreo = validarCorreo(correo)
        errorPassword = validarPassword(password)
        errorRepetirPassword = validarRepetirPassword(password, repetirPassword)
        errorTerminos = validarTerminos(aceptaTerminos)

        return listOf(
            errorNombre,
            errorApellido,
            errorCorreo,
            errorPassword,
            errorRepetirPassword,
            errorTerminos
        ).all { it == null }
    }
}
