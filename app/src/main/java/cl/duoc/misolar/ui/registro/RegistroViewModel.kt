package cl.duoc.misolar.ui.registro

import androidx.lifecycle.ViewModel
import cl.duoc.misolar.util.validarApellido
import cl.duoc.misolar.util.validarCorreo
import cl.duoc.misolar.util.validarNombre
import cl.duoc.misolar.util.validarPassword
import cl.duoc.misolar.util.validarRepetirPassword
import cl.duoc.misolar.util.validarTerminos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Clase 05: UiState + StateFlow. La pantalla lee uiState y llama a estas funciones.
class RegistroViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()

    // En cada cambio, si el campo ya tenia error se valida de nuevo para que el error
    // desaparezca apenas el usuario lo corrige
    fun cambiarNombre(nuevoNombre: String) {
        _uiState.update { estadoActual ->
            estadoActual.copy(
                nombre = nuevoNombre,
                errorNombre = if (estadoActual.errorNombre != null) validarNombre(nuevoNombre) else null
            )
        }
    }

    fun cambiarApellido(nuevoApellido: String) {
        _uiState.update { estadoActual ->
            estadoActual.copy(
                apellido = nuevoApellido,
                errorApellido = if (estadoActual.errorApellido != null) validarApellido(nuevoApellido) else null
            )
        }
    }

    fun cambiarCorreo(nuevoCorreo: String) {
        _uiState.update { estadoActual ->
            estadoActual.copy(
                correo = nuevoCorreo,
                errorCorreo = if (estadoActual.errorCorreo != null) validarCorreo(nuevoCorreo) else null
            )
        }
    }

    fun cambiarPassword(nuevoPassword: String) {
        _uiState.update { estadoActual ->
            estadoActual.copy(
                password = nuevoPassword,
                errorPassword = if (estadoActual.errorPassword != null) validarPassword(nuevoPassword) else null,
                errorRepetirPassword = if (estadoActual.errorRepetirPassword != null) {
                    validarRepetirPassword(nuevoPassword, estadoActual.repetirPassword)
                } else {
                    null
                }
            )
        }
    }

    fun cambiarRepetirPassword(nuevoRepetirPassword: String) {
        _uiState.update { estadoActual ->
            estadoActual.copy(
                repetirPassword = nuevoRepetirPassword,
                errorRepetirPassword = if (estadoActual.errorRepetirPassword != null) {
                    validarRepetirPassword(estadoActual.password, nuevoRepetirPassword)
                } else {
                    null
                }
            )
        }
    }

    fun cambiarTerminos(nuevoTerminos: Boolean) {
        _uiState.update { estadoActual ->
            estadoActual.copy(
                aceptaTerminos = nuevoTerminos,
                errorTerminos = if (estadoActual.errorTerminos != null) validarTerminos(nuevoTerminos) else null
            )
        }
    }

    // Valida todo el formulario y, si esta correcto, marca el registro como exitoso.
    // En la etapa 4 aqui se guardara el usuario en Room.
    fun registrar() {
        val estado = _uiState.value
        val errorNombre = validarNombre(estado.nombre)
        val errorApellido = validarApellido(estado.apellido)
        val errorCorreo = validarCorreo(estado.correo)
        val errorPassword = validarPassword(estado.password)
        val errorRepetirPassword = validarRepetirPassword(estado.password, estado.repetirPassword)
        val errorTerminos = validarTerminos(estado.aceptaTerminos)

        val formularioValido = listOf(
            errorNombre,
            errorApellido,
            errorCorreo,
            errorPassword,
            errorRepetirPassword,
            errorTerminos
        ).all { it == null }

        _uiState.update { estadoActual ->
            estadoActual.copy(
                errorNombre = errorNombre,
                errorApellido = errorApellido,
                errorCorreo = errorCorreo,
                errorPassword = errorPassword,
                errorRepetirPassword = errorRepetirPassword,
                errorTerminos = errorTerminos,
                registroExitoso = formularioValido
            )
        }
    }

    // Se llama despues de mostrar el mensaje de exito: limpia el formulario
    fun reiniciar() {
        _uiState.value = RegistroUiState()
    }
}
