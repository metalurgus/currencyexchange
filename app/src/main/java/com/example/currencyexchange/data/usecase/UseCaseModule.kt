package com.example.currencyexchange.data.usecase

import com.example.currencyexchange.data.usecase.impl.ExchangeCurrencyUseCaseImpl
import com.example.currencyexchange.data.usecase.impl.GetExchangeRatesUseCaseImpl
import org.koin.dsl.module


val useCaseModule = module {
    single<GetExchangeRatesUseCase> { GetExchangeRatesUseCaseImpl(get()) }
    single<ExchangeCurrencyUseCase> { ExchangeCurrencyUseCaseImpl(get()) }
}