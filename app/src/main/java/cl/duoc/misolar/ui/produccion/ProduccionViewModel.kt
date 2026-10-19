package cl.duoc.misolar.ui.produccion

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.misolar.data.AppDatabase
import cl.duoc.misolar.data.LecturaRepository
import cl.duoc.misolar.data.UsuarioRepository
import cl.duoc.misolar.model.Lectura
import cl.duoc.misolar.util.borrarFoto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProduccionViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val usuarioRepository = UsuarioRepository(database.usuarioDao(), database.sesionDao())
    private val lecturaRepository = LecturaRepository(database.lecturaDao())

    private val usuarioId = MutableStateFlow<Int?>(null)

    // Clase 05: la lista se actualiza sola cuando cambia la tabla
    @OptIn(ExperimentalCoroutinesApi::class)
    val lecturas: StateFlow<List<Lectura>> = usuarioId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList()) else lecturaRepository.observar(id)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Lectura que se quiere borrar, muestra el dialogo de confirmacion
    private val _lecturaAEliminar = MutableStateFlow<Lectura?>(null)
    val lecturaAEliminar: StateFlow<Lectura?> = _lecturaAEliminar.asStateFlow()

    init {
        viewModelScope.launch {
            usuarioId.value = usuarioRepository.obtenerUsuarioConSesion()?.id
        }
    }

    fun pedirEliminar(lectura: Lectura) {
        _lecturaAEliminar.value = lectura
    }

    fun cancelarEliminar() {
        _lecturaAEliminar.value = null
    }

    fun confirmarEliminar() {
        val lectura = _lecturaAEliminar.value ?: return
        viewModelScope.launch {
            lecturaRepository.eliminar(lectura)
            borrarFoto(getApplication<Application>(), lectura.fotoUri)
            _lecturaAEliminar.value = null
        }
    }
}
