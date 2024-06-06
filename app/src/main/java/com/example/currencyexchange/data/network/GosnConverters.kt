package com.example.currencyexchange.data.network


import com.example.currencyexchange.data.model.ExchangeRate
import com.example.currencyexchange.data.model.response.ExchangeRatesResponse
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RateListDeserializer : JsonDeserializer<List<ExchangeRate>> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ) = json.asJsonObject.entrySet().map {
        ExchangeRate(it.key, it.value.asDouble)
    }
}

private fun String.parseAsDate(): Date {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return format.parse(this) ?: Date()
}

class ExchangeRatesResponseDeserializer : JsonDeserializer<ExchangeRatesResponse> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): ExchangeRatesResponse {
        val jsonObject = json.asJsonObject
        val base = ExchangeRate(jsonObject["base"].asString, 1.0)
        val date = jsonObject["date"].asString.parseAsDate()
        val rates = context.deserialize<List<ExchangeRate>>(
            jsonObject["rates"],
            object : TypeToken<List<ExchangeRate>>() {}.type
        )
        return ExchangeRatesResponse(base, date, rates)
    }
}
