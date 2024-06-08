package com.example.currencyexchange.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyexchange.data.Result
import com.example.currencyexchange.data.model.Balance
import com.example.currencyexchange.data.model.ExchangeRate
import com.example.currencyexchange.data.usecase.GetExchangeRatesUseCase
import com.example.currencyexchange.data.usecase.GetUserBalancesUseCase
import com.example.currencyexchange.data.usecase.UseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val getExchangeRatesUseCase: GetExchangeRatesUseCase,
    private val getUserBalancesUseCase: GetUserBalancesUseCase
) : ViewModel() {

    private val _mainViewStateFlow = MutableStateFlow(MainViewState())
    val viewState = _mainViewStateFlow.asStateFlow()

    private val _mainViewEffectFlow = MutableStateFlow(MainViewEffect())
    val viewEffect = _mainViewEffectFlow.asStateFlow()

    private var isUpdatingExchangeRates = false

    data class MainViewState(
        val isLoading: Boolean = false,
        val fromCurrency: String? = null,
        val toCurrency: String? = null,
        val amountText: String = "",
        val exchangeRate: Double = 0.0,
        val result: Double = 0.0,
        val balances: List<Balance> = emptyList(),
        val exchangeRates: List<ExchangeRate> = emptyList(),
        val sellableCurrencies: Collection<String> = emptyList(),
    )

    data class MainViewEffect(
        val message: String? = null,
        val errorMessage: String? = null,
    )


    fun startUpdatingCurrencyRates() {
        isUpdatingExchangeRates = true
        viewModelScope.launch {
            while (isUpdatingExchangeRates) {
                updateCurrencyRates()
                delay(5000) // delay for 5 seconds
            }
        }
    }

    fun stopUpdatingCurrencyRates() {
        isUpdatingExchangeRates = false
    }

    private fun updateCurrencyRates() {
        Log.d("MainViewModel", "updateCurrencyRates")
        viewModelScope.launch {
            getExchangeRatesUseCase.run(UseCase.None).collect { result ->
                when (result) {
                    is Result.Success -> {
                        val exchangeRatesResponse = result.data
                        val sellableCurrencies =
                            exchangeRatesResponse
                                .rates
                                .map { it.currency } intersect
                                    _mainViewStateFlow
                                        .value
                                        .balances
                                        .filter { it.amount > 0 }
                                        .map { it.currency }
                                        .toSet()

                        _mainViewStateFlow.value = _mainViewStateFlow.value.copy(
                            exchangeRates = exchangeRatesResponse.rates,
                            sellableCurrencies = sellableCurrencies
                        )
                    }

                    is Result.Error -> {
                        _mainViewEffectFlow.update {
                            it.copy(
                                errorMessage = result.exception.message
                            )
                        }
                    }

                    else -> Unit
                }
            }
        }
    }

    fun updateBalances() {
        Log.d("MainViewModel", "updateBalances")
        viewModelScope.launch {
            getUserBalancesUseCase.run(UseCase.None).collect { result ->
                when (result) {
                    is Result.Success -> {
                        val balances = result.data
                        val sellableCurrencies =
                            if (_mainViewStateFlow.value.exchangeRates.isEmpty()) {
                                mutableListOf()
                            } else {
                                balances.filter { it.amount > 0 }
                                    .map { it.currency } intersect _mainViewStateFlow.value.exchangeRates.map { it.currency }
                                    .toSet()
                            }
                        _mainViewStateFlow.value = _mainViewStateFlow.value.copy(
                            isLoading = false,
                            balances = balances,
                            sellableCurrencies = sellableCurrencies
                        )
                    }

                    is Result.Error -> {
                        _mainViewStateFlow.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                        _mainViewEffectFlow.update {
                            it.copy(
                                errorMessage = result.exception.message
                            )
                        }
                    }

                    is Result.Loading -> {
                        _mainViewStateFlow.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                }
            }
        }
    }

    fun updateSellFromCurrency(selectedCurrency: String) {
        _mainViewStateFlow.value = _mainViewStateFlow.value.copy(
            fromCurrency = selectedCurrency
        )
    }

    fun updateAmount(amount: String) {
        _mainViewStateFlow.value = _mainViewStateFlow.value.copy(
            amountText = amount
        )
    }
}