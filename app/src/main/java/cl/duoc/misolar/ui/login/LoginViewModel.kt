package cl.duoc.misolar.ui.login

import androidx.lifecycle.ViewModel
import cl.duoc.misolar.util.validarCorreo
import cl.duoc.misolar.util.validarPassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Clase 05: el ViewModel expone un unico UiState mediante StateFlow.
// Solo el ViewModel puede modificarlo (_uiState es privado); la pantalla solo lo lee.
class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun cambiarCorreo(nuevoCorreo: String) {
        _uiState.update { estadoActual ->
            estadoActual.copy(
                correo = nuevoCorreo,
                // Si el campo ya mostraba un error, se vuelve a validar mientras el usuario corrige
                errorCorreo = if (estadoActual.errorCorreo != null) validarCorreo(nuevoCorreo) else null
            )
        }
    }

    fun cambiarPassword(nuevoPassword: String) {
        _uiState.update { estadoActual ->
            estadoActual.copy(
                password = nuevoPassword,
                errorPassword = if (estadoActual.errorPassword != null) validarPasswordLogin(nuevoPassword) else null
            )
        }
    }

    // Devuelve true si el formulario es valido.
    // En la etapa 4 tambien se revisara que el usuario exista en Room.
    fun ingresar(): Boolean {
        val estado = _uiState.value
        val errorCorreo = validarCorreo(estado.correo)
        val errorPassword = validarPasswordLogin(estado.password)

        _uiState.update { estadoActual ->
            estadoActual.copy(
                errorCorreo = errorCorreo,
                errorPassword = errorPassword
            )
        }
        return errorCorreo == null && errorPassword == null
    }

    // En el login solo se pide que no este vacia; las reglas completas se aplican al registrarse
    private fun validarPasswordLogin(password: String): String? {
        return if (password.isEmpty()) validarPassword(password) else null
    }
}
