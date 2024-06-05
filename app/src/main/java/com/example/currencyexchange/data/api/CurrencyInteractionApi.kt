package com.example.currencyexchange.data.api

import com.example.currencyexchange.data.model.Balance
import com.example.currencyexchange.data.model.response.ExchangeOperationResponse
import retrofit2.Response

interface CurrencyInteractionApi {
    suspend fun exchange(fromCurrency: String, toCurrency: String, amount: Double): Response<ExchangeOperationResponse>
    suspend fun getBalances(): Response<List<Balance>>
}