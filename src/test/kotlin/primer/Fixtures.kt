package primer

import primer.api.resource.Payment
import primer.api.resource.Payment.Status.SETTLED
import primer.api.resource.PaymentMethod
import primer.api.types.Customer
import primer.api.types.Order
import sh.vcm.billing.primer.domain.PaymentMethodData
import java.time.Instant

object Fixtures {

    fun payment(
        paymentId: String = "paymentId",
        status: Payment.Status = SETTLED,
    ) = Payment(
        id = paymentId,
        date = Instant.now(),
        status = status,
        orderId = "orderId",
        currencyCode = "SEK",
        amount = 1000,
        order = Order(lineItems = setOf(), countryCode = "", fees = setOf(), shipping = null),
        paymentMethodToken = "paymentMethodToken",
        customerId = "customerId",
        customer = customer(),
        metadata = mapOf(),
        paymentMethod = paymentMethod(),
    )

    fun paymentMethod() = PaymentMethod(
        analyticsId = "",
        createdAt = "",
        customerId = "",
        default = false,
        deleted = false,
        description = null,
        paymentMethodType = null,
        paymentMethodData = PaymentMethodData(
            accountFundingType = "",
            cardholderName = "",
            expirationMonth = "",
            expirationYear = "",
            last4Digits = "",
            network = "",
            networkTransactionId = 0,
        ),
        token = "",
        tokenType = "",
    )

    fun customer() = Customer(
        emailAddress = null,
        mobileNumber = null,
        firstName = null,
        lastName = null,
        billingAddress = null,
        shippingAddress = null,
        taxId = null,
        nationalDocumentId = null,
    )
}
