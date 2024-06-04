package com.example.currencyexchange.data.usecase

import com.example.currencyexchange.data.model.Rate

abstract class GetExchangeRatesUseCase : UseCase<List<Rate>, UseCase.None>()

