package cl.duoc.misolar.ui.navegacion

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.misolar.data.AppDatabase
import cl.duoc.misolar.data.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Decide la primera pantalla: si hay sesion guardada va directo al Panel
class InicioViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val usuarioRepository = UsuarioRepository(database.usuarioDao(), database.sesionDao())

    // null mientras se revisa la base de datos
    private val _rutaInicial = MutableStateFlow<String?>(null)
    val rutaInicial: StateFlow<String?> = _rutaInicial.asStateFlow()

    init {
        viewModelScope.launch {
            val usuario = usuarioRepository.obtenerUsuarioConSesion()
            _rutaInicial.value = if (usuario != null) Rutas.PANEL else Rutas.LOGIN
        }
    }
}
