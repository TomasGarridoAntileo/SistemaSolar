package cl.duoc.misolar.ui.produccion

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.misolar.data.AppDatabase
import cl.duoc.misolar.data.InstalacionRepository
import cl.duoc.misolar.data.LecturaRepository
import cl.duoc.misolar.data.UsuarioRepository
import cl.duoc.misolar.model.Instalacion
import cl.duoc.misolar.model.Lectura
import cl.duoc.misolar.util.HORAS_SOL_MAXIMAS
import cl.duoc.misolar.util.borrarFoto
import cl.duoc.misolar.util.copiarAFotos
import cl.duoc.misolar.util.convertirKwh
import cl.duoc.misolar.util.fechaAIso
import cl.duoc.misolar.util.validarFechaLectura
import cl.duoc.misolar.util.validarKwh
import cl.duoc.misolar.util.validarNota
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val SIN_INSTALACION = "Primero registra tu instalación en el Panel"

class NuevaLecturaViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val usuarioRepository = UsuarioRepository(database.usuarioDao(), database.sesionDao())
    private val instalacionRepository = InstalacionRepository(database.instalacionDao())
    private val lecturaRepository = LecturaRepository(database.lecturaDao())

    private val _uiState = MutableStateFlow(NuevaLecturaUiState())
    val uiState: StateFlow<NuevaLecturaUiState> = _uiState.asStateFlow()

    private var usuarioId: Int? = null
    private var instalacion: Instalacion? = null

    // Se necesita la instalacion para validar los kWh
    init {
        viewModelScope.launch {
            val usuario = usuarioRepository.obtenerUsuarioConSesion() ?: return@launch
            usuarioId = usuario.id
            val datos = instalacionRepository.obtener(usuario.id)
            instalacion = datos
            _uiState.update {
                it.copy(
                    maximoKwh = datos?.let { inst -> inst.potenciaTotalKw * HORAS_SOL_MAXIMAS },
                    errorGeneral = if (datos == null) SIN_INSTALACION else null
                )
            }
        }
    }

    private fun validarFechaActual(texto: String): String? {
        return validarFechaLectura(texto, instalacion?.fechaInstalacion ?: "")
    }

    private fun validarKwhActual(texto: String): String? {
        val potencia = instalacion?.potenciaTotalKw ?: return null
        return validarKwh(texto, potencia)
    }

    fun cambiarFecha(valor: String) {
        _uiState.update {
            it.copy(
                fecha = valor,
                errorFecha = if (it.errorFecha != null) validarFechaActual(valor) else null
            )
        }
    }

    fun cambiarKwh(valor: String) {
        _uiState.update {
            it.copy(
                kwh = valor,
                errorKwh = if (it.errorKwh != null) validarKwhActual(valor) else null
            )
        }
    }

    fun cambiarNota(valor: String) {
        _uiState.update {
            it.copy(nota = valor, errorNota = validarNota(valor))
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
        _uiState.update { it.copy(fotoUri = nueva) }
    }

    fun guardar() {
        val estado = _uiState.value
        val id = usuarioId
        if (instalacion == null || id == null) {
            _uiState.update { it.copy(errorGeneral = SIN_INSTALACION) }
            return
        }

        val errorFecha = validarFechaActual(estado.fecha)
        val errorKwh = validarKwhActual(estado.kwh)
        val errorNota = validarNota(estado.nota)

        _uiState.update {
            it.copy(errorFecha = errorFecha, errorKwh = errorKwh, errorNota = errorNota)
        }

        val fechaIso = fechaAIso(estado.fecha)
        val kwh = convertirKwh(estado.kwh)
        if (errorFecha != null || errorKwh != null || errorNota != null) return
        if (fechaIso == null || kwh == null) return

        viewModelScope.launch {
            _uiState.update { it.copy(guardando = true) }

            // Solo una lectura por dia
            if (lecturaRepository.existeFecha(id, fechaIso)) {
                _uiState.update {
                    it.copy(guardando = false, errorFecha = "Ya registraste una lectura para este día")
                }
                return@launch
            }

            lecturaRepository.guardar(
                Lectura(
                    usuarioId = id,
                    fecha = fechaIso,
                    kwh = kwh,
                    nota = estado.nota.trim(),
                    fotoUri = estado.fotoUri
                )
            )
            _uiState.update { it.copy(guardando = false, guardadoExitoso = true) }
        }
    }
}
