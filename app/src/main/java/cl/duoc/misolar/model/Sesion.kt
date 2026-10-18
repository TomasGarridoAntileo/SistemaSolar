package cl.duoc.misolar.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey

// Usuario que inicio sesion (una sola fila)
@Entity(tableName = "sesion")
data class Sesion(
    @PrimaryKey
    val id: Int = 1,
    val usuarioId: Int
)
