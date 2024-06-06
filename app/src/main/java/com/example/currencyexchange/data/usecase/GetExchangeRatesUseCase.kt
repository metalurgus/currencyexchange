package com.example.currencyexchange.data.usecase

import com.example.currencyexchange.data.model.response.ExchangeRatesResponse

abstract class GetExchangeRatesUseCase : UseCase<ExchangeRatesResponse, UseCase.None>()

