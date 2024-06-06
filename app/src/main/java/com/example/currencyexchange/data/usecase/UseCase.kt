package com.example.currencyexchange.data.usecase

abstract class UseCase<out Type, in Params> where Type : Any {

    abstract suspend fun run(params: Params): Type

    object None
}