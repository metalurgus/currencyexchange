package com.example.currencyexchange.data.model

data class TransactionRecord(
    val fromCurrency: String,
    val toCurrency: String,
    val amount: Double,
    val rate: Double,
    val transactionNumber: Int,
    val commissionFee: Double,
    val date: Long,
    val message: String,
    val isSuccess: Boolean
)
