package com.example.currencyexchangeapp

import android.R
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.currencyexchangeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var baseCurrencySpinner: Spinner
    private lateinit var targetCurrencySpinner: Spinner
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var lastBaseCurrency: String
    private lateinit var lastTargetCurrency: String

    private val currencyExchangeViewModel: CurrencyExchangeViewModel by viewModels { CurrencyExchangeViewModel.Factory }
    private var currencyList = ArrayList<String>()
    private var defaultListSize = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setupViews()
        setupListeners()
        setupObservers()
    }

    override fun onStart() {
        super.onStart()
        //for app launch network check
        NetworkUtils.isNetworkAvailable(this)

        NetworkUtils.registerNetworkStatusListener(this, object : NetworkUtils.NetworkStatusListener {
            override fun onNetworkStatusChanged(isConnected: Boolean) {
                if (isConnected) {
                    // only query list if list haven't updated
                    if (currencyList.size <= defaultListSize) currencyExchangeViewModel.getSupportedCurrencyList()
                } else {
                    NetworkUtils.isNetworkAvailable(this@MainActivity)
                }
            }
        })
    }

    private fun setupViews() {
        setupSpinnerView()

        // set total amount display with scrollable if out of view
        binding.totalAmountTextView.movementMethod = ScrollingMovementMethod()
    }

    private fun setupSpinnerView() {
        // get last base & target currency from sharedPref, default base = SGD, target = MYR
        lastBaseCurrency = SharedPreferencesUtils.getLastBaseCurrency(this)
        lastTargetCurrency = SharedPreferencesUtils.getLastTargetCurrency(this)

        // create default short list by last selected currency
        currencyList.add(lastBaseCurrency)
        currencyList.add(lastTargetCurrency)
        defaultListSize = currencyList.size

        baseCurrencySpinner = binding.baseCurrencySpinner
        targetCurrencySpinner = binding.targetCurrencySpinner

        adapter = ArrayAdapter(this, R.layout.simple_spinner_item, currencyList)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        baseCurrencySpinner.adapter = adapter
        targetCurrencySpinner.adapter = adapter

        /* must call before setup spinner listener, else initialization will triggered listener to pre-select an item,
        and save the wrong pre-selected item into sharedPreference as last selected item
         https://stackoverflow.com/questions/13397933/android-spinner-avoid-onitemselected-calls-during-initialization*/
        baseCurrencySpinner.setSelection(0, false)
        targetCurrencySpinner.setSelection(1, false)
    }

    private fun setupListeners() {
        //spinner base currency
        baseCurrencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                parent?.let { parent ->
                    val selectedItem = parent.getItemAtPosition(position) as String
                    SharedPreferencesUtils.saveLastBaseCurrency(this@MainActivity, selectedItem)
                    // clear display text
                    binding.baseRateTextView.text = ""
                    binding.totalAmountTextView.text = ""
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing when no item is selected
            }
        }

        // spinner target currency
        targetCurrencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                parent?.let { parent ->
                    val selectedItem = parent.getItemAtPosition(position) as String
                    SharedPreferencesUtils.saveLastTargetCurrency(this@MainActivity, selectedItem)
                    // clear display text
                    binding.baseRateTextView.text = ""
                    binding.totalAmountTextView.text = ""
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing when no item is selected
            }
        }

        // check rate button
        binding.checkRateButton.setOnClickListener {
            checkExchangeRate(it)
        }

        // clear input button
        binding.clearButtonImageView.setOnClickListener {
            binding.amountEditText.text.clear()
            binding.totalAmountTextView.text = ""
        }

        // input soft keyboard click on done action
        binding.amountEditText.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                checkExchangeRate(view)
                true
            } else false
        }
    }

    private fun checkExchangeRate(it: View) {
        // hide the soft keyboard
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        // check network & query rates
        if (NetworkUtils.isNetworkAvailable(this)) {
            val amount = binding.amountEditText.text.toString().toDoubleOrNull()?.takeIf { it > 0 } ?: 1.0
            val baseCurrency = binding.baseCurrencySpinner.selectedItem.toString()
            val targetCurrency = binding.targetCurrencySpinner.selectedItem.toString()
            currencyExchangeViewModel.getExchangeRate(amount, baseCurrency, targetCurrency)
        }
    }

    private fun setupObservers() {
        // update exchange rate result
        currencyExchangeViewModel.exchangeRateResult.observe(this) { exchangeResult ->
            binding.totalAmountTextView.text = exchangeResult.first
            binding.baseRateTextView.text = exchangeResult.second
        }

        // update currency spinner list
        currencyExchangeViewModel.supportedCurrencyList.observe(this) { list ->
            currencyList = ArrayList(list)
            // only clear the offline list when query list successfully
            if (currencyList.size > defaultListSize) {
                adapter.clear()
                currencyList.forEach { adapter.add(it) }

                // save last Selected currency into full currency list
                val baseIndex = currencyList.indexOf(lastBaseCurrency).takeIf { it >= 0 } ?: currencyList.indexOf(DEFAULT_BASE_CURRENCY)
                baseCurrencySpinner.setSelection(baseIndex, false)
                val targetIndex = currencyList.indexOf(lastTargetCurrency).takeIf { it >= 0 } ?: currencyList.indexOf(DEFAULT_TARGET_CURRENCY)
                targetCurrencySpinner.setSelection(targetIndex, false)

            }
        }
    }

    override fun onStop() {
        NetworkUtils.unregisterNetworkStatusListener()
        super.onStop()
    }
}
