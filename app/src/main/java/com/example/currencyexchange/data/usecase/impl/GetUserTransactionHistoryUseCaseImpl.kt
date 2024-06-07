package com.example.currencyexchange.data.usecase.impl

import com.example.currencyexchange.data.Result
import com.example.currencyexchange.data.repository.UserDataRepository
import com.example.currencyexchange.data.successOrError
import com.example.currencyexchange.data.usecase.GetUserTransactionHistoryUseCase
import kotlinx.coroutines.flow.flow

class GetUserTransactionHistoryUseCaseImpl(
    private val repository: UserDataRepository
) : GetUserTransactionHistoryUseCase() {
    override fun run(params: None) = flow {
        emit(Result.Loading)
        val request = successOrError {
            repository.getTransactionHistory()
        }
        emit(request)
    }
}