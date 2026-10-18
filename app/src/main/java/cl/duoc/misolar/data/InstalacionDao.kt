package cl.duoc.misolar.data

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import cl.duoc.misolar.model.Instalacion
import kotlinx.coroutines.flow.Flow

@Dao
interface InstalacionDao {

    @Insert
    suspend fun insertar(instalacion: Instalacion)

    @Update
    suspend fun actualizar(instalacion: Instalacion)

    // Flow: avisa cuando la instalacion cambia
    @Query("SELECT * FROM instalaciones WHERE usuarioId = :usuarioId LIMIT 1")
    fun observarPorUsuario(usuarioId: Int): Flow<Instalacion?>

    @Query("SELECT * FROM instalaciones WHERE usuarioId = :usuarioId LIMIT 1")
    suspend fun buscarPorUsuario(usuarioId: Int): Instalacion?
}
