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
}
