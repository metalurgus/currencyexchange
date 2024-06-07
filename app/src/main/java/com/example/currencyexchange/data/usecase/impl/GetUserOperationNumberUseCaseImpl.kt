package com.example.currencyexchange.data.usecase.impl

import com.example.currencyexchange.data.Result
import com.example.currencyexchange.data.repository.UserDataRepository
import com.example.currencyexchange.data.successOrError
import com.example.currencyexchange.data.usecase.GetUserOperationNumberUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetUserOperationNumberUseCaseImpl(private val repository: UserDataRepository) :
    GetUserOperationNumberUseCase() {
    override fun run(params: None): Flow<Result<Int>> = flow {
        emit(Result.Loading)
        val result = successOrError {
            repository.getOperationNumber()
        }
        emit(result)
    }

}