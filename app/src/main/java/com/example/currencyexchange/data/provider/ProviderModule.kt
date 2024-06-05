package com.example.currencyexchange.data.provider

import com.example.currencyexchange.data.provider.impl.ExchangeRatesProviderImpl
import com.example.currencyexchange.data.provider.impl.SharedPrefsProviderImpl
import com.google.gson.Gson
import org.koin.dsl.module

fun provideExchangeRatesProvider(): ExchangeRatesProvider {
    return ExchangeRatesProviderImpl()
}

fun provideSharedPrefsProvider(gson: Gson): SharedPrefsProvider {
    return SharedPrefsProviderImpl(gson)
}

val providerModule = module {
    single { provideExchangeRatesProvider() }
    single { provideSharedPrefsProvider(get()) }
}