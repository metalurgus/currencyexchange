package com.example.currencyexchange.util

import com.example.currencyexchange.data.util.floorTo2DecimalPlaces
import java.util.Locale

fun Double.toCurrencyString(): String {
    return String.format(Locale.getDefault(), "%.2f", this.floorTo2DecimalPlaces())
}

fun String.replaceComaWithDot(): String {
    return this.replace(',', '.')
}