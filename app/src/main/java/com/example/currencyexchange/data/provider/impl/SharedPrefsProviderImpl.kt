package com.example.currencyexchange.data.provider.impl

import android.content.Context
import com.example.currencyexchange.data.model.Balance
import com.example.currencyexchange.data.provider.SharedPrefsProvider
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
        val balancesJson = prefs.getString(BALANCES_KEY, null)
        val result = ObservableMutableList<Balance>()

        result.addObserver { list ->
            prefs.edit().putString(BALANCES_KEY, gson.toJson(list)).apply()
        }

        if (balancesJson != null) {
            result.addAll(gson.fromJson(balancesJson, Array<Balance>::class.java))
        } else {
            result.add(DEFAULT_BALANCE)
        }



        result
    }

    override fun getAllBalances(): List<Balance> {
        return balances
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
