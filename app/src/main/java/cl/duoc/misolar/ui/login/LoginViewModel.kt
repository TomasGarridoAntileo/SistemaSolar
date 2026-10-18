package cl.duoc.misolar.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.misolar.data.AppDatabase
import cl.duoc.misolar.data.UsuarioRepository
import cl.duoc.misolar.util.validarCorreo
import cl.duoc.misolar.util.validarPassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Clase 05: AndroidViewModel porque necesita el Context para abrir la base de datos
class LoginViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val repository = UsuarioRepository(database.usuarioDao(), database.sesionDao())

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun cambiarCorreo(nuevoCorreo: String) {
        _uiState.update { estadoActual ->
            estadoActual.copy(
                correo = nuevoCorreo,
                // Si el campo ya mostraba un error, se vuelve a validar mientras el usuario corrige
                errorCorreo = if (estadoActual.errorCorreo != null) validarCorreo(nuevoCorreo) else null,
                errorGeneral = null
            )
        }
    }

    fun cambiarPassword(nuevoPassword: String) {
        _uiState.update { estadoActual ->
            estadoActual.copy(
                password = nuevoPassword,
                errorPassword = if (estadoActual.errorPassword != null) validarPasswordLogin(nuevoPassword) else null,
                errorGeneral = null
            )
        }
    }

    fun ingresar() {
        val estado = _uiState.value
        val errorCorreo = validarCorreo(estado.correo)
        val errorPassword = validarPasswordLogin(estado.password)

        _uiState.update { estadoActual ->
            estadoActual.copy(
                errorCorreo = errorCorreo,
                errorPassword = errorPassword
            )
        }
        if (errorCorreo != null || errorPassword != null) return

        // Consultar la base de datos se hace en una corrutina para no bloquear la pantalla
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true) }
            val usuario = repository.autenticar(estado.correo, estado.password)
            if (usuario != null) {
                repository.iniciarSesion(usuario)
                _uiState.update { it.copy(cargando = false, loginExitoso = true) }
            } else {
                _uiState.update {
                    it.copy(cargando = false, errorGeneral = "Correo o contraseña incorrectos")
                }
            }
        }
    }

    // Se llama despues de navegar al Panel, para que al volver el formulario este limpio
    fun reiniciar() {
        _uiState.value = LoginUiState()
    }

    // En el login solo se pide que no este vacia; las reglas completas se aplican al registrarse
    private fun validarPasswordLogin(password: String): String? {
        return if (password.isEmpty()) validarPassword(password) else null
    }
}
