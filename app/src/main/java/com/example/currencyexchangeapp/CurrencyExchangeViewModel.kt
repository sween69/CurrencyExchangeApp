package com.example.currencyexchangeapp

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrencyExchangeViewModel(private val repository: CurrencyExchangeRepository) : ViewModel() {

    private val _exchangeRateResult = MutableLiveData(Pair("", ""))
    val exchangeRateResult: LiveData<Pair<String, String>> get() = _exchangeRateResult
    private val _supportedCurrencyList = MutableLiveData(listOf(""))
    val supportedCurrencyList: LiveData<List<String>> get() = _supportedCurrencyList

    fun getExchangeRate(amount: Double, baseCurrency: String, targetCurrency: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.fetchExchangeRate(amount, baseCurrency, targetCurrency)
            _exchangeRateResult.postValue(result)
        }
    }

    fun getSupportedCurrencyList() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.fetchSupportedCurrency() ?: listOf()
            _supportedCurrencyList.postValue(result)
        }
    }

    // Define ViewModel factory in a companion object
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val currencyExchangeRepository = CurrencyExchangeRepository()
                CurrencyExchangeViewModel(repository = currencyExchangeRepository)
            }
        }
    }
}


