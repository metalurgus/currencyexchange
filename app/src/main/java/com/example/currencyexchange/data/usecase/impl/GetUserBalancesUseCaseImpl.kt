package com.example.currencyexchange.data.usecase.impl

import com.example.currencyexchange.data.Result
import com.example.currencyexchange.data.model.Balance
import com.example.currencyexchange.data.repository.UserDataRepository
import com.example.currencyexchange.data.successOrError
import com.example.currencyexchange.data.usecase.GetUserBalancesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetUserBalancesUseCaseImpl(private val repository: UserDataRepository) :
    GetUserBalancesUseCase() {
    override fun run(params: None): Flow<Result<List<Balance>>> = flow {
        emit(Result.Loading)
        val result = successOrError {
            repository.getBalances()
        }
        emit(result)
    }

}