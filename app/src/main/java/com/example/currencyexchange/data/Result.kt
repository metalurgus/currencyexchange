package com.example.currencyexchange.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}

suspend fun <T> successOrError(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    call: suspend () -> T
): Result<T> = withContext(dispatcher) {
    try {
        val result = call.invoke()
        Result.Success(result)
    } catch (error: Throwable) {
        Result.Error(Exception(error))
    }
}