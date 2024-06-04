package com.example.currencyexchange.data.usecase

import com.example.currencyexchange.data.api.CurrencyExchangeApi
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

class ExchangeCurrencyUseCaseImpl(
    private val currencyExchangeApi: CurrencyExchangeApi
) : ExchangeCurrencyUseCase() {

    override suspend fun run(params: Params): Result {
        val response = currencyExchangeApi.exchange(
            params.fromCurrencyRate.currency,
            params.toCurrencyRate.currency,
            params.amount
        )
        if (response.isSuccessful) {
            with(response.body()) {
                if (this == null) {
                    throw Exception("Error exchanging currency", Exception("Response body is null"))
                }
                return Result(
                    fromBalance, toBalance, commissionFeeMessage
                )
            }
        } else {
            throw Exception("Error exchanging currency", Exception(response.message()))
        }
    }
}