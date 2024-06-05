package com.example.currencyexchange.data.provider.impl

import com.example.currencyexchange.data.model.Rate
import com.example.currencyexchange.data.provider.ExchangeRatesProvider

class ExchangeRatesProviderImpl : ExchangeRatesProvider {
    private val exchangeRates: MutableList<Rate> = mutableListOf()

    override fun getExchangeRate(fromCurrency: String, toCurrency: String): Double? {
        val fromRate = exchangeRates.firstOrNull { it.currency == fromCurrency }
        val toRate = exchangeRates.firstOrNull { it.currency == toCurrency }
        if (fromRate == null || toRate == null) {
            return null
        }
        return toRate.rate / fromRate.rate
    }

    override fun setExchangeRates(newExchangeRates: List<Rate>) {
        exchangeRates.clear()
        exchangeRates.addAll(newExchangeRates)
    }
}