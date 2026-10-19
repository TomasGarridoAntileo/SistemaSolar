package cl.duoc.misolar.ui.mantencion

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.misolar.data.AppDatabase
import cl.duoc.misolar.data.MantencionRepository
import cl.duoc.misolar.data.UsuarioRepository
import cl.duoc.misolar.model.Mantencion
import cl.duoc.misolar.model.TipoMantencion
import cl.duoc.misolar.util.borrarFoto
import cl.duoc.misolar.util.copiarAFotos
import cl.duoc.misolar.util.fechaAIso
import cl.duoc.misolar.util.validarDescripcion
import cl.duoc.misolar.util.validarFechaPasada
import cl.duoc.misolar.util.validarFoto
import cl.duoc.misolar.util.validarTipoMantencion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NuevaMantencionViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val usuarioRepository = UsuarioRepository(database.usuarioDao(), database.sesionDao())
    private val mantencionRepository = MantencionRepository(database.mantencionDao())

    private val _uiState = MutableStateFlow(NuevaMantencionUiState())
    val uiState: StateFlow<NuevaMantencionUiState> = _uiState.asStateFlow()

    private var usuarioId: Int? = null

    init {
        viewModelScope.launch {
            usuarioId = usuarioRepository.obtenerUsuarioConSesion()?.id
        }
    }

    fun cambiarTipo(tipo: TipoMantencion) {
        _uiState.update { it.copy(tipo = tipo, errorTipo = null) }
    }

    fun cambiarFecha(valor: String) {
        _uiState.update {
            it.copy(
                fecha = valor,
                errorFecha = if (it.errorFecha != null) validarFechaPasada(valor) else null
            )
        }
    }

    fun cambiarDescripcion(valor: String) {
        _uiState.update {
            it.copy(
                descripcion = valor,
                errorDescripcion = if (it.errorDescripcion != null) validarDescripcion(valor) else null
            )
        }
    }

    fun ponerFotoCamara(uri: Uri) {
        reemplazarFoto(uri.toString())
    }

    // Copiar la imagen puede demorar, por eso va en Dispatchers.IO
    fun ponerFotoGaleria(uri: Uri) {
        viewModelScope.launch {
            val copia = withContext(Dispatchers.IO) { copiarAFotos(getApplication<Application>(), uri) }
            if (copia != null) reemplazarFoto(copia.toString())
        }
    }

    fun quitarFoto() {
        borrarFoto(getApplication<Application>(), _uiState.value.fotoUri)
        _uiState.update { it.copy(fotoUri = null) }
    }

    // Si ya habia una foto se borra la anterior
    private fun reemplazarFoto(nueva: String) {
        borrarFoto(getApplication<Application>(), _uiState.value.fotoUri)
        _uiState.update { it.copy(fotoUri = nueva, errorFoto = null) }
    }

    fun guardar() {
        val estado = _uiState.value
        val errorTipo = validarTipoMantencion(estado.tipo?.name)
        val errorFecha = validarFechaPasada(estado.fecha)
        val errorDescripcion = validarDescripcion(estado.descripcion)
        val errorFoto = validarFoto(estado.fotoUri)

        _uiState.update {
            it.copy(
                errorTipo = errorTipo,
                errorFecha = errorFecha,
                errorDescripcion = errorDescripcion,
                errorFoto = errorFoto
            )
        }

        val formularioValido = listOf(errorTipo, errorFecha, errorDescripcion, errorFoto).all { it == null }
        val id = usuarioId
        val tipo = estado.tipo
        val fechaIso = fechaAIso(estado.fecha)
        val foto = estado.fotoUri
        if (!formularioValido || id == null || tipo == null || fechaIso == null || foto == null) return

        viewModelScope.launch {
            _uiState.update { it.copy(guardando = true) }
            mantencionRepository.guardar(
                Mantencion(
                    usuarioId = id,
                    tipo = tipo.name,
                    fecha = fechaIso,
                    descripcion = estado.descripcion.trim(),
                    fotoUri = foto
                )
            )
            _uiState.update { it.copy(guardando = false, guardadoExitoso = true) }
        }
    }
}
