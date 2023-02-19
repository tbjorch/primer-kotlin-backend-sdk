package primer.api.resource

import primer.api.ApiResource
import primer.api.request.dto.CreateClientSessionData
import primer.api.types.Customer
import primer.api.types.Order
import primer.api.types.PaymentMethod
import primer.api.types.Warning
import java.time.Instant
import java.util.Currency
import java.util.UUID

class ClientSession {
    var clientToken: String? = null
    var clientTokenExpirationDate: Instant? = null
    var currency: Currency? = null
    var amountInMinorUnit: Int? = null
    var orderId: String? = null
    var order: Order? = null
    var customerId: String? = null
    var customer: Customer? = null

    // TODO make metadata support both String, String and String, Int
    var metadata: Map<String, String>? = null
    var paymentMethod: PaymentMethod? = null
    var warnings: Set<Warning>? = null

    companion object {
        private const val RESOURCE_PATH = "client-session"

        // TODO: decide on static create method using DTO, or constructor based creation?
        fun create(
            clientSessionData: CreateClientSessionData,
            idempotencyKey: String = UUID.randomUUID().toString(),
        ): ClientSession {
            return ApiResource.post(RESOURCE_PATH, idempotencyKey, clientSessionData)
        }

        fun retrieve(clientToken: String): ClientSession {
            val params = mapOf(QueryParams.CLIENT_TOKEN.headerValue to clientToken)
            return ApiResource.get(path = RESOURCE_PATH, params = params)
        }
    }

    fun update(idempotencyKey: String = UUID.randomUUID().toString()): ClientSession =
        ApiResource.patch(RESOURCE_PATH, idempotencyKey, this)

    enum class QueryParams(
        val headerValue: String,
    ) {
        CLIENT_TOKEN("clientToken"),
    }
}
