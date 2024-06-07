package com.example.currencyexchange.data.usecase

import com.example.currencyexchange.data.model.TransactionRecord

abstract class GetUserTransactionHistoryUseCase : UseCase<List<TransactionRecord>, UseCase.None>()

