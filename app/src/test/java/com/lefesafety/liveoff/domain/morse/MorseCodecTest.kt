package com.lefesafety.liveoff.domain.morse

import org.junit.Assert.assertEquals
import org.junit.Test

class MorseCodecTest {

    private val codec = MorseCodec()

    @Test
    fun `SOS encodes correctly`() {
        assertEquals("... --- ...", codec.encode("SOS"))
    }

    @Test
    fun `Russian A encodes to dot-dash`() {
        assertEquals(".-", codec.encode("А"))
    }

    @Test
    fun `digits encode correctly`() {
        assertEquals("-----", codec.encode("0"))
        assertEquals(".----", codec.encode("1"))
        assertEquals("..---", codec.encode("2"))
    }

    @Test
    fun `full Russian word encodes`() {
        assertEquals("... --- ...", codec.encode("СОС"))
    }

    @Test
    fun `spaces become word separators`() {
        assertEquals("... --- ... / -.. .-", codec.encode("СОС ДА"))
    }

    @Test
    fun `decode reverses encode`() {
        val original = "ПОМОГИТЕ"
        val encoded = codec.encode(original)
        assertEquals(original, codec.decode(encoded))
    }

    @Test
    fun `unknown characters are skipped`() {
        val result = codec.encode("А@Б")
        assertEquals(".- -...", result)
    }

    @Test
    fun `getAlphabet returns all Russian letters and digits`() {
        val alphabet = codec.getAlphabet()
        assertEquals(33 + 10, alphabet.size)
    }
}
