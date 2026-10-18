package cl.duoc.misolar.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cl.duoc.misolar.util.validarCorreo
import cl.duoc.misolar.util.validarPassword

// Clase 04: el ViewModel guarda el estado del formulario y valida.
// La pantalla solo muestra los datos y avisa cuando el usuario escribe o presiona un boton.
class LoginViewModel : ViewModel() {

    var correo by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    var errorCorreo by mutableStateOf<String?>(null)
        private set
    var errorPassword by mutableStateOf<String?>(null)
        private set

    fun cambiarCorreo(nuevoCorreo: String) {
        correo = nuevoCorreo
        // Si el campo ya mostraba un error, se vuelve a validar mientras el usuario corrige
        if (errorCorreo != null) errorCorreo = validarCorreo(nuevoCorreo)
    }

    fun cambiarPassword(nuevoPassword: String) {
        password = nuevoPassword
        if (errorPassword != null) errorPassword = validarPasswordLogin(nuevoPassword)
    }

    // Devuelve true si el formulario es valido.
    // En la etapa 4 tambien se revisara que el usuario exista en Room.
    fun ingresar(): Boolean {
        errorCorreo = validarCorreo(correo)
        errorPassword = validarPasswordLogin(password)
        return errorCorreo == null && errorPassword == null
    }

    // En el login solo se pide que no este vacia; las reglas completas se aplican al registrarse
    private fun validarPasswordLogin(password: String): String? {
        return if (password.isEmpty()) validarPassword(password) else null
    }
}
