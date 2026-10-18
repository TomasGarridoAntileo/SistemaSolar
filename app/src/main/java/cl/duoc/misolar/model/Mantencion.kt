package cl.duoc.misolar.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "mantenciones")
data class Mantencion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val usuarioId: Int,
    // Nombre de TipoMantencion
    val tipo: String,
    // Formato yyyy-MM-dd
    val fecha: String,
    val descripcion: String,
    val fotoUri: String
)
