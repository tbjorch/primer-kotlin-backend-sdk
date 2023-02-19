package primer.exception

class PrimerException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(message: String, ex: Exception) : super(message, ex)
    constructor(ex: Exception) : super(ex)
}
