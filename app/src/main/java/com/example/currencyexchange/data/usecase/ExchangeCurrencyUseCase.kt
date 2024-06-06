package com.example.currencyexchange.data.usecase

import com.example.currencyexchange.data.model.Balance
import com.example.currencyexchange.data.model.ExchangeRate

abstract class ExchangeCurrencyUseCase :
    UseCase<ExchangeCurrencyUseCase.Result, ExchangeCurrencyUseCase.Params>() {

    data class Params(
        val fromCurrencyExchangeRate: ExchangeRate,
        val toCurrencyExchangeRate: ExchangeRate,
        val amount: Double
    )

    data class Result(
        val fromCurrencyBalance: Balance,
        val toCurrencyBalance: Balance,
        val commissionFeeMessage: String
    )
}

