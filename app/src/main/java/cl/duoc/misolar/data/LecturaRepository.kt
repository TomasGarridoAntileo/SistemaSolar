package cl.duoc.misolar.data

import cl.duoc.misolar.model.Lectura
import kotlinx.coroutines.flow.Flow

class LecturaRepository(private val lecturaDao: LecturaDao) {

    fun observar(usuarioId: Int): Flow<List<Lectura>> {
        return lecturaDao.observarPorUsuario(usuarioId)
    }

    fun observarKwhDelMes(usuarioId: Int, mes: String): Flow<Double> {
        return lecturaDao.observarKwhDelMes(usuarioId, mes)
    }

    // Una lectura por dia
    suspend fun existeFecha(usuarioId: Int, fecha: String): Boolean {
        return lecturaDao.contarPorFecha(usuarioId, fecha) > 0
    }

    suspend fun guardar(lectura: Lectura) {
        lecturaDao.insertar(lectura)
    }

    suspend fun eliminar(lectura: Lectura) {
        lecturaDao.eliminar(lectura)
    }
}
