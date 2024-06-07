package com.example.currencyexchange.data.usecase

import com.example.currencyexchange.data.Result
import kotlinx.coroutines.flow.Flow


abstract class UseCase<out Type, in Params> where Type : Any {

    abstract fun run(params: Params): Flow<Result<Type>>

    object None
}