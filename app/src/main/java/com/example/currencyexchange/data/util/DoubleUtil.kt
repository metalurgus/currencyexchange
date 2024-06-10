package com.example.currencyexchange.data.util

import kotlin.math.floor

fun Double.floorTo2DecimalPlaces(): Double {
    return floor(this * 100) / 100
}