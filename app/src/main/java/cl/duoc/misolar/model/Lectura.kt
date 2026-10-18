package cl.duoc.misolar.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey

// Produccion diaria en kWh
@Entity(tableName = "lecturas")
data class Lectura(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val usuarioId: Int,
    // Formato yyyy-MM-dd para ordenar y filtrar por mes
    val fecha: String,
    val kwh: Double,
    val nota: String = "",
    val fotoUri: String? = null
)
