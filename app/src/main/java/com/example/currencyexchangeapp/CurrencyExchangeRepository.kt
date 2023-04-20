package com.example.currencyexchangeapp

import android.util.Log
import java.math.BigDecimal

class CurrencyExchangeRepository {
    private val tag = javaClass.simpleName

    suspend fun fetchExchangeRate(amount: Double, baseCurrency: String, targetCurrency: String): Pair<String, String> {
        return try {
            val response = CurrencyExchangeRetrofitService.getExchangeRates(baseCurrency)
            if (response.isSuccessful) {
                val conversionRates = response.body()?.conversionRates
                val totalAmount = conversionRates?.get(targetCurrency)?.times(amount)
                val baseRate = conversionRates?.get(targetCurrency)

                Pair(
                    // show full amount and with common separate and 2 decimal points
                    "$targetCurrency ${String.format("%,.2f", BigDecimal(totalAmount ?: 0.0))}",
                    "1 $baseCurrency = $baseRate $targetCurrency"
                )
            } else {
                var errorMessage = ""
                response.errorBody()?.string()?.let { errorBody ->
                    Log.d(tag, "error: $errorBody")
                    val moshi = CurrencyExchangeRetrofitService.getMoshi()
                    val jsonAdapter = moshi.adapter(ErrorResponseModel::class.java)
                    val errorResponse = jsonAdapter.fromJson(errorBody)
                    errorMessage = errorResponse?.errorType ?: ""
                }
                Pair("Error fetching exchange rate, try again later", errorMessage)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Pair("Error fetching exchange rate", "")
        }
    }

    suspend fun fetchSupportedCurrency(): List<String>? {
        return try {
            // fetch the first currency info so the list will sort by alphabet
            val response = CurrencyExchangeRetrofitService.getExchangeRates("AED")
            if (response.isSuccessful) {
                val conversionRates = response.body()?.conversionRates
                conversionRates?.entries?.map { it.key }
            } else {
                response.errorBody()?.string()?.let { errorBody ->
                    val moshi = CurrencyExchangeRetrofitService.getMoshi()
                    val jsonAdapter = moshi.adapter(ErrorResponseModel::class.java)
                    val errorResponse = jsonAdapter.fromJson(errorBody)
                    Log.d(tag, "fetchSupportedCurrency() failed with error: ${errorResponse?.errorType}")
                }
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}


