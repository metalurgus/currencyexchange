package com.example.currencyexchange.data.provider.impl

import com.example.currencyexchange.data.model.ExchangeRate
import com.example.currencyexchange.data.model.response.ExchangeRatesResponse
import com.example.currencyexchange.data.provider.ExchangeRatesProvider

class ExchangeRatesProviderImpl : ExchangeRatesProvider {
    private var baseExchangeRate: ExchangeRate? = null
    private val exchangeRates: MutableList<ExchangeRate> = mutableListOf()

    override fun getExchangeRate(fromCurrency: String, toCurrency: String): Double? {
        val fromRate = exchangeRates.firstOrNull { it.currency == fromCurrency }
        val toRate = exchangeRates.firstOrNull { it.currency == toCurrency }
        if (fromRate == null || toRate == null) {
            return null
        }
        return toRate.rate / fromRate.rate
    }

    override fun setExchangeRates(newExchangeRates: ExchangeRatesResponse) {
        exchangeRates.clear()
        exchangeRates.addAll(newExchangeRates.rates)
        baseExchangeRate = newExchangeRates.base
    }
}