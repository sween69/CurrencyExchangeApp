package com.example.currencyexchangeapp

import android.content.Context
import android.content.SharedPreferences

const val DEFAULT_BASE_CURRENCY = "SGD"
const val DEFAULT_TARGET_CURRENCY = "MYR"

object SharedPreferencesUtils {
    private const val PREFS_NAME = "app_preferences"
    private const val BASE_CURRENCY_KEY = "base_currency_key"
    private const val TARGET_CURRENCY_KEY = "target_currency_key"

    fun saveLastBaseCurrency(context: Context, baseCurrency: String) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString(BASE_CURRENCY_KEY, baseCurrency)
        editor.apply()
    }

    fun saveLastTargetCurrency(context: Context, targetCurrency: String) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString(TARGET_CURRENCY_KEY, targetCurrency)
        editor.apply()
    }

    fun getLastBaseCurrency(context: Context): String {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString(BASE_CURRENCY_KEY, DEFAULT_BASE_CURRENCY) ?: DEFAULT_BASE_CURRENCY
    }

    fun getLastTargetCurrency(context: Context): String {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString(TARGET_CURRENCY_KEY, DEFAULT_TARGET_CURRENCY) ?: DEFAULT_TARGET_CURRENCY
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
}
