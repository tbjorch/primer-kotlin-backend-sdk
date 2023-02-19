package primer.api.types

import java.util.Locale.IsoCountryCode

data class Address(
    val firstName: String,
    val lastName: String,
    val addressLine1: String,
    val addressLine2: String?,
    val city: String,
    val state: String?,
    val countryCode: IsoCountryCode
)
