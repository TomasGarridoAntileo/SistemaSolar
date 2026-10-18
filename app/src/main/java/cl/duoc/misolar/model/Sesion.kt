package cl.duoc.misolar.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey

// Guarda que usuario inicio sesion. Siempre hay como maximo una fila (id = 1).
@Entity(tableName = "sesion")
data class Sesion(
    @PrimaryKey
    val id: Int = 1,
    val usuarioId: Int
)
