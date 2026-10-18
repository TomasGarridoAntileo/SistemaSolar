package cl.duoc.misolar.data

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import cl.duoc.misolar.model.Usuario

// QUE operaciones se pueden hacer sobre la tabla usuarios
@Dao
interface UsuarioDao {

    @Insert
    suspend fun insertar(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun buscarPorCorreo(correo: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): Usuario?
}
