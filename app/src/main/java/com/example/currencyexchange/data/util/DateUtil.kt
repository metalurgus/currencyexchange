package com.example.currencyexchange.data.util

import java.util.Calendar
import java.util.Date

fun createDate(year: Int, month: Int, day: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month - 1) // Months are 0-based in Java
    calendar.set(Calendar.DAY_OF_MONTH, day)
    return calendar.time
}