package com.example.currencyexchange.data.usecase

import com.example.currencyexchange.data.api.CurrencyInteractionApi
import com.example.currencyexchange.data.api.CurrencyRateApi
import com.example.currencyexchange.data.usecase.impl.ExchangeCurrencyUseCaseImpl
import com.example.currencyexchange.data.usecase.impl.GetExchangeRatesUseCaseImpl
import org.koin.dsl.module

fun provideGetExchangeRatesUseCase(currencyRateApi: CurrencyRateApi): GetExchangeRatesUseCase {
    return GetExchangeRatesUseCaseImpl(currencyRateApi)
}

fun provideExchangeCurrencyUseCase(currencyInteractionApi: CurrencyInteractionApi): ExchangeCurrencyUseCase {
    return ExchangeCurrencyUseCaseImpl(currencyInteractionApi)
}

val useCaseModule = module {
    single { provideGetExchangeRatesUseCase(get()) }
    single { provideExchangeCurrencyUseCase(get()) }
}