package cl.duoc.misolar.data

import cl.duoc.misolar.model.Mantencion
import kotlinx.coroutines.flow.Flow

class MantencionRepository(private val mantencionDao: MantencionDao) {

    fun observar(usuarioId: Int): Flow<List<Mantencion>> {
        return mantencionDao.observarPorUsuario(usuarioId)
    }

    suspend fun guardar(mantencion: Mantencion) {
        mantencionDao.insertar(mantencion)
    }

    suspend fun eliminar(mantencion: Mantencion) {
        mantencionDao.eliminar(mantencion)
    }
}
