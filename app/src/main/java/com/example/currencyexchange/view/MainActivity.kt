package com.example.currencyexchange.view

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencyexchange.util.afterTextChangedWatcher
import com.example.currencyexchange.util.noNothingSelectOnItemSelectedListener
import com.example.currencyexchange.util.SpaceItemDecoration
import com.example.currencyexchange.view.adapter.BalanceAdapter
import com.example.currencyexchange.view.adapter.CurrencyRateAdapter
import com.example.currencyexchange.viewmodel.MainViewModel
import com.example.currencyexchangetesttask.R
import com.example.currencyexchangetesttask.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private var fromCurrenciesAdapter: ArrayAdapter<String>? = null
    private var toCurrenciesAdapter: ArrayAdapter<String>? = null
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewMyBalances.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewMyBalances.adapter = BalanceAdapter()
        binding.recyclerViewCurrencyRates.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewCurrencyRates.adapter = CurrencyRateAdapter()

        val space = resources.getDimensionPixelSize(R.dimen.item_space)
        binding.recyclerViewMyBalances.addItemDecoration(SpaceItemDecoration(space))
        binding.recyclerViewCurrencyRates.addItemDecoration(SpaceItemDecoration(space))
        fromCurrenciesAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, mutableListOf<String>())
        fromCurrenciesAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        toCurrenciesAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, mutableListOf<String>())
        toCurrenciesAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSellCurrency.adapter = fromCurrenciesAdapter
        binding.spinnerSellCurrency.onItemSelectedListener =
            noNothingSelectOnItemSelectedListener { parent, _, position, _ ->
                val selectedCurrency = parent.getItemAtPosition(position).toString()
                viewModel.updateFromCurrency(selectedCurrency)
            }

        binding.spinnerBuyCurrency.adapter = toCurrenciesAdapter
        binding.spinnerBuyCurrency.onItemSelectedListener =
            noNothingSelectOnItemSelectedListener { parent, _, position, _ ->
                val selectedCurrency = parent.getItemAtPosition(position).toString()
                viewModel.updateToCurrency(selectedCurrency)
            }

        binding.editTextSellAmount.addTextChangedListener(
            afterTextChangedWatcher { amount ->
                viewModel.updateAmount(amount)
            }
        )

        subscribeToViewStateAndEffects()
        viewModel.updateBalances()

    }

    private fun subscribeToViewStateAndEffects() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { viewState ->
                    (binding.recyclerViewMyBalances.adapter as BalanceAdapter).submitList(viewState.balances)
                    (binding.recyclerViewCurrencyRates.adapter as CurrencyRateAdapter).submitList(
                        viewState.exchangeRates
                    )
                    fromCurrenciesAdapter?.let {
                        it.clear()
                        it.addAll(viewState.sellableCurrencies)
                        binding.spinnerSellCurrency.setSelection(
                            it.getPosition(viewState.fromCurrency)
                        )
                    }
                    toCurrenciesAdapter?.let {
                        it.clear()
                        it.addAll(viewState.buyableCurrencies)
                        binding.spinnerBuyCurrency.setSelection(
                            it.getPosition(viewState.toCurrency)
                        )
                    }
                    if (binding.editTextSellAmount.text.toString() != viewState.amountText) {
                        binding.editTextSellAmount.setText(viewState.amountText)
                    }

                    binding.imageViewWarningBalances.visibility =
                        if (viewState.balancesError) View.VISIBLE else View.GONE
                    binding.imageViewWarningCurrencyRates.visibility =
                        if (viewState.exchangeRatesError) View.VISIBLE else View.GONE
                    if (binding.textViewBuyAmount.text.toString() != viewState.exchangeResult) {
                        binding.textViewBuyAmount.text = viewState.exchangeResult
                    }
                    binding.buttonExchange.isEnabled = viewState.exchangeEnabled
                    binding.buttonExchange.setOnClickListener {
                        viewModel.exchange()
                    }
                    binding.buttonTransactionHistory.setOnClickListener {
                        val transactionHistoryDialogFragment = TransactionHistoryDialogFragment()
                        transactionHistoryDialogFragment.show(
                            supportFragmentManager,
                            "TransactionHistoryDialog"
                        )
                    }

                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewEffect.collect { viewEffect ->
                    viewEffect.message?.let { message ->
                        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).apply {
                            setBackgroundTint(Color.BLUE)
                            show()
                        }
                    }
                    viewEffect.errorMessage?.let { errorMessage ->
                        Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).apply {
                            setBackgroundTint(Color.RED)
                            show()
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startUpdatingCurrencyRates()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopUpdatingCurrencyRates()
    }


}