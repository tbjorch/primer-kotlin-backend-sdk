package primer.api.types

data class ProductData(
    val sku: String,
    val brand: String,
    val color: String,
    val globalTradeItemNumber: String,
    val manufacturerPartNumber: String,
    val weight: Float,
    val weightUnit: WeightUnit
)
