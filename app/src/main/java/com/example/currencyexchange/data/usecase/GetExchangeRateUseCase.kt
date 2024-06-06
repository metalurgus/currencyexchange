package com.example.currencyexchange.data.usecase

import com.example.currencyexchange.data.model.ExchangeRate
import com.example.currencyexchange.data.model.response.ExchangeRatesResponse
import com.example.currencyexchange.data.repository.ExchangeRateRepository

abstract class GetExchangeRateUseCase : UseCase<ExchangeRate, String>()

class GetExchangeRateUseCaseImpl(
    private val exchangeRateRepository: ExchangeRateRepository
) : GetExchangeRateUseCase() {

    override suspend fun run(params: String): ExchangeRate {
        return exchangeRateRepository.getExchangeRate(params)
    }
}