package cl.duoc.misolar.ui.login

// Clase 05: "fotografia" de todo lo que muestra la pantalla de Login.
// Se usa val para que el estado sea inmutable: cada cambio crea una copia con copy()
data class LoginUiState(
    val correo: String = "",
    val password: String = "",
    val errorCorreo: String? = null,
    val errorPassword: String? = null,
    val errorGeneral: String? = null,
    val cargando: Boolean = false,
    val loginExitoso: Boolean = false
)
