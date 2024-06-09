package com.example.currencyexchange.data.usecase

import com.example.currencyexchange.data.usecase.impl.ExchangeCurrencyUseCaseImpl
import com.example.currencyexchange.data.usecase.impl.GetExchangeRateUseCaseImpl
import com.example.currencyexchange.data.usecase.impl.GetExchangeRatesUseCaseImpl
import com.example.currencyexchange.data.usecase.impl.GetUserBalanceUseCaseImpl
import com.example.currencyexchange.data.usecase.impl.GetUserBalancesUseCaseImpl
import com.example.currencyexchange.data.usecase.impl.GetUserOperationNumberUseCaseImpl
import com.example.currencyexchange.data.usecase.impl.GetUserTransactionHistoryUseCaseImpl
import com.example.currencyexchange.data.usecase.impl.PreviewExchangeCurrencyUseCaseImpl
import org.koin.dsl.module


val useCaseModule = module {
    single<GetExchangeRatesUseCase> { GetExchangeRatesUseCaseImpl(get()) }
    single<GetExchangeRateUseCase> { GetExchangeRateUseCaseImpl(get()) }
    single<ExchangeCurrencyUseCase> { ExchangeCurrencyUseCaseImpl(get()) }
    single<PreviewExchangeCurrencyUseCase> { PreviewExchangeCurrencyUseCaseImpl(get()) }
    single<GetUserBalancesUseCase> { GetUserBalancesUseCaseImpl(get()) }
    single<GetUserBalanceUseCase> { GetUserBalanceUseCaseImpl(get()) }
    single<GetUserOperationNumberUseCase> { GetUserOperationNumberUseCaseImpl(get()) }
    single<GetUserTransactionHistoryUseCase> { GetUserTransactionHistoryUseCaseImpl(get()) }


}