package cl.duoc.misolar.ui.panel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.misolar.data.AppDatabase
import cl.duoc.misolar.data.InstalacionRepository
import cl.duoc.misolar.data.LecturaRepository
import cl.duoc.misolar.data.UsuarioRepository
import cl.duoc.misolar.model.Instalacion
import cl.duoc.misolar.util.mesActualIso
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
    private val lecturaRepository = LecturaRepository(database.lecturaDao())

    private val usuarioId = MutableStateFlow<Int?>(null)

    // Clase 05: Flow de Room convertido en StateFlow con stateIn
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

    // Suma de kWh del mes actual
    @OptIn(ExperimentalCoroutinesApi::class)
    val kwhDelMes: StateFlow<Double> = usuarioId
        .flatMapLatest { id ->
            if (id == null) flowOf(0.0) else lecturaRepository.observarKwhDelMes(id, mesActualIso())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    // Dias del mes con lectura, para calcular el rendimiento
    @OptIn(ExperimentalCoroutinesApi::class)
    val diasDelMes: StateFlow<Int> = usuarioId
        .flatMapLatest { id ->
            if (id == null) flowOf(0) else lecturaRepository.observarDiasDelMes(id, mesActualIso())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    init {
        viewModelScope.launch {
            usuarioId.value = usuarioRepository.obtenerUsuarioConSesion()?.id
        }
    }
}
