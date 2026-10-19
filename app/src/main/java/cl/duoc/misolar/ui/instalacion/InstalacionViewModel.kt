package cl.duoc.misolar.ui.instalacion

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.misolar.data.AppDatabase
import cl.duoc.misolar.data.InstalacionRepository
import cl.duoc.misolar.data.UsuarioRepository
import cl.duoc.misolar.model.Instalacion
import cl.duoc.misolar.util.pedirUbicacion
import cl.duoc.misolar.util.validarCantidadPaneles
import cl.duoc.misolar.util.validarFechaPasada
import cl.duoc.misolar.util.validarNombreInstalacion
import cl.duoc.misolar.util.validarPotenciaPanel
import cl.duoc.misolar.util.validarPrecioKwh
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InstalacionViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val usuarioRepository = UsuarioRepository(database.usuarioDao(), database.sesionDao())
    private val instalacionRepository = InstalacionRepository(database.instalacionDao())

    private val _uiState = MutableStateFlow(InstalacionUiState())
    val uiState: StateFlow<InstalacionUiState> = _uiState.asStateFlow()

    private var usuarioId: Int? = null

    // Si ya hay instalacion, se cargan sus datos para editar
    init {
        viewModelScope.launch {
            val usuario = usuarioRepository.obtenerUsuarioConSesion() ?: return@launch
            usuarioId = usuario.id
            val instalacion = instalacionRepository.obtener(usuario.id) ?: return@launch
            _uiState.update {
                it.copy(
                    nombre = instalacion.nombre,
                    cantidadPaneles = instalacion.cantidadPaneles.toString(),
                    potenciaPanel = instalacion.potenciaPanelW.toString(),
                    fechaInstalacion = instalacion.fechaInstalacion,
                    precioKwh = instalacion.precioKwh.toString(),
                    latitud = instalacion.latitud,
                    longitud = instalacion.longitud,
                    esEdicion = true
                )
            }
        }
    }

    fun cambiarNombre(valor: String) {
        _uiState.update {
            it.copy(
                nombre = valor,
                errorNombre = if (it.errorNombre != null) validarNombreInstalacion(valor) else null
            )
        }
    }

    fun cambiarCantidadPaneles(valor: String) {
        _uiState.update {
            it.copy(
                cantidadPaneles = valor,
                errorCantidadPaneles = if (it.errorCantidadPaneles != null) validarCantidadPaneles(valor) else null
            )
        }
    }

    fun cambiarPotenciaPanel(valor: String) {
        _uiState.update {
            it.copy(
                potenciaPanel = valor,
                errorPotenciaPanel = if (it.errorPotenciaPanel != null) validarPotenciaPanel(valor) else null
            )
        }
    }

    fun cambiarFechaInstalacion(valor: String) {
        _uiState.update {
            it.copy(
                fechaInstalacion = valor,
                errorFechaInstalacion = if (it.errorFechaInstalacion != null) validarFechaPasada(valor) else null
            )
        }
    }

    fun cambiarPrecioKwh(valor: String) {
        _uiState.update {
            it.copy(
                precioKwh = valor,
                errorPrecioKwh = if (it.errorPrecioKwh != null) validarPrecioKwh(valor) else null
            )
        }
    }

    // Se llama despues de que el usuario acepta el permiso
    fun buscarUbicacion() {
        _uiState.update { it.copy(buscandoUbicacion = true, mensajeUbicacion = null) }
        pedirUbicacion(getApplication<Application>()) { ubicacion ->
            _uiState.update {
                if (ubicacion == null) {
                    it.copy(
                        buscandoUbicacion = false,
                        mensajeUbicacion = "No se pudo obtener la ubicación. Revisa que el GPS esté encendido."
                    )
                } else {
                    it.copy(
                        buscandoUbicacion = false,
                        latitud = ubicacion.latitude,
                        longitud = ubicacion.longitude
                    )
                }
            }
        }
    }

    fun permisoUbicacionRechazado() {
        _uiState.update {
            it.copy(mensajeUbicacion = "Sin el permiso no podemos guardar dónde están tus paneles. Puedes darlo después en Ajustes, o guardar sin ubicación.")
        }
    }

    fun guardar() {
        val estado = _uiState.value
        val errorNombre = validarNombreInstalacion(estado.nombre)
        val errorCantidad = validarCantidadPaneles(estado.cantidadPaneles)
        val errorPotencia = validarPotenciaPanel(estado.potenciaPanel)
        val errorFecha = validarFechaPasada(estado.fechaInstalacion)
        val errorPrecio = validarPrecioKwh(estado.precioKwh)

        _uiState.update {
            it.copy(
                errorNombre = errorNombre,
                errorCantidadPaneles = errorCantidad,
                errorPotenciaPanel = errorPotencia,
                errorFechaInstalacion = errorFecha,
                errorPrecioKwh = errorPrecio
            )
        }

        val formularioValido = listOf(errorNombre, errorCantidad, errorPotencia, errorFecha, errorPrecio)
            .all { it == null }
        val id = usuarioId
        if (!formularioValido || id == null) return

        viewModelScope.launch {
            _uiState.update { it.copy(guardando = true) }
            instalacionRepository.guardar(
                Instalacion(
                    usuarioId = id,
                    nombre = estado.nombre.trim(),
                    cantidadPaneles = estado.cantidadPaneles.trim().toInt(),
                    potenciaPanelW = estado.potenciaPanel.trim().toInt(),
                    fechaInstalacion = estado.fechaInstalacion.trim(),
                    precioKwh = estado.precioKwh.trim().toInt(),
                    latitud = estado.latitud,
                    longitud = estado.longitud
                )
            )
            _uiState.update { it.copy(guardando = false, guardadoExitoso = true) }
        }
    }
}
