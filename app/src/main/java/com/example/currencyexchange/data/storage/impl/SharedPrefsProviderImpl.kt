package com.example.currencyexchange.data.storage.impl

import android.content.Context
import com.example.currencyexchange.data.model.Balance
import com.example.currencyexchange.data.storage.SharedPrefsProvider
import com.example.currencyexchange.data.util.ObservableMutableList
import com.google.gson.Gson
import org.koin.core.context.GlobalContext

class SharedPrefsProviderImpl(private val gson: Gson) : SharedPrefsProvider {
    companion object {
        private const val BALANCES_KEY = "balances"
        private val DEFAULT_BALANCE = Balance("EUR", 1000.0)
    }


    private val context: Context = GlobalContext.get().get()
    private val prefs =
        context.getSharedPreferences(SharedPrefsProvider::class.java.name, Context.MODE_PRIVATE)

    private val balances: ObservableMutableList<Balance> by lazy {
        //TODO: initialize by lazy or somehow else
        val balancesJson = prefs.getString(BALANCES_KEY, null)
        if (balancesJson != null) {
            val result = ObservableMutableList<Balance>()
            result.addAll(gson.fromJson(balancesJson, Array<Balance>::class.java))
            result
        } else {
            saveBalances(listOf(DEFAULT_BALANCE))
            balances
        }
    }

    override fun getAllBalances(): List<Balance> {
        return emptyList()
    }

    override fun saveBalances(newBalances: List<Balance>) {
        balances.clear()
        balances.addAll(newBalances)
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
}
