package sh.vcm.billing.primer.domain

data class PaymentMethodData(
    val accountFundingType: String,
    val cardholderName: String,
    val expirationMonth: String,
    val expirationYear: String,
    val last4Digits: String,
    val network: String,
    val networkTransactionId: Long,
)
