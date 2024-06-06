package com.example.currencyexchange.data.provider

import com.example.currencyexchange.data.provider.impl.ExchangeRatesProviderImpl
import com.example.currencyexchange.data.provider.impl.SharedPrefsProviderImpl
import org.koin.dsl.module


val providerModule = module {
    single<SharedPrefsProvider> { SharedPrefsProviderImpl(get(), get()) }
    single<ExchangeRatesProvider> { ExchangeRatesProviderImpl() }
    single<CommissionRulesProvider> { CommissionRulesProviderImpl() }
}