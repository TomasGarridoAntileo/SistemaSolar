package cl.duoc.misolar.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

// Pruebas de las reglas de validacion (se ejecutan sin emulador)
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

    // ---------- Instalacion ----------

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
}
