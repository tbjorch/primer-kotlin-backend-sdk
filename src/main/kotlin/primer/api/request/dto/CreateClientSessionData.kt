package primer.api.request.dto

import primer.api.types.Customer
import primer.api.types.Order
import primer.api.resource.PaymentMethod
import primer.api.types.Warning
import java.util.Currency

data class CreateClientSessionData(
    var currency: Currency? = null,
    var amountInMinorUnit: Int? = null,
    var orderId: String? = null,
    var order: Order? = null,
    var customerId: String? = null,
    var customer: Customer? = null,
    // TODO make metadata support both String, String and String, Int
    var metadata: Map<String, String>? = null,
    var paymentMethod: PaymentMethod? = null,
    var warnings: Set<Warning>? = null,
)
