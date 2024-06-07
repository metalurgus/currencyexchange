package com.example.currencyexchange.data.repository

import com.example.currencyexchange.data.api.CurrencyInteractionApi
import com.example.currencyexchange.data.model.Balance
import com.example.currencyexchange.data.model.TransactionRecord
import com.example.currencyexchange.data.util.bodyOrThrow

class UserDataRepositoryImpl(private val currencyInteractionApi: CurrencyInteractionApi) :
    UserDataRepository {
    override suspend fun getBalances(): List<Balance> {
        return currencyInteractionApi.getBalances().bodyOrThrow
    }

    override suspend fun getBalance(currency: String): Balance {
        return currencyInteractionApi.getBalance(currency).bodyOrThrow
    }

    override suspend fun getOperationNumber(): Int {
        return currencyInteractionApi.getOperationNumber().bodyOrThrow
    }

    override suspend fun getTransactionHistory(): List<TransactionRecord> {
        return currencyInteractionApi.getTransactionHistory().bodyOrThrow
    }
}

interface UserDataRepository {
    suspend fun getBalances(): List<Balance>
    suspend fun getBalance(currency: String): Balance
    suspend fun getOperationNumber(): Int
    suspend fun getTransactionHistory(): List<TransactionRecord>
}