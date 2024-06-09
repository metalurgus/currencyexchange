package com.example.currencyexchange.data.repository

import com.example.currencyexchange.data.api.CurrencyInteractionApi
import com.example.currencyexchange.data.model.response.ExchangeOperationResponse
import com.example.currencyexchange.data.util.bodyOrThrow

class CurrencyExchangeRepositoryImpl(private val currencyInteractionApi: CurrencyInteractionApi): CurrencyExchangeRepository {
    override suspend fun exchange(fromCurrency: String, toCurrency: String, amount: Double): ExchangeOperationResponse {
        return currencyInteractionApi.exchange(fromCurrency, toCurrency, amount).bodyOrThrow
    }

    override suspend fun previewExchange(fromCurrency: String, toCurrency: String, amount: Double): ExchangeOperationResponse {
        return currencyInteractionApi.previewExchange(fromCurrency, toCurrency, amount).bodyOrThrow
    }
}

interface CurrencyExchangeRepository {
    suspend fun exchange(
        fromCurrency: String,
        toCurrency: String,
        amount: Double
    ): ExchangeOperationResponse

    suspend fun previewExchange(
        fromCurrency: String,
        toCurrency: String,
        amount: Double
    ): ExchangeOperationResponse
}