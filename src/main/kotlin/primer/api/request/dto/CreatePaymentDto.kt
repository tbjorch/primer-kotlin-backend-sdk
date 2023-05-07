package primer.api.request.dto

import primer.api.resource.PaymentMethod
import primer.api.types.Customer
import primer.api.types.Order

data class CreatePaymentDto(
    val orderId: String,
    val currencyCode: String,
    val amount: Int?,
    val order: Order? = null,
    val paymentMethodToken: String,
    val customerId: String,
    val customer: Customer? = null,
    val metadata: Map<String, String>? = null,
    val paymentMethod: PaymentMethod? = null,
)
