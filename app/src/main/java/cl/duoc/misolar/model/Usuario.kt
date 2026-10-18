package cl.duoc.misolar.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey

// Clase 05: cada @Entity es una tabla de la base de datos.
// No tiene repetirPassword ni aceptaTerminos porque esos datos son del formulario, no del usuario.
@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val apellido: String,
    val correo: String,
    val password: String
)
