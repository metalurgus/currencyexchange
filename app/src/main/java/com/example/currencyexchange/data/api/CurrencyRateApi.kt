package com.example.currencyexchange.data.api

import com.example.currencyexchange.data.model.response.ExchangeRatesResponse
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyRateApi {
    @GET("currency-exchange-rates")
    suspend fun getExchangeRates(): Response<ExchangeRatesResponse>
}