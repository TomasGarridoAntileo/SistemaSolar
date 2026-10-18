package cl.duoc.misolar.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey

// Registro de mantencion de los paneles (se usa en la etapa 8)
@Entity(tableName = "mantenciones")
data class Mantencion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val usuarioId: Int,
    // LIMPIEZA, REVISION o REPARACION (ver TipoMantencion)
    val tipo: String,
    // Fecha en formato yyyy-MM-dd
    val fecha: String,
    val descripcion: String,
    val fotoUri: String
)
