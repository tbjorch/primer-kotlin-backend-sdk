package primer

import primer.exception.PrimerException
import java.net.URL

class Primer {
    companion object {
        private val sandboxBaseUrl = URL("https://api.sandbox.primer.io")
        private val productionBaseUrl = URL("https://api.primer.io")
        var unitTestUrl: URL? = null
        var isProduction: Boolean = false

        val baseUrl: URL
            get() {
                return unitTestUrl
                    ?: if (isProduction) productionBaseUrl else sandboxBaseUrl
            }

        var apiKey: String = ""
            get() {
                if (field.isBlank()) throw PrimerException("Primer.apiKey must be set in order for SDK to work")
                return field
            }
        var apiVersion: String = ""
            get() {
                if (field.isBlank()) throw PrimerException("Primer.apiVersion must be set in order for SDK to work")
                return field
            }
    }
}
