package com.example.currencyexchange.data.provider

import com.example.currencyexchange.data.provider.impl.ExchangeRatesProviderImpl
import com.example.currencyexchange.data.provider.impl.SharedPrefsProviderImpl
import com.google.gson.Gson
import org.koin.dsl.module

fun provideSharedPrefsProvider(gson: Gson): SharedPrefsProvider {
    return SharedPrefsProviderImpl(gson)
}


val providerModule = module {
    single { provideSharedPrefsProvider(get()) }
    single<ExchangeRatesProvider> { ExchangeRatesProviderImpl() }
    single<CommissionRulesProvider> { CommissionRulesProviderImpl() }
}