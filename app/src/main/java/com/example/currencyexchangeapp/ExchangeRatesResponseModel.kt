package com.example.currencyexchangeapp

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/*
* know bug: kotlinx cant serialize/deserialize external json object to Kotlin data class with number of field more than 250 fields
* https://github.com/Kotlin/kotlinx.serialization/issues/1393
* https://github.com/Kotlin/kotlinx.serialization/issues/632
* https://stackoverflow.com/questions/73012212/creating-a-custom-serializer-for-large-data-classes
* */

@JsonClass(generateAdapter = true)
data class ExchangeRatesResponseModel(
    val result: String,
    val documentation: String,
    @Json(name = "terms_of_use") val termsOfUse: String,
    @Json(name = "time_last_update_unix") val timeLastUpdateUnix: Int,
    @Json(name = "time_last_update_utc") val timeLastUpdateUtc: String,
    @Json(name = "time_next_update_unix") val timeNextUpdateUnix: Int,
    @Json(name = "time_next_update_utc") val timeNextUpdateUtc: String,
    @Json(name = "base_code") val baseCode: String,
    @Json(name = "conversion_rates") val conversionRates: Map<String, Double>
)

@JsonClass(generateAdapter = true)
data class ErrorResponseModel(
    val result: String,
    val documentation: String,
    @Json(name = "terms-of-use") val termsOfUse: String,
    @Json(name = "error-type") val errorType: String
)

