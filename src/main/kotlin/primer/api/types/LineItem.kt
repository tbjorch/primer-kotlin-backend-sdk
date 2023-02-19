package primer.api.types

data class LineItem(
    val itemId: String,
    val name: String,
    val description: String,
    val amount: Int,
    val quantity: Int,
    val discountAmount: Int,
    val taxAmount: Int,
    val taxCode: String,
    val productType: ProductType,
    val productData: ProductData

)
