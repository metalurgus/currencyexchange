package com.example.currencyexchange.data.usecase

import com.example.currencyexchange.data.model.Balance

abstract class GetUserBalancesUseCase : UseCase<List<Balance>, UseCase.None>()

