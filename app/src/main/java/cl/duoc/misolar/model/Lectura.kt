package cl.duoc.misolar.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey

// Lectura diaria de produccion (se usa en la etapa 6)
@Entity(tableName = "lecturas")
data class Lectura(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val usuarioId: Int,
    // Fecha en formato yyyy-MM-dd para poder ordenar y filtrar por mes en las consultas
    val fecha: String,
    val kwh: Double,
    val nota: String = "",
    val fotoUri: String? = null
)
