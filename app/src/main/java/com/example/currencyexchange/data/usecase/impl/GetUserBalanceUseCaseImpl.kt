package com.example.currencyexchange.data.usecase.impl

import com.example.currencyexchange.data.Result
import com.example.currencyexchange.data.model.Balance
import com.example.currencyexchange.data.repository.UserDataRepository
import com.example.currencyexchange.data.successOrError
import com.example.currencyexchange.data.usecase.GetUserBalanceUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetUserBalanceUseCaseImpl(private val repository: UserDataRepository) :
    GetUserBalanceUseCase() {
    override fun run(params: String): Flow<Result<Balance>> = flow {
        emit(Result.Loading)
        val result = successOrError {
            repository.getBalance(params)
        }
        emit(result)
    }

}