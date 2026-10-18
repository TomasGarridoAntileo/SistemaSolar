package cl.duoc.misolar.ui.panel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.misolar.data.AppDatabase
import cl.duoc.misolar.data.InstalacionRepository
import cl.duoc.misolar.data.UsuarioRepository
import cl.duoc.misolar.model.Instalacion
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PanelViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val usuarioRepository = UsuarioRepository(database.usuarioDao(), database.sesionDao())
    private val instalacionRepository = InstalacionRepository(database.instalacionDao())

    private val usuarioId = MutableStateFlow<Int?>(null)

    // Clase 05: el Flow de Room se convierte en StateFlow con stateIn.
    // Cuando se guarda o edita la instalacion, el Panel se actualiza solo.
    @OptIn(ExperimentalCoroutinesApi::class)
    val instalacion: StateFlow<Instalacion?> = usuarioId
        .flatMapLatest { id ->
            if (id == null) flowOf(null) else instalacionRepository.observar(id)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    init {
        viewModelScope.launch {
            usuarioId.value = usuarioRepository.obtenerUsuarioConSesion()?.id
        }
    }
}
