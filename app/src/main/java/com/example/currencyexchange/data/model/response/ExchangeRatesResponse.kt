package com.example.currencyexchange.data.model.response

import com.example.currencyexchange.data.model.Rate
import java.util.Date

data class ExchangeRatesResponse(
    val base: Rate,
    val date: Date,
    val rates: List<Rate>
)
