package cl.duoc.misolar.data

import cl.duoc.misolar.model.Instalacion
import kotlinx.coroutines.flow.Flow

class InstalacionRepository(private val instalacionDao: InstalacionDao) {

    fun observar(usuarioId: Int): Flow<Instalacion?> {
        return instalacionDao.observarPorUsuario(usuarioId)
    }

    suspend fun obtener(usuarioId: Int): Instalacion? {
        return instalacionDao.buscarPorUsuario(usuarioId)
    }

    // Si el usuario ya tiene instalacion se actualiza; si no, se crea
    suspend fun guardar(instalacion: Instalacion) {
        val existente = instalacionDao.buscarPorUsuario(instalacion.usuarioId)
        if (existente == null) {
            instalacionDao.insertar(instalacion)
        } else {
            instalacionDao.actualizar(instalacion.copy(id = existente.id))
        }
    }
}
