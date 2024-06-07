package com.example.currencyexchange.data.usecase.impl

import com.example.currencyexchange.data.Result
import com.example.currencyexchange.data.model.ExchangeRate
import com.example.currencyexchange.data.repository.ExchangeRateRepository
import com.example.currencyexchange.data.successOrError
import com.example.currencyexchange.data.usecase.GetExchangeRateUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetExchangeRateUseCaseImpl(
    private val exchangeRateRepository: ExchangeRateRepository
) : GetExchangeRateUseCase() {
    override fun run(params: String): Flow<Result<ExchangeRate>> = flow {
        emit(Result.Loading)
        emit(successOrError {
            exchangeRateRepository.getExchangeRate(params)
        })
    }

}