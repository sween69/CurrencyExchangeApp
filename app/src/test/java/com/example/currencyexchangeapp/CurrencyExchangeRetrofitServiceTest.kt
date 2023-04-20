package com.example.currencyexchangeapp

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test


internal class CurrencyExchangeRetrofitServiceTest {
    private val supportedBaseCurrency = "SGD"
    private val unsupportedBaseCurrency = "ABC"
    private var moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()


    /*    Input valid data - supported currency   */
    @Test
    fun getExchangeRates_inputSupportedCurrency_returnSuccessfulWithResponseBody() {
        runBlocking {
            val response = CurrencyExchangeRetrofitService.getExchangeRates(supportedBaseCurrency)
            assertTrue(response.isSuccessful)
            assertNotNull(response.body())
        }
    }

    @Test
    fun testExchangeRates_inputSupportedCurrency_returnSuccessResponseJsonMatchedWithExchangeRatesResponseModel() {
        // create an instance of ExchangeRatesResponseModel with supportedBaseCurrency values
        val expectedResponse = ExchangeRatesResponseModel(
            "success",
            "https://www.exchangerate-api.com/docs",
            "https://www.exchangerate-api.com/terms",
            0,
            "dateTime1",
            0,
            "dateTime2",
            supportedBaseCurrency,
            mapOf(supportedBaseCurrency to 1.0)
        )

        // parse the JSON response into an ExchangeRatesResponseModel object
        val responseJson = """
        {
            "result":"success",
             "documentation":"https://www.exchangerate-api.com/docs",
            "terms_of_use":"https://www.exchangerate-api.com/terms",
            "time_last_update_unix":0,
            "time_last_update_utc":"dateTime1",
            "time_next_update_unix":0,
            "time_next_update_utc":"dateTime2",
            "base_code":"$supportedBaseCurrency",
            "conversion_rates":{
            "$supportedBaseCurrency":1   }
        }
            """.trimIndent()

        val jsonAdapter = moshi.adapter(ExchangeRatesResponseModel::class.java)
        val response = jsonAdapter.fromJson(responseJson)

        assertEquals(expectedResponse, response)
    }


    /*    Input invalid data - unsupported currency   */
    @Test
    fun getExchangeRates_inputUnsupportedCurrency_returnFailedWithResponseErrorBody() {
        runBlocking {
            val response = CurrencyExchangeRetrofitService.getExchangeRates(unsupportedBaseCurrency)
            assertFalse(response.isSuccessful)
            assertNotNull(response.errorBody())
        }
    }

    @Test
    fun testExchangeRates_inputUnsupportedCurrency_returnErrorResponseJsonMatchedWithErrorResponseModel() {
        runBlocking {

            val response = CurrencyExchangeRetrofitService.getExchangeRates(unsupportedBaseCurrency)
            // create an instance of ErrorResponseModel with unsupportedBaseCurrency values
            val expectedErrorResponse = ErrorResponseModel(
                "error",
                "https://www.exchangerate-api.com/docs",
                "https://www.exchangerate-api.com/terms",
                "unsupported-code"
            )

            val errorBody = response.errorBody()?.string()
            val jsonAdapter = moshi.adapter(ErrorResponseModel::class.java)
            val errorResponse = jsonAdapter.fromJson(errorBody ?: "")

            assertEquals(expectedErrorResponse, errorResponse)
        }
    }
}