package cl.duoc.misolar.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey

// Datos de la instalacion solar del usuario (una por usuario)
@Entity(tableName = "instalaciones")
data class Instalacion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val usuarioId: Int,
    val nombre: String,
    val cantidadPaneles: Int,
    val potenciaPanelW: Int,
    // Fecha en formato dd/MM/yyyy, igual a como la escribe el usuario
    val fechaInstalacion: String,
    val precioKwh: Int,
    // Se completan en la etapa de GPS
    val latitud: Double? = null,
    val longitud: Double? = null
) {
    // Potencia total en kW: 10 paneles de 400 W = 4,0 kW
    val potenciaTotalKw: Double
        get() = cantidadPaneles * potenciaPanelW / 1000.0
}
