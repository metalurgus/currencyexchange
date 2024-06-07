package com.example.currencyexchange.data.repository

import org.koin.dsl.module

val repositoryModule = module {
    single<UserDataRepository> { UserDataRepositoryImpl(get()) }
    single<ExchangeRateRepository> { ExchangeRateRepositoryImpl(get()) }
    single<CurrencyExchangeRepository> { CurrencyExchangeRepositoryImpl(get()) }

}