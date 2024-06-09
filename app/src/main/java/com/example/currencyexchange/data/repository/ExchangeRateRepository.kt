package com.example.currencyexchange.data.repository

import com.example.currencyexchange.data.api.CurrencyRateApi
import com.example.currencyexchange.data.model.ExchangeRate
import com.example.currencyexchange.data.model.response.ExchangeRatesResponse
import com.example.currencyexchange.data.provider.ExchangeRatesProvider
import com.example.currencyexchange.data.util.bodyOrThrow

class ExchangeRateRepositoryImpl(
    private val currencyRateApi: CurrencyRateApi,
    private val exchangeRatesProvider: ExchangeRatesProvider
) :
    ExchangeRateRepository {
    override suspend fun getExchangeRates(): ExchangeRatesResponse {
        val body = currencyRateApi.getExchangeRates().bodyOrThrow
        exchangeRatesProvider.setExchangeRates(body)
        return body
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