package com.example.currencyexchange.data.storage

import com.example.currencyexchange.data.model.Balance

interface SharedPrefsProvider {
    fun getAllBalances(): List<Balance>
    fun saveBalances(newBalances: List<Balance>)
    fun getBalance(currency: String): Balance?
    fun saveBalance(balance: Balance)
}