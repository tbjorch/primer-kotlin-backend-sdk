package primer.api.resource

import primer.api.ApiResource
import primer.api.request.dto.CreateClientSessionData
import primer.api.types.Customer
import primer.api.types.Order
import primer.api.types.Warning
import java.time.Instant
import java.util.UUID

data class ClientSession(
    val clientToken: String,
    val clientExpirationDate: Instant,
    var currencyCode: String?,
    var amountInMinorUnit: Int?,
    var orderId: String?,
    var order: Order?,
    val customerId: String?,
    var customer: Customer?,
    // TODO make metadata support both String, String and String, Int
    var metadata: Map<String, String>?,
    var paymentMethod: PaymentMethod?,
    val warnings: Set<Warning>?,
) {
    companion object {
        private const val RESOURCE_PATH = "client-session"

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

    fun update(idempotencyKey: String = UUID.randomUUID().toString()): ClientSession {
        requireNotNull(this.clientToken)
        return ApiResource.patch(RESOURCE_PATH, idempotencyKey, this)
    }

    private enum class QueryParams(
        val headerValue: String,
    ) {
        CLIENT_TOKEN("clientToken"),
    }
}
