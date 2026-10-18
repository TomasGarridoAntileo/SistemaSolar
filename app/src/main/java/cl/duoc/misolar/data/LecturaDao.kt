package cl.duoc.misolar.data

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import cl.duoc.misolar.model.Lectura
import kotlinx.coroutines.flow.Flow

@Dao
interface LecturaDao {

    @Insert
    suspend fun insertar(lectura: Lectura)

    @Delete
    suspend fun eliminar(lectura: Lectura)

    // La mas reciente primero
    @Query("SELECT * FROM lecturas WHERE usuarioId = :usuarioId ORDER BY fecha DESC")
    fun observarPorUsuario(usuarioId: Int): Flow<List<Lectura>>

    // Suma del mes, mes = "2026-10"
    @Query("SELECT COALESCE(SUM(kwh), 0) FROM lecturas WHERE usuarioId = :usuarioId AND fecha LIKE :mes || '%'")
    fun observarKwhDelMes(usuarioId: Int, mes: String): Flow<Double>

    @Query("SELECT COUNT(*) FROM lecturas WHERE usuarioId = :usuarioId AND fecha = :fecha")
    suspend fun contarPorFecha(usuarioId: Int, fecha: String): Int
}
