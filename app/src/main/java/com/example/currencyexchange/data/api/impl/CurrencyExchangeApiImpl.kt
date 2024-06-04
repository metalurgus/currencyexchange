package com.example.currencyexchange.data.api.impl

import com.example.currencyexchange.data.api.CurrencyExchangeApi
import com.example.currencyexchange.data.model.response.ExchangeOperationResponse
import retrofit2.Response

class CurrencyExchangeApiImpl : CurrencyExchangeApi {
    override suspend fun exchange(
        fromCurrency: String,
        toCurrency: String,
        amount: Double
    ): Response<ExchangeOperationResponse> {

    }

}