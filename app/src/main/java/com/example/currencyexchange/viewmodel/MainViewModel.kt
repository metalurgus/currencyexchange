package com.example.currencyexchange.viewmodel

import androidx.lifecycle.ViewModel
import com.example.currencyexchange.data.model.Balance
import com.example.currencyexchange.data.model.ExchangeRate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private val _mainViewStateFlow = MutableStateFlow(MainViewState())
    val mainViewStateFlow = _mainViewStateFlow.asStateFlow()

    private val _mainViewEffectFlow = MutableStateFlow(MainViewEffect())
    val mainViewEffectFlow = _mainViewEffectFlow.asStateFlow()

    data class MainViewState(
        val isLoading: Boolean = false,
        val fromCurrency: String? = null,
        val toCurrency: String? = null,
        val amount: Double = 0.0,
        val exchangeRate: Double = 0.0,
        val result: Double = 0.0,
        val balances: List<Balance> = emptyList(),
        val exchangeRates: List<ExchangeRate> = emptyList(),
    )

    data class MainViewEffect(
        val message: String? = null,
        val errorMessage: String? = null,
    )
}