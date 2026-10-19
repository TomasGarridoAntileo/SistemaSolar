package cl.duoc.misolar.ui.mantencion

import cl.duoc.misolar.model.TipoMantencion
import cl.duoc.misolar.util.hoyTexto

data class NuevaMantencionUiState(
    val tipo: TipoMantencion? = null,
    val fecha: String = hoyTexto(),
    val descripcion: String = "",
    val fotoUri: String? = null,

    val errorTipo: String? = null,
    val errorFecha: String? = null,
    val errorDescripcion: String? = null,
    val errorFoto: String? = null,

    val guardando: Boolean = false,
    val guardadoExitoso: Boolean = false
)
