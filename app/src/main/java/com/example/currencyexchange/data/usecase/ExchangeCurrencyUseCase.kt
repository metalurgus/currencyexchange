package com.example.currencyexchange.data.usecase

abstract class ExchangeCurrencyUseCase :
    UseCase<String, ExchangeCurrencyUseCase.Params>() {

    data class Params(
        val fromCurrency: String,
        val toCurrency: String,
        val amount: Double
    )
}

