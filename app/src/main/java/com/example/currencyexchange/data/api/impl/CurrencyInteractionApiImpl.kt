package com.example.currencyexchange.data.api.impl

import com.example.currencyexchange.data.api.CurrencyInteractionApi
import com.example.currencyexchange.data.model.Balance
import com.example.currencyexchange.data.model.response.ExchangeOperationResponse
import com.example.currencyexchange.data.provider.ExchangeRatesProvider
import com.example.currencyexchange.data.provider.SharedPrefsProvider
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class CurrencyInteractionApiImpl(
    private val sharedPrefsProvider: SharedPrefsProvider,
    private val exchangeRatesProvider: ExchangeRatesProvider
) :
    CurrencyInteractionApi {

    companion object {
        private val EMPTY_BODY = ByteArray(0).toResponseBody(null)
        private val EMPTY_REQUEST = okhttp3.Request.Builder().url("http://localhost").build()
    }

    override suspend fun exchange(
        fromCurrency: String,
        toCurrency: String,
        amount: Double
    ): Response<ExchangeOperationResponse> {
        val fromBalance = sharedPrefsProvider.getBalance(fromCurrency)
        var toBalance = sharedPrefsProvider.getBalance(toCurrency)

        fromBalance ?: return Response.error(
            EMPTY_BODY,
            okhttp3.Response.Builder()
                .request(EMPTY_REQUEST)
                .protocol(okhttp3.Protocol.HTTP_1_1)
                .code(400)
                .message("Source balance not found")
                .build()
        )
        if (amount > fromBalance.amount) {
            return Response.error(
                EMPTY_BODY,
                okhttp3.Response.Builder()
                    .request(EMPTY_REQUEST)
                    .protocol(okhttp3.Protocol.HTTP_1_1)
                    .code(400)
                    .message("Not enough money")
                    .build()
            )
        }
        if (toBalance == null) {
            toBalance = Balance(toCurrency, 0.0)
        }
        val commissionFee = calculateCommissionFee(fromCurrency, toCurrency, amount)

        val exchangingAmount = amount - commissionFee

        if (exchangingAmount < 0) {
            return Response.error(
                EMPTY_BODY,
                okhttp3.Response.Builder()
                    .request(EMPTY_REQUEST)
                    .protocol(okhttp3.Protocol.HTTP_1_1)
                    .code(400)
                    .message("Commission fee is higher then exchanging amount")
                    .build()
            )
        }

        val exchangeRate = exchangeRatesProvider.getExchangeRate(fromCurrency, toCurrency)
            ?: return Response.error(
                EMPTY_BODY,
                okhttp3.Response.Builder()
                    .request(EMPTY_REQUEST)
                    .protocol(okhttp3.Protocol.HTTP_1_1)
                    .code(400)
                    .message("Exchange between $fromCurrency and $toCurrency is not currently available")
                    .build()
            )

        val exchangedAmount = exchangingAmount * exchangeRate

        val newFromBalance = fromBalance.copy(amount = fromBalance.amount - amount)
        val newToBalance = toBalance.copy(amount = toBalance.amount + exchangedAmount)

        sharedPrefsProvider.saveBalance(newFromBalance)
        sharedPrefsProvider.saveBalance(newToBalance)

        return Response.success(
            ExchangeOperationResponse(
                newFromBalance,
                newToBalance,
                "Commission fee: $commissionFee $fromCurrency"
            )
        )
    }

    override suspend fun getBalances(): Response<List<Balance>> {
        return Response.success(sharedPrefsProvider.getAllBalances())
    }

    private fun calculateCommissionFee(
        fromCurrency: String,
        toCurrency: String,
        amount: Double
    ): Double {
        return 0.0
    }

}