package com.example.currencyexchangetesttask.data

import com.example.currencyexchange.data.model.Balance
import com.example.currencyexchange.data.model.TransactionRecord
import com.example.currencyexchange.data.provider.SharedPrefsProvider

class SharedPrefsProviderTestImpl : SharedPrefsProvider {

    private var transactionNumber: Int = 1
    private val balances = mutableListOf(Balance("EUR", 1000.0))
    private val _transactionHistory = mutableListOf<TransactionRecord>()


    override fun getAllBalances(): List<Balance> {
        return balances
    }

    override fun saveBalances(newBalances: List<Balance>) {
        balances.clear()
        balances.addAll(balances)
    }

    override fun getBalance(currency: String): Balance? {
        return balances.find { it.currency == currency }
    }

    override fun saveBalance(balance: Balance) {
        val index = balances.indexOfFirst { it.currency == balance.currency }
        if (index != -1) {
            balances[index] = balance
        } else {
            balances.add(balance)
        }
    }

    override fun getTransactionNumber(): Int {
        return transactionNumber
    }

    override fun increaseTransactionNumber() {
        this.transactionNumber++
    }

    override fun getTransactionHistory(): List<TransactionRecord> {
        return _transactionHistory
    }

    override fun saveTransactionRecord(transactionRecord: TransactionRecord) {
        _transactionHistory.add(transactionRecord)
    }

}
