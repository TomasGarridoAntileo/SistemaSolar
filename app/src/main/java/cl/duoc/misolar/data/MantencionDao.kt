package cl.duoc.misolar.data

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import cl.duoc.misolar.model.Mantencion
import kotlinx.coroutines.flow.Flow

@Dao
interface MantencionDao {

    @Insert
    suspend fun insertar(mantencion: Mantencion)

    @Delete
    suspend fun eliminar(mantencion: Mantencion)

    // La mas reciente primero
    @Query("SELECT * FROM mantenciones WHERE usuarioId = :usuarioId ORDER BY fecha DESC, id DESC")
    fun observarPorUsuario(usuarioId: Int): Flow<List<Mantencion>>
}
