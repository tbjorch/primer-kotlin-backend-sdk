package primer.api.types

data class Order(
    val lineItems: Set<LineItem>,
    val countryCode: String,
    val fees: Set<Fee>?,
    val shipping: Shipping?
)
