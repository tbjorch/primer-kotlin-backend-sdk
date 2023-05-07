package primer.api.resource

import primer.api.ApiResource
import primer.api.types.PaymentMethodRoot
import sh.vcm.billing.primer.domain.PaymentMethodData

data class PaymentMethod(
    val analyticsId: String,
    val createdAt: String,
    val customerId: String,
    private var default: Boolean,
    private var deleted: Boolean,
    val description: String?,
    val paymentMethodType: String?,
    val paymentMethodData: PaymentMethodData,
    val token: String,
    val tokenType: String,
) {

    val isDefault: Boolean get() = default

    val isDeleted: Boolean get() = deleted

    companion object {
        private const val RESOURCE_PATH = "payment-instruments"

        fun retrieveSavedPaymentMethods(customerId: String): Set<PaymentMethod> {
            val params = mapOf(QueryParams.CUSTOMER_ID.headerValue to customerId)
            return ApiResource.get<PaymentMethodRoot>(path = RESOURCE_PATH, params = params).data
        }

        fun retrieveDefaultPaymentMethod(customerId: String): PaymentMethod {
            return retrieveSavedPaymentMethods(customerId).first{ it.isDefault }
        }
    }

    fun makeDefault() =
        ApiResource.post<PaymentMethod, Unit>(path = "$RESOURCE_PATH/${this.token}/default", body = Unit)
            .let { default = it.default }

    fun delete() =
        ApiResource.delete<PaymentMethod>(path = "$RESOURCE_PATH/${this.token}").let { deleted = it.deleted }

    private enum class QueryParams(
        val headerValue: String,
    ) {
        CUSTOMER_ID("customer_id"),
    }
}
