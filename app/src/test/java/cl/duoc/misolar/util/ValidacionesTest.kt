package cl.duoc.misolar.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

// Pruebas de las validaciones, sin emulador
class ValidacionesTest {

    @Test
    fun nombreVacioTieneError() {
        assertEquals("El nombre es obligatorio", validarNombre(""))
    }

    @Test
    fun nombreConNumerosTieneError() {
        assertNotNull(validarNombre("Tomas2"))
    }

    @Test
    fun nombreConTildeEsValido() {
        assertNull(validarNombre("Tomás"))
    }

    @Test
    fun correoSinArrobaTieneError() {
        assertEquals("Falta el @", validarCorreo("tomas.duocuc.cl"))
    }

    @Test
    fun correoValido() {
        assertNull(validarCorreo("tomas@duocuc.cl"))
    }

    @Test
    fun passwordCortaTieneError() {
        assertNotNull(validarPassword("abc1"))
    }

    @Test
    fun passwordSinNumeroTieneError() {
        assertEquals("Debe incluir al menos un número", validarPassword("abcdefgh"))
    }

    @Test
    fun passwordValida() {
        assertNull(validarPassword("solar2026"))
    }

    @Test
    fun passwordsDistintasTienenError() {
        assertEquals("Las contraseñas no coinciden", validarRepetirPassword("solar2026", "solar2025"))
    }

    @Test
    fun terminosNoAceptadosTienenError() {
        assertNotNull(validarTerminos(false))
        assertNull(validarTerminos(true))
    }

    // Instalacion

    @Test
    fun cantidadPanelesConLetrasTieneError() {
        assertEquals("Ingrese solo números enteros", validarCantidadPaneles("diez"))
    }

    @Test
    fun cantidadPanelesFueraDeRangoTieneError() {
        assertNotNull(validarCantidadPaneles("0"))
        assertNotNull(validarCantidadPaneles("101"))
        assertNull(validarCantidadPaneles("12"))
    }

    @Test
    fun potenciaPanelValida() {
        assertNull(validarPotenciaPanel("450"))
        assertNotNull(validarPotenciaPanel("50"))
    }

    @Test
    fun fechaConFormatoIncorrectoTieneError() {
        assertEquals("Use el formato dd/mm/aaaa con una fecha real", validarFechaPasada("2026-01-10"))
    }

    @Test
    fun fechaImposibleTieneError() {
        assertNotNull(validarFechaPasada("31/02/2025"))
    }

    @Test
    fun fechaFuturaTieneError() {
        assertEquals("La fecha no puede ser futura", validarFechaPasada("01/01/2099"))
    }

    @Test
    fun fechaPasadaValida() {
        assertNull(validarFechaPasada("15/03/2024"))
    }

    @Test
    fun precioKwhVacioTieneError() {
        assertEquals("El precio del kWh es obligatorio", validarPrecioKwh(""))
    }

    // Lectura

    @Test
    fun kwhConComaEsValido() {
        // 4 kW x 12 horas = 48 kWh maximo
        assertNull(validarKwh("12,5", 4.0))
    }

    @Test
    fun kwhSobreElMaximoTieneError() {
        assertNotNull(validarKwh("60", 4.0))
    }

    @Test
    fun kwhCeroOTextoTieneError() {
        assertEquals("Debe ser mayor que 0", validarKwh("0", 4.0))
        assertEquals("Ingrese un número, por ejemplo 12,5", validarKwh("abc", 4.0))
    }

    @Test
    fun lecturaAntesDeLaInstalacionTieneError() {
        assertNotNull(validarFechaLectura("01/01/2024", "15/03/2024"))
        assertNull(validarFechaLectura("20/03/2024", "15/03/2024"))
    }

    @Test
    fun notaMuyLargaTieneError() {
        assertNull(validarNota(""))
        assertNotNull(validarNota("a".repeat(101)))
    }
}
