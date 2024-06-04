package com.example.currencyexchange.data.usecase.impl

import com.example.currencyexchange.data.model.Rate
import com.example.currencyexchange.data.api.CurrencyRateApi
import com.example.currencyexchange.data.usecase.GetExchangeRatesUseCase

class GetExchangeRatesUseCaseImpl(
    private val currencyRateApi: CurrencyRateApi
) : GetExchangeRatesUseCase() {

    override suspend fun run(params: None): List<Rate> {
        val response = currencyRateApi.getExchangeRates()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Error getting exchange rates", Exception(response.message()))
        }
    }
}