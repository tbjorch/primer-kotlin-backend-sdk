package primer.api.request.dto

import primer.api.types.Customer
import primer.api.types.Order
import primer.api.types.PaymentMethod
import primer.api.types.Warning
import java.util.Currency

data class CreateClientSessionData(
    var currency: Currency?,
    var amountInMinorUnit: Int?,
    var orderId: String?,
    var order: Order?,
    var customerId: String?,
    var customer: Customer?,
    // TODO make metadata support both String, String and String, Int
    var metadata: Map<String, String>?,
    var paymentMethod: PaymentMethod?,
    var warnings: Set<Warning>?,
)
