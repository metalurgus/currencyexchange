package com.example.currencyexchange.data.usecase.impl

import com.example.currencyexchange.data.api.CurrencyRateApi
import com.example.currencyexchange.data.model.response.ExchangeRatesResponse
import com.example.currencyexchange.data.usecase.GetExchangeRatesUseCase

class GetExchangeRatesUseCaseImpl(
    private val currencyRateApi: CurrencyRateApi
) : GetExchangeRatesUseCase() {

    override suspend fun run(params: None): ExchangeRatesResponse {
        val response = currencyRateApi.getExchangeRates()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception(
                "Error getting exchange rates",
                Exception("Response body is null")
            )
        } else {
            throw Exception("Error getting exchange rates", Exception(response.message()))
        }
    }
}