package cl.duoc.misolar.ui.perfil

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.misolar.data.AppDatabase
import cl.duoc.misolar.data.UsuarioRepository
import cl.duoc.misolar.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PerfilViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val repository = UsuarioRepository(database.usuarioDao(), database.sesionDao())

    private val _usuario = MutableStateFlow<Usuario?>(null)
    val usuario: StateFlow<Usuario?> = _usuario.asStateFlow()

    init {
        viewModelScope.launch {
            _usuario.value = repository.obtenerUsuarioConSesion()
        }
    }

    // Borra la sesion en Room y despues avisa a la pantalla para que navegue al Login
    fun cerrarSesion(alTerminar: () -> Unit) {
        viewModelScope.launch {
            repository.cerrarSesion()
            alTerminar()
        }
    }
}
