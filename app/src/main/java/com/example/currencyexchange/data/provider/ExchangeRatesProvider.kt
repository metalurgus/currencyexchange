package com.example.currencyexchange.data.provider

import com.example.currencyexchange.data.model.response.ExchangeRatesResponse


interface ExchangeRatesProvider {
    fun getExchangeRate(fromCurrency: String, toCurrency: String): Double?
    fun setExchangeRates(newExchangeRates: ExchangeRatesResponse)
}

