package com.example.currencyexchange.view

import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyexchange.view.adapter.BalanceAdapter
import com.example.currencyexchange.view.adapter.CurrencyRateAdapter
import com.example.currencyexchange.viewmodel.MainViewModel
import com.example.currencyexchangetesttask.R
import com.example.currencyexchangetesttask.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private var fromCurrenciesAdapter: ArrayAdapter<String>? = null
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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
        binding.spinnerSellCurrency.adapter = fromCurrenciesAdapter
        binding.spinnerSellCurrency.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    val selectedCurrency = parent.getItemAtPosition(position).toString()
                    viewModel.updateSellFromCurrency(selectedCurrency)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do something when nothing is selected
                }
            }

        binding.editTextSellAmount.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}

                override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    viewModel.updateAmount(s.toString())
                }
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
                    if (binding.editTextSellAmount.text.toString() != viewState.amountText) {
                        binding.editTextSellAmount.setText(viewState.amountText)
                    }

                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewEffect.collect { viewEffect ->
                    viewEffect.message?.let { message ->
                        // Show message
                    }
                    viewEffect.errorMessage?.let { errorMessage ->
                        // Show error message
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

    class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect, view: View,
            parent: RecyclerView, state: RecyclerView.State
        ) {
            outRect.left = space
            outRect.right = space
            outRect.bottom = space

            val itemPosition = parent.getChildAdapterPosition(view)
            if (itemPosition != 0) {
                outRect.left = space
            }
            if (itemPosition == parent.adapter?.itemCount?.minus(1)) {
                outRect.right = space
            }
        }
    }
}