package com.example.currencyexchange.data.api

import com.example.currencyexchange.data.model.Balance
import com.example.currencyexchange.data.model.TransactionRecord
import com.example.currencyexchange.data.model.response.ExchangeOperationResponse
import com.example.currencyexchange.data.model.response.GetCommissionFeeForTransactionResponse
import retrofit2.Response

interface CurrencyInteractionApi {
    suspend fun exchange(
        fromCurrency: String,
        toCurrency: String,
        amount: Double
    ): Response<ExchangeOperationResponse>

    suspend fun getBalances(): Response<List<Balance>>

    suspend fun getBalance(currency: String): Response<Balance>
    suspend fun getCommissionFeeForTransaction(
        fromCurrency: String,
        toCurrency: String,
        amount: Double,
        transactionNumber: Int
    ): Response<GetCommissionFeeForTransactionResponse>

    suspend fun getTransactionHistory(): Response<List<TransactionRecord>>
    fun getOperationNumber(): Response<Int>
}