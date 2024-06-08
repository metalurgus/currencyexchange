package com.example.currencyexchange.data.model

import java.util.Locale

data class ExchangeRate(
    val currency: String,
    val rate: Double
) {
    override fun toString(): String {
        return String.format(Locale.getDefault(), "%s: %.2f", currency, rate)
    }
}
