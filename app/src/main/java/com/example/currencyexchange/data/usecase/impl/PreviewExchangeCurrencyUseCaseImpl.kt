package com.example.currencyexchange.data.usecase.impl

import com.example.currencyexchange.data.Result
import com.example.currencyexchange.data.repository.CurrencyExchangeRepository
import com.example.currencyexchange.data.successOrError
import com.example.currencyexchange.data.usecase.PreviewExchangeCurrencyUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PreviewExchangeCurrencyUseCaseImpl(
    private val repository: CurrencyExchangeRepository,
) : PreviewExchangeCurrencyUseCase() {
    override fun run(params: Params): Flow<Result<Double>> = flow {
        emit(Result.Loading)
        emit(successOrError {
            val result = repository.previewExchange(
                params.fromCurrency,
                params.toCurrency,
                params.amount
            )
            result.resultAmount
        })

    }
}