package com.example.currencyexchangeapp

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

object CurrencyExchangeRetrofitService {
    private const val API_KEY = "f8e09b84c5520e57de6cf5d8"
    private const val BASE_URL = "https://v6.exchangerate-api.com/v6/$API_KEY/"

    private val api: ExchangeRateApi

    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClientWithLogs = OkHttpClient.Builder()
        // for debugging, adding logs to logcat
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClientWithLogs)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        api = retrofit.create(ExchangeRateApi::class.java)
    }

    fun getMoshi() = moshi

    suspend fun getExchangeRates(baseCurrency: String): Response<ExchangeRatesResponseModel> {
        return api.getExchangeRates(baseCurrency)
    }

    interface ExchangeRateApi {
        @GET("latest/{baseCurrency}")
        suspend fun getExchangeRates(@Path("baseCurrency") baseCurrency: String): Response<ExchangeRatesResponseModel>
    }
}


