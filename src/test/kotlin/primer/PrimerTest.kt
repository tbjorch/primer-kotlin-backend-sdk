package primer

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import primer.exception.PrimerException

class PrimerTest {
    @Test
    fun `Should require apiKey to be set`() {
        assertThrows<PrimerException> {
            Primer.apiKey
        }.also {
            assertThat(it).hasMessageContaining("Primer.apiKey must be set in order for SDK to work")
        }
    }

    @Test
    fun `Should provide production baseUrl`() {
        Primer.isProduction = true
        assertThat(Primer.baseUrl).isEqualTo("https://api.primer.io")
    }

    @Test
    fun `Should provide sandbox baseUrl`() {
        Primer.isProduction = false
        assertThat(Primer.baseUrl).isEqualTo("https://api.sandbox.primer.io")
    }

    @Test
    fun `Should allow setting new apiKey`() {
        val newApiKey = "newApiKey"
        assertDoesNotThrow { Primer.apiKey = newApiKey }
        assertThat(Primer.apiKey).isEqualTo(newApiKey)
    }
}
