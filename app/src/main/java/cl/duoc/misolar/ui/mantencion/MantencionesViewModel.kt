package cl.duoc.misolar.ui.mantencion

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.misolar.data.AppDatabase
import cl.duoc.misolar.data.MantencionRepository
import cl.duoc.misolar.data.UsuarioRepository
import cl.duoc.misolar.model.Mantencion
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

class MantencionesViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val usuarioRepository = UsuarioRepository(database.usuarioDao(), database.sesionDao())
    private val mantencionRepository = MantencionRepository(database.mantencionDao())

    private val usuarioId = MutableStateFlow<Int?>(null)

    // Clase 05: la lista se actualiza sola cuando cambia la tabla
    @OptIn(ExperimentalCoroutinesApi::class)
    val mantenciones: StateFlow<List<Mantencion>> = usuarioId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList()) else mantencionRepository.observar(id)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Mantencion que se quiere borrar, muestra el dialogo de confirmacion
    private val _mantencionAEliminar = MutableStateFlow<Mantencion?>(null)
    val mantencionAEliminar: StateFlow<Mantencion?> = _mantencionAEliminar.asStateFlow()

    init {
        viewModelScope.launch {
            usuarioId.value = usuarioRepository.obtenerUsuarioConSesion()?.id
        }
    }

    fun pedirEliminar(mantencion: Mantencion) {
        _mantencionAEliminar.value = mantencion
    }

    fun cancelarEliminar() {
        _mantencionAEliminar.value = null
    }

    fun confirmarEliminar() {
        val mantencion = _mantencionAEliminar.value ?: return
        viewModelScope.launch {
            mantencionRepository.eliminar(mantencion)
            borrarFoto(getApplication<Application>(), mantencion.fotoUri)
            _mantencionAEliminar.value = null
        }
    }
}
