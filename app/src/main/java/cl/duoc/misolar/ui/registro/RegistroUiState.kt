package cl.duoc.misolar.ui.registro

// Clase 05: estado completo del formulario de registro.
// repetirPassword y aceptaTerminos son del formulario, no del usuario,
// por eso viven aqui y no en el modelo Usuario (etapa 4).
data class RegistroUiState(
    val nombre: String = "",
    val apellido: String = "",
    val correo: String = "",
    val password: String = "",
    val repetirPassword: String = "",
    val aceptaTerminos: Boolean = false,

    val errorNombre: String? = null,
    val errorApellido: String? = null,
    val errorCorreo: String? = null,
    val errorPassword: String? = null,
    val errorRepetirPassword: String? = null,
    val errorTerminos: String? = null,

    val registroExitoso: Boolean = false
)
