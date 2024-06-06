package com.example.currencyexchange.data.util

import retrofit2.Response

val <T> Response<T>.bodyOrThrow: T
    get() {
        if (this.isSuccessful) {
            return this.body()
                ?: throw Exception(message = "The body is null")
        } else {
            throw Exception(message = this.message())
        }
    }