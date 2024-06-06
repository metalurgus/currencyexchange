package com.example.currencyexchange.data.model.response

import com.example.currencyexchange.data.model.ExchangeRate
import java.util.Date

data class ExchangeRatesResponse(
    val base: ExchangeRate,
    val date: Date,
    val rates: List<ExchangeRate>
)
