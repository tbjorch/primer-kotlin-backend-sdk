package primer

import primer.exception.PrimerException

class Primer {
    companion object {
        private const val sandboxBaseUrl = "https://api.sandbox.primer.io"
        private const val productionBaseUrl = "https://api.primer.io"
        var isProduction: Boolean = false

        val baseUrl: String
            get() {
                return if (isProduction) productionBaseUrl else sandboxBaseUrl
            }

        var apiKey: String = ""
            get() {
                if (field.isBlank()) throw PrimerException("Primer.apiKey must be set in order for SDK to work")
                return field
            }
    }
}
