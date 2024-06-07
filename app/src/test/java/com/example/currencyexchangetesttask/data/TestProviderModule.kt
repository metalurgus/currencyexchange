package com.example.currencyexchangetesttask.data

import com.example.currencyexchange.data.provider.CommissionRulesProvider
import com.example.currencyexchange.data.provider.impl.CommissionRulesProviderImpl
import com.example.currencyexchange.data.provider.ExchangeRatesProvider
import com.example.currencyexchange.data.provider.SharedPrefsProvider
import com.example.currencyexchange.data.provider.impl.ExchangeRatesProviderImpl
import org.koin.dsl.module

val testProviderModule = module {
    single<ExchangeRatesProvider> { ExchangeRatesProviderImpl() }
    single<CommissionRulesProvider> { CommissionRulesProviderImpl() }
    single<SharedPrefsProvider> { SharedPrefsProviderTestImpl() }
}