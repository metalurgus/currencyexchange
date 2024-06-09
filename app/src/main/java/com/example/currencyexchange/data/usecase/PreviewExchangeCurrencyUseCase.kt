package com.example.currencyexchange.data.usecase

abstract class PreviewExchangeCurrencyUseCase :
    UseCase<Double, PreviewExchangeCurrencyUseCase.Params>() {

    data class Params(
        val fromCurrency: String,
        val toCurrency: String,
        val amount: Double
    )
}

