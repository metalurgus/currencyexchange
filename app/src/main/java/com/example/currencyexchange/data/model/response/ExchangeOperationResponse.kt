package com.example.currencyexchange.data.model.response

import com.example.currencyexchange.data.model.Balance

data class ExchangeOperationResponse(
    val fromBalance: Balance,
    val toBalance: Balance,
    val commissionFee: Double,
    val rate: Double,
    val commissionFeeMessage: String
)
