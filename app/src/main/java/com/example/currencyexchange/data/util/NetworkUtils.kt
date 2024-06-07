package com.example.currencyexchange.data.util

import retrofit2.Response

val <T> Response<T>.bodyOrThrow: T
    get() {
        if (this.isSuccessful) {
            return this.body()
                ?: throw Exception("The body is null")
        } else {
            throw Exception(this.message())
        }
    }