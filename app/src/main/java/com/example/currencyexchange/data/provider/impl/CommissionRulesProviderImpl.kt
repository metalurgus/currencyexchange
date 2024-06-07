package com.example.currencyexchange.data.provider.impl

import com.example.currencyexchange.data.model.CommissionRule
import com.example.currencyexchange.data.model.CurrencyList
import com.example.currencyexchange.data.model.EveryNthNumberRange
import com.example.currencyexchange.data.model.FromNumberRange
import com.example.currencyexchange.data.model.FromToDateRange
import com.example.currencyexchange.data.model.ToNumberRange
import com.example.currencyexchange.data.provider.CommissionRulesProvider
import com.example.currencyexchange.data.util.createDate

class CommissionRulesProviderImpl : CommissionRulesProvider {
    override fun getCommissionRules(
        fromCurrency: String,
        toCurrency: String
    ): List<CommissionRule> {
        return listOf(
            CommissionRule(
                ruleName = "First 5 transactions",
                ruleDescription = "First 5 transactions are free of charge",
                transactionNumberRange = ToNumberRange(5),
                priority = Int.MAX_VALUE,
            ),
            CommissionRule(
                ruleName = "From 6th transaction",
                ruleDescription = "Starting from 6th transaction partial fee is 5%",
                partialFee = 0.05,
                transactionNumberRange = FromNumberRange(6),
            ),
            CommissionRule(
                ruleName = "Every 10th transaction",
                ruleDescription = "Every 10th transaction commission fee is raw at 1 source currency",
                rawFee = 1.0,
                transactionNumberRange = EveryNthNumberRange(10),
                priority = 1
            ),
            CommissionRule(
                ruleName = "EUR to UAH",
                ruleDescription = "Commission fee for EUR to UAH is 0.5%",
                partialFee = 0.005,
                fromCurrencies = CurrencyList(listOf("EUR")),
                toCurrencies = CurrencyList(listOf("UAH")),
                priority = 2
            ),
            CommissionRule(
                ruleName = "PROMO: 08.06.2020 - 10.06.2020",
                ruleDescription = "Promo commission fee is 0.1%",
                partialFee = 0.001,
                dateRange = FromToDateRange(
                    createDate(2020, 6, 8),
                    createDate(2020, 6, 10),
                ),
                priority = Int.MAX_VALUE - 1,
            ),
        ).filter { rule ->
            rule.fromCurrencies.contains(fromCurrency) &&
                    rule.toCurrencies.contains(toCurrency)
        }
    }
}