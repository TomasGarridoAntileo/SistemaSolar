package cl.duoc.misolar.data

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import cl.duoc.misolar.model.Sesion

@Dao
interface SesionDao {

    @Insert
    suspend fun insertar(sesion: Sesion)

    @Query("SELECT * FROM sesion WHERE id = 1 LIMIT 1")
    suspend fun obtener(): Sesion?

    @Query("DELETE FROM sesion")
    suspend fun borrar()
}
