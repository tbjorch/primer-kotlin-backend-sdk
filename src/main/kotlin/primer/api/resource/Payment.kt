package primer.api.resource

import primer.api.ApiResource
import primer.api.request.dto.CapturePaymentDto
import primer.api.request.dto.CreatePaymentDto
import primer.api.resource.Payment.Status.AUTHORIZED
import primer.api.resource.Payment.Status.DECLINED
import primer.api.resource.Payment.Status.FAILED
import primer.api.resource.Payment.Status.PENDING
import primer.api.resource.Payment.Status.SETTLING
import primer.api.types.Customer
import primer.api.types.Order
import primer.exception.PaymentException
import java.time.Instant
import java.util.UUID

data class Payment(
    val id: String,
    val date: Instant,
    val status: Status,
    val orderId: String,
    val currencyCode: String,
    val amount: Int,
    val order: Order,
    val paymentMethodToken: String,
    val customerId: String,
    val customer: Customer,
    val metadata: Map<String, String>,
    val paymentMethod: PaymentMethod,
) {
    companion object {

        private const val RESOURCE_PATH = "payments"

        fun create(
            createPaymentDto: CreatePaymentDto,
            idempotencyKey: String = UUID.randomUUID().toString(),
        ) = ApiResource.post<Payment, CreatePaymentDto>(
            path = RESOURCE_PATH,
            idempotencyKey = idempotencyKey,
            body = createPaymentDto,
        )

        fun retrieve(
            paymentId: String,
        ) = ApiResource.get<Payment>(path = RESOURCE_PATH, id = paymentId)
    }

    fun capture(
        capturePaymentDto: CapturePaymentDto? = null,
        idempotencyKey: String = UUID.randomUUID().toString(),
    ): Payment {
        if (this.status != AUTHORIZED) {
            throw PaymentException("Capture not allowed for payment status ${this.status}")
        }
        return ApiResource.post(
            path = "$RESOURCE_PATH/${this.id}/capture",
            idempotencyKey = idempotencyKey,
            body = capturePaymentDto,
        )
    }

    fun cancel(
        reason: String? = null,
        idempotencyKey: String = UUID.randomUUID().toString(),
    ): Payment {
        if (!this.isCancellable()) {
            throw PaymentException("Payment in status=${this.status} is not cancellable")
        }
        return ApiResource.post(
            path = "$RESOURCE_PATH/${this.id}/cancel",
            idempotencyKey = idempotencyKey,
            body = mapOf(
                "reason" to reason,
            ),
        )
    }

    private fun isCancellable() = listOf(AUTHORIZED, PENDING, FAILED, SETTLING, DECLINED).contains(this.status)

    enum class Status {
        AUTHORIZED,
        PENDING,
        FAILED,
        SETTLING,
        PARTIALLY_SETTLED,
        SETTLED,
        DECLINED,
        CANCELLED,
    }
}
