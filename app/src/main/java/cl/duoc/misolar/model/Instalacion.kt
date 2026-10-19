package cl.duoc.misolar.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey

// Una instalacion por usuario
@Entity(tableName = "instalaciones")
data class Instalacion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val usuarioId: Int,
    val nombre: String,
    val cantidadPaneles: Int,
    val potenciaPanelW: Int,
    // Formato dd/MM/yyyy
    val fechaInstalacion: String,
    val precioKwh: Int,
    // Ubicacion GPS, opcional
    val latitud: Double? = null,
    val longitud: Double? = null
) {
    // Ej: 10 paneles de 400 W = 4 kW
    val potenciaTotalKw: Double
        get() = cantidadPaneles * potenciaPanelW / 1000.0
}
