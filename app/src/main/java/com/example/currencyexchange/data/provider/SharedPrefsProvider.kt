package com.example.currencyexchange.data.provider

import com.example.currencyexchange.data.model.Balance
import com.example.currencyexchange.data.model.TransactionRecord

interface SharedPrefsProvider {
    fun getAllBalances(): List<Balance>
    fun saveBalances(newBalances: List<Balance>)
    fun getBalance(currency: String): Balance?
    fun saveBalance(balance: Balance)

    fun getTransactionNumber(): Int
    fun increaseTransactionNumber()
    fun getTransactionHistory(): List<TransactionRecord>

    fun saveTransactionRecord(transactionRecord: TransactionRecord)
}