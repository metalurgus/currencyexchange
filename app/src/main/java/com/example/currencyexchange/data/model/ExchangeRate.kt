package com.example.currencyexchange.data.model

import com.example.currencyexchange.data.util.floorTo2DecimalPlaces
import java.util.Locale

data class ExchangeRate(
    val currency: String,
    val rate: Double
) {
    override fun toString(): String {
        return String.format(Locale.getDefault(), "%s: %.2f", currency, rate.floorTo2DecimalPlaces())
    }
}
