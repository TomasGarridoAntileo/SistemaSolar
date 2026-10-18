package cl.duoc.misolar.data

import cl.duoc.misolar.model.Sesion
import cl.duoc.misolar.model.Usuario

// COMO se obtienen y guardan los usuarios. Los ViewModel hablan con el Repository, nunca con el DAO.
class UsuarioRepository(
    private val usuarioDao: UsuarioDao,
    private val sesionDao: SesionDao
) {

    suspend fun existeCorreo(correo: String): Boolean {
        return usuarioDao.buscarPorCorreo(normalizar(correo)) != null
    }

    suspend fun registrar(usuario: Usuario) {
        usuarioDao.insertar(usuario.copy(correo = normalizar(usuario.correo)))
    }

    // Devuelve el usuario si el correo y la contrasena coinciden; si no, null
    suspend fun autenticar(correo: String, password: String): Usuario? {
        val usuario = usuarioDao.buscarPorCorreo(normalizar(correo))
        return if (usuario != null && usuario.password == password) usuario else null
    }

    suspend fun iniciarSesion(usuario: Usuario) {
        sesionDao.borrar()
        sesionDao.insertar(Sesion(usuarioId = usuario.id))
    }

    suspend fun obtenerUsuarioConSesion(): Usuario? {
        val sesion = sesionDao.obtener() ?: return null
        return usuarioDao.buscarPorId(sesion.usuarioId)
    }

    suspend fun cerrarSesion() {
        sesionDao.borrar()
    }

    // Se guarda en minusculas y sin espacios para que "Tomas@Duoc.cl" y "tomas@duoc.cl" sean el mismo
    private fun normalizar(correo: String): String = correo.trim().lowercase()
}
