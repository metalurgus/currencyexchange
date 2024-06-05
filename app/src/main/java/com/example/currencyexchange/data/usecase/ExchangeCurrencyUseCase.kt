package com.example.currencyexchange.data.usecase

import com.example.currencyexchange.data.model.Balance
import com.example.currencyexchange.data.model.Rate

abstract class ExchangeCurrencyUseCase :
    UseCase<ExchangeCurrencyUseCase.Result, ExchangeCurrencyUseCase.Params>() {

    data class Params(
        val fromCurrencyRate: Rate,
        val toCurrencyRate: Rate,
        val amount: Double
    )

    data class Result(
        val fromCurrencyBalance: Balance,
        val toCurrencyBalance: Balance,
        val commissionFeeMessage: String
    )
}

