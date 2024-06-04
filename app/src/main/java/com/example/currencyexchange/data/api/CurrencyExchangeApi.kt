package com.example.currencyexchange.data.api

import com.example.currencyexchange.data.model.response.ExchangeOperationResponse
import retrofit2.Response

interface CurrencyExchangeApi {
    suspend fun exchange(fromCurrency: String, toCurrency: String, amount: Double): Response<ExchangeOperationResponse>
}