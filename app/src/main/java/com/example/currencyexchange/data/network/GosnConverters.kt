package com.example.currencyexchange.data.network


import com.example.currencyexchange.data.model.Rate
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class RateListDeserializer : JsonDeserializer<List<Rate>> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ) = json.asJsonObject.entrySet().map {
        Rate(it.key, it.value.asDouble)
    }
}
