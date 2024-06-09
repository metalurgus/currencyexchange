package com.example.currencyexchange.util

import java.util.Locale

fun Double.toCurrencyString(): String {
    return String.format(Locale.getDefault(), "%.2f", this)
}