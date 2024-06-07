package com.example.currencyexchange.data.usecase.impl

import com.example.currencyexchange.data.usecase.ExchangeCurrencyUseCase
import com.example.currencyexchange.data.Result
import com.example.currencyexchange.data.repository.CurrencyExchangeRepository
import com.example.currencyexchange.data.successOrError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExchangeCurrencyUseCaseImpl(
    private val repository: CurrencyExchangeRepository,
) : ExchangeCurrencyUseCase() {
    override fun run(params: Params): Flow<Result<String>> = flow {
        emit(Result.Loading)
        emit(successOrError {
            val result = repository.exchange(
                params.fromCurrency,
                params.toCurrency,
                params.amount
            )
            //TODO: update balance
            result.commissionFeeMessage
        })

    }
}