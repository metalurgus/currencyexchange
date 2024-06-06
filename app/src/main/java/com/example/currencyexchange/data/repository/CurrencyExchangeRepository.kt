package com.example.currencyexchange.data.repository

import com.example.currencyexchange.data.api.CurrencyInteractionApi

class CurrencyExchangeRepositoryImpl(private val currencyInteractionApi: CurrencyInteractionApi): CurrencyExchangeRepository {
    override suspend fun exchange(fromCurrency: String, toCurrency: String, amount: Double): String {
        return currencyInteractionApi.exchange(fromCurrency, toCurrency, amount).bodyOrThrow
    }
}

interface CurrencyExchangeRepository {
    suspend fun exchange(
        fromCurrency: String,
        toCurrency: String,
        amount: Double
    ): String
}