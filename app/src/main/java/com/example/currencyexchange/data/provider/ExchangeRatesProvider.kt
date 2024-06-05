package com.example.currencyexchange.data.provider

import com.example.currencyexchange.data.model.Rate


interface ExchangeRatesProvider {
    fun getExchangeRate(fromCurrency: String, toCurrency: String): Double?
    fun setExchangeRates(newExchangeRates: List<Rate>)
}

