package com.example.currencyexchange.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyexchange.data.Result
import com.example.currencyexchange.data.model.Balance
import com.example.currencyexchange.data.model.ExchangeRate
import com.example.currencyexchange.data.usecase.ExchangeCurrencyUseCase
import com.example.currencyexchange.data.usecase.GetExchangeRatesUseCase
import com.example.currencyexchange.data.usecase.GetUserBalancesUseCase
import com.example.currencyexchange.data.usecase.PreviewExchangeCurrencyUseCase
import com.example.currencyexchange.data.usecase.UseCase
import com.example.currencyexchange.util.multilet
import com.example.currencyexchangetesttask.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val getExchangeRatesUseCase: GetExchangeRatesUseCase,
    private val getUserBalancesUseCase: GetUserBalancesUseCase,
    private val previewExchangeCurrencyUseCase: PreviewExchangeCurrencyUseCase,
    private val exchangeCurrencyUseCase: ExchangeCurrencyUseCase,
    private val getString: (Int) -> String
) : ViewModel() {

    private val _mainViewStateFlow = MutableStateFlow(MainViewState())
    val viewState = _mainViewStateFlow.asStateFlow()

    private val _mainViewEffectFlow = MutableSharedFlow<MainViewEffect>(replay = 0)
    val viewEffect = _mainViewEffectFlow.asSharedFlow()

    private var isUpdatingExchangeRates = false

    data class MainViewState(
        val isLoading: Boolean = false,
        val fromCurrency: String? = null,
        val toCurrency: String? = null,
        val amountText: String = "",
        val exchangeRate: Double = 0.0,
        val exchangeResult: Double = 0.0,
        val balances: List<Balance> = emptyList(),
        val exchangeRates: List<ExchangeRate> = emptyList(),
        val sellableCurrencies: Collection<String> = emptyList(),
        val buyableCurrencies: Collection<String> = emptyList(),
        val balancesError: Boolean = false,
        val exchangeRatesError: Boolean = false,
        val exchangeEnabled: Boolean = false
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as MainViewState

            if (isLoading != other.isLoading) return false
            if (fromCurrency != other.fromCurrency) return false
            if (toCurrency != other.toCurrency) return false
            if (amountText != other.amountText) return false
            if (exchangeRate != other.exchangeRate) return false
            if (exchangeResult != other.exchangeResult) return false
            if (balances != other.balances) return false
            if (exchangeRates != other.exchangeRates) return false
            if (sellableCurrencies != other.sellableCurrencies) return false
            if (buyableCurrencies != other.buyableCurrencies) return false
            if (balancesError != other.balancesError) return false
            if (exchangeRatesError != other.exchangeRatesError) return false
            if (exchangeEnabled != other.exchangeEnabled) return false

            return true
        }
    }

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

                        val buyableCurrencies =
                            exchangeRatesResponse
                                .rates
                                .map { it.currency }

                        _mainViewStateFlow.value = _mainViewStateFlow.value.copy(
                            exchangeRates = exchangeRatesResponse.rates,
                            sellableCurrencies = sellableCurrencies,
                            buyableCurrencies = buyableCurrencies,
                            exchangeRatesError = false
                        )
                    }

                    is Result.Error -> {
                        _mainViewStateFlow.value = _mainViewStateFlow.value.copy(
                            exchangeRatesError = true
                        )
                    }

                    else -> Unit
                }
            }
        }
    }

    fun updateBalances() {
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
                            sellableCurrencies = sellableCurrencies,
                            balancesError = false
                        )
                    }

                    is Result.Error -> {
                        _mainViewStateFlow.update {
                            it.copy(
                                isLoading = false,
                                balancesError = true
                            )
                        }
                        _mainViewEffectFlow.emit(
                            MainViewEffect(
                                errorMessage = getString(R.string.failed_to_load_balances)
                            )
                        )

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

    fun updateFromCurrency(selectedCurrency: String) {
        _mainViewStateFlow.value = _mainViewStateFlow.value.copy(
            fromCurrency = selectedCurrency
        )
        previewExchange()
        updateExchangeEnabled()
    }

    fun updateAmount(amount: String) {
        _mainViewStateFlow.value = _mainViewStateFlow.value.copy(
            amountText = amount
        )
        previewExchange()
        updateExchangeEnabled()
    }

    fun updateToCurrency(selectedCurrency: String) {
        _mainViewStateFlow.value = _mainViewStateFlow.value.copy(
            toCurrency = selectedCurrency
        )
        previewExchange()
        updateExchangeEnabled()
    }

    private fun previewExchange() {
        multilet(
            _mainViewStateFlow.value.fromCurrency,
            _mainViewStateFlow.value.toCurrency
        ) { fromCurrency, toCurrency ->
            val amountText = _mainViewStateFlow.value.amountText
            if (amountText.isNotEmpty()) {
                viewModelScope.launch {
                    previewExchangeCurrencyUseCase.run(
                        PreviewExchangeCurrencyUseCase.Params(
                            fromCurrency,
                            toCurrency,
                            amountText.toDouble()
                        )
                    ).collect { result ->
                        when (result) {
                            is Result.Success -> {
                                _mainViewStateFlow.value = _mainViewStateFlow.value.copy(
                                    exchangeResult = result.data
                                )
                            }

                            is Result.Error -> {
                                _mainViewEffectFlow.emit(
                                    MainViewEffect(
                                        errorMessage = getString(R.string.failed_to_load_exchange_details)
                                    )
                                )
                            }

                            else -> Unit
                        }
                    }
                }
            } else {
                _mainViewStateFlow.value = _mainViewStateFlow.value.copy(
                    exchangeResult = 0.0
                )
            }
        }
    }

    private fun updateExchangeEnabled() {
        var exchangeEnabled = true
        with(_mainViewStateFlow.value) {
            val sourceBalanceValue = this.balances.find { it.currency == fromCurrency }?.amount
            if (sourceBalanceValue == null) {
                exchangeEnabled = false
                return@with
            }
            val amount = try {
                amountText.toDouble()
            } catch (e: NumberFormatException) {
                exchangeEnabled = false
                return@with
            }
            if (amount > sourceBalanceValue) {
                exchangeEnabled = false
                return@with
            }
        }
        _mainViewStateFlow.update {
            it.copy(
                exchangeEnabled = exchangeEnabled
            )
        }
    }

    fun exchange() {
        viewModelScope.launch {
            val fromCurrency = _mainViewStateFlow.value.fromCurrency
            val toCurrency = _mainViewStateFlow.value.toCurrency
            val amount = _mainViewStateFlow.value.amountText.toDouble()
            multilet(fromCurrency, toCurrency) { fromCurrencyNotNull, toCurrencyNotNull ->
                exchangeCurrencyUseCase.run(
                    ExchangeCurrencyUseCase.Params(
                        fromCurrencyNotNull,
                        toCurrencyNotNull,
                        amount
                    )
                ).collect { result ->
                    when (result) {
                        is Result.Success -> {
                            _mainViewEffectFlow.emit(
                                MainViewEffect(
                                    message = String.format(
                                        getString(R.string.exchange_syccessful),
                                        result.data
                                    )
                                )
                            )
                            _mainViewStateFlow.update {
                                it.copy(
                                    amountText = "",
                                    exchangeResult = 0.0,
                                    isLoading = false
                                )
                            }
                            updateBalances()
                        }

                        is Result.Error -> {
                            _mainViewEffectFlow.emit(
                                MainViewEffect(
                                    errorMessage = getString(R.string.exchange_failed)
                                )
                            )
                            _mainViewStateFlow.update {
                                it.copy(
                                    isLoading = false
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
    }
}