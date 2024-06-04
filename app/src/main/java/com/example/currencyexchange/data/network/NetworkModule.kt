package com.example.currencyexchange.data.network

import com.example.currencyexchange.data.api.CurrencyExchangeApi
import com.example.currencyexchange.data.api.CurrencyRateApi
import com.example.currencyexchange.data.model.Rate
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun provideGson(): Gson {
    return GsonBuilder()
        .registerTypeAdapter(object : TypeToken<List<Rate>>() {}.type, RateListDeserializer())
        .create()
}

fun provideRetrofit(gson: Gson): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://developers.paysera.com/tasks/api/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}

fun provideCurrencyRateApi(retrofit: Retrofit): CurrencyRateApi {
    return retrofit.create(CurrencyRateApi::class.java)
}

fun provideCurrencyExchangeApi(retrofit: Retrofit): CurrencyExchangeApi {
    return
}

val networkModule = module {
    single { provideGson() }
    single { provideRetrofit(get()) }
    single { provideCurrencyRateApi(get()) }
    single { provideCurrencyExchangeApi(get()) }
}