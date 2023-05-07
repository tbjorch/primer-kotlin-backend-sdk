package primer.exception

class PaymentException : PrimerException {
    constructor(message: String) : super(message)
    constructor(message: String, ex: Exception) : super(message, ex)
    constructor(ex: Exception) : super(ex)
}
