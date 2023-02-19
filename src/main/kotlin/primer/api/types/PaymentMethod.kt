package primer.api.types

data class PaymentMethod(
    val vaultOnSuccess: Boolean,
    val descriptor: String?,
    val paymentType: PaymentType,
    // TODO add support for PaymentMethod.options. See https://apiref.primer.io/reference/create_client_side_token_client_session_post
)
