package com.example.currencyexchange.data.usecase.impl

import com.example.currencyexchange.data.api.CurrencyInteractionApi
import com.example.currencyexchange.data.usecase.ExchangeCurrencyUseCase

class ExchangeCurrencyUseCaseImpl(
    private val currencyInteractionApi: CurrencyInteractionApi
) : ExchangeCurrencyUseCase() {

    override suspend fun run(params: Params): Result {
        val response = currencyInteractionApi.exchange(
            params.fromCurrencyExchangeRate.currency,
            params.toCurrencyExchangeRate.currency,
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