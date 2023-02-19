package primer.api.types

data class Customer(
    val emailAddress: String?,
    val mobileNumber: String?,
    val firstName: String?,
    val lastName: String?,
    val billingAddress: Address?,
    val shippingAddress: Address?,
    val taxId: String?,
    val nationalDocumentId: String?,
)
