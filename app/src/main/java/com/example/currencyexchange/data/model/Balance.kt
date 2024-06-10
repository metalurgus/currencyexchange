package com.example.currencyexchange.data.model

import com.example.currencyexchange.data.util.floorTo2DecimalPlaces
import java.util.Locale

data class Balance(
    val currency: String,
    val amount: Double
) {
    override fun toString(): String {
        return String.format(Locale.getDefault(), "%.2f %s", amount.floorTo2DecimalPlaces(), currency)
    }
}
