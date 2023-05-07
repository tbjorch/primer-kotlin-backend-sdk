package primer.api.types

import primer.api.resource.PaymentMethod

data class PaymentMethodRoot(
    val data: Set<PaymentMethod>,
)
