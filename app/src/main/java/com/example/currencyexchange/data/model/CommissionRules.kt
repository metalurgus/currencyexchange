package com.example.currencyexchange.data.model

import java.util.Date


data class CommissionRule(
    val ruleName: String = "",
    val ruleDescription: String = "",
    val rawFee: Double = 0.0,
    val partialFee: Double = 0.0,
    val dateRange: DateRange = ALL_DATES,
    val amountRange: AmountRange = ALL_AMOUNTS,
    val fromCurrencies: CurrencyList = ALL_CURRENCIES,
    val toCurrencies: CurrencyList = ALL_CURRENCIES,
    val transactionNumberRange: NumberRange = ALL_NUMBERS,
    val priority: Int = 0
) {
    companion object {
        val ALL_DATES = FromToDateRange(Date(0), Date(Long.MAX_VALUE))
        val ALL_AMOUNTS = FromToAmountRange(0.0, Double.MAX_VALUE)
        val ALL_NUMBERS = FromToNumberRange(0, Int.MAX_VALUE)
        val ALL_CURRENCIES = AllCurrencies()

        val DEFAULT_RULE = CommissionRule( priority = -1)
    }

    fun getFee(amount: Double): Double {
        return if (partialFee > 0) {
            amount * partialFee
        } else {
            rawFee
        }
    }
}

open class CurrencyList(private val currencies: List<String>) {
    open fun contains(currency: String): Boolean {
        return currencies.contains(currency)

    }
}

class AllCurrencies : CurrencyList(emptyList()) {
    override fun contains(currency: String): Boolean {
        return true
    }
}

abstract class NumberRange {
    abstract fun isInRange(number: Int): Boolean
}

class FromToNumberRange(
    private val fromNumber: Int,
    private val toNumber: Int
) : NumberRange() {
    override fun isInRange(number: Int): Boolean {
        return number in fromNumber..toNumber
    }
}

class FromNumberRange(private val fromNumber: Int) : NumberRange() {
    override fun isInRange(number: Int): Boolean {
        return number >= fromNumber
    }
}

class ToNumberRange(private val toNumber: Int) : NumberRange() {
    override fun isInRange(number: Int): Boolean {
        return number <= toNumber
    }
}

class EveryNthNumberRange(private val n: Int) : NumberRange() {
    override fun isInRange(number: Int): Boolean {
        return number % n == 0
    }
}

abstract class AmountRange {
    abstract fun isInRange(amount: Double): Boolean
}

class FromToAmountRange(
    private val fromAmount: Double,
    private val toAmount: Double
) : AmountRange() {
    override fun isInRange(amount: Double): Boolean {
        return amount in fromAmount..toAmount
    }
}

class FromAmountRange(private val fromAmount: Double) : AmountRange() {
    override fun isInRange(amount: Double): Boolean {
        return amount >= fromAmount
    }
}

class ToAmountRange(private val toAmount: Double) : AmountRange() {
    override fun isInRange(amount: Double): Boolean {
        return amount <= toAmount
    }
}

abstract class DateRange {
    abstract fun isInRange(date: Date): Boolean
}

class FromToDateRange(
    private val fromDate: Date,
    private val toDate: Date
) : DateRange() {
    override fun isInRange(date: Date): Boolean {
        return date in fromDate..toDate
    }
}

class FromDateRange(private val fromDate: Date) : DateRange() {
    override fun isInRange(date: Date): Boolean {
        return date >= fromDate
    }
}

class ToDateRange(private val toDate: Date) : DateRange() {


    override fun isInRange(date: Date): Boolean {
        return date <= toDate
    }
}

class ExactDateRange(private val exactDates: List<Date>) : DateRange() {
    override fun isInRange(date: Date): Boolean {
        return date in exactDates
    }
}

