package com.example.currencyexchange.data.provider

import com.example.currencyexchange.data.model.CommissionRule

interface CommissionRulesProvider {
    fun getCommissionRules(fromCurrency: String, toCurrency: String): List<CommissionRule>
}

