package com.example.currencyexchange.data.usecase.impl

import com.example.currencyexchange.data.Result
import com.example.currencyexchange.data.model.response.ExchangeRatesResponse
import com.example.currencyexchange.data.repository.ExchangeRateRepository
import com.example.currencyexchange.data.successOrError
import com.example.currencyexchange.data.usecase.GetExchangeRatesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetExchangeRatesUseCaseImpl(
    private val exchangeRateRepository: ExchangeRateRepository
) : GetExchangeRatesUseCase() {
    override fun run(params: None): Flow<Result<ExchangeRatesResponse>> = flow {
        emit(Result.Loading)
        emit(successOrError {
            exchangeRateRepository.getExchangeRates()
        })
    }


}