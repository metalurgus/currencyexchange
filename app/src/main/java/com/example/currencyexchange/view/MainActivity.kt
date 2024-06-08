package com.example.currencyexchange.view

import android.graphics.Rect
import android.os.Bundle
import android.view.View
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
            if(itemPosition != 0) {
                outRect.left = space
            }
            if(itemPosition == parent.adapter?.itemCount?.minus(1)) {
                outRect.right = space
            }
        }
    }
}