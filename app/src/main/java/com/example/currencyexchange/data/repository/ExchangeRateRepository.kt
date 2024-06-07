package com.example.currencyexchange.data.repository

import com.example.currencyexchange.data.api.CurrencyRateApi
import com.example.currencyexchange.data.model.ExchangeRate
import com.example.currencyexchange.data.model.response.ExchangeRatesResponse
import com.example.currencyexchange.data.util.bodyOrThrow

class ExchangeRateRepositoryImpl(private val currencyRateApi: CurrencyRateApi) :
    ExchangeRateRepository {
    override suspend fun getExchangeRates(): ExchangeRatesResponse {
        return currencyRateApi.getExchangeRates().bodyOrThrow
    }

    override suspend fun getExchangeRate(currency: String): ExchangeRate {
        return currencyRateApi.getExchangeRates().bodyOrThrow.rates.find { it.currency == currency }
            ?: throw IllegalArgumentException("Currency not found")
    }

}

interface ExchangeRateRepository {
    suspend fun getExchangeRates(): ExchangeRatesResponse
    suspend fun getExchangeRate(currency: String): ExchangeRate
}