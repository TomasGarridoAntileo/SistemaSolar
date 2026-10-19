package cl.duoc.misolar.util

import org.junit.Assert.assertEquals
import org.junit.Test

// Pruebas de conversion de fechas y montos
class FormatoTest {

    @Test
    fun fechaSeConvierteAIsoYDeVuelta() {
        assertEquals("2024-03-15", fechaAIso("15/03/2024"))
        assertEquals("15/03/2024", isoAFecha("2024-03-15"))
    }

    @Test
    fun ahorroEsKwhPorPrecio() {
        assertEquals(1530, calcularAhorro(10.2, 150))
    }

    @Test
    fun pesosConPuntoDeMiles() {
        assertEquals("$15.300", formatearPesos(15300))
    }

    @Test
    fun rendimientoSegunLoEsperado() {
        // 4 kW x 4 horas x 2 dias = 32 kWh esperados
        assertEquals(100, calcularRendimiento(32.0, 2, 4.0))
        assertEquals(NivelRendimiento.EXCELENTE, nivelRendimiento(100))
        assertEquals(NivelRendimiento.BAJO, nivelRendimiento(calcularRendimiento(10.0, 2, 4.0)))
        assertEquals(NivelRendimiento.SIN_DATOS, nivelRendimiento(calcularRendimiento(0.0, 0, 4.0)))
    }
}
