package com.example.currencyexchange.data.api.impl

import com.example.currencyexchange.data.api.CurrencyInteractionApi
import com.example.currencyexchange.data.model.Balance
import com.example.currencyexchange.data.model.CommissionRule
import com.example.currencyexchange.data.model.TransactionRecord
import com.example.currencyexchange.data.model.response.ExchangeOperationResponse
import com.example.currencyexchange.data.model.response.GetCommissionFeeForTransactionResponse
import com.example.currencyexchange.data.provider.CommissionRulesProvider
import com.example.currencyexchange.data.provider.ExchangeRatesProvider
import com.example.currencyexchange.data.provider.SharedPrefsProvider
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.util.Date

class CurrencyInteractionApiImpl(
    private val sharedPrefsProvider: SharedPrefsProvider,
    private val exchangeRatesProvider: ExchangeRatesProvider,
    private val commissionRulesProvider: CommissionRulesProvider
) :
    CurrencyInteractionApi {

    companion object {
        private val EMPTY_BODY = ByteArray(0).toResponseBody(null)
        private val EMPTY_REQUEST = okhttp3.Request.Builder().url("http://localhost").build()
    }

    override suspend fun exchange(
        fromCurrency: String,
        toCurrency: String,
        amount: Double,
        isPreview: Boolean
    ): Response<ExchangeOperationResponse> {
        val fromBalance = sharedPrefsProvider.getBalance(fromCurrency)
        var toBalance = sharedPrefsProvider.getBalance(toCurrency)

        fromBalance ?: return Response.error<ExchangeOperationResponse?>(
            EMPTY_BODY,
            okhttp3.Response.Builder()
                .request(EMPTY_REQUEST)
                .protocol(okhttp3.Protocol.HTTP_1_1)
                .code(400)
                .message("Source balance not found")
                .build()
        ).run {
            if (!isPreview) {
                sharedPrefsProvider.saveTransactionRecord(
                    TransactionRecord(
                        fromCurrency,
                        toCurrency,
                        amount,
                        0.0,
                        sharedPrefsProvider.getTransactionNumber(),
                        0.0,
                        Date().time,
                        "Failed Exchange from $fromCurrency to $toCurrency. Source balance not found",
                        false
                    )
                )
            }
            this
        }
        if (!isPreview) {
            if (amount > fromBalance.amount) {
                return Response.error<ExchangeOperationResponse?>(
                    EMPTY_BODY,
                    okhttp3.Response.Builder()
                        .request(EMPTY_REQUEST)
                        .protocol(okhttp3.Protocol.HTTP_1_1)
                        .code(400)
                        .message("Not enough money")
                        .build()
                ).run {
                    sharedPrefsProvider.saveTransactionRecord(
                        TransactionRecord(
                            fromCurrency,
                            toCurrency,
                            amount,
                            0.0,
                            sharedPrefsProvider.getTransactionNumber(),
                            0.0,
                            Date().time,
                            "Failed Exchange from $fromCurrency to $toCurrency. Not enough money",
                            false
                        )
                    )

                    this
                }
            }
        }
        val commissionResponse = getCommissionFeeForTransaction(
            fromCurrency,
            toCurrency,
            amount,
            sharedPrefsProvider.getTransactionNumber()
        )
        if (!commissionResponse.isSuccessful || commissionResponse.body() == null) {
            return Response.error<ExchangeOperationResponse?>(
                EMPTY_BODY,
                okhttp3.Response.Builder()
                    .request(EMPTY_REQUEST)
                    .protocol(okhttp3.Protocol.HTTP_1_1)
                    .code(400)
                    .message("Exchange between $fromCurrency and $toCurrency is not currently available")
                    .build()
            ).run {
                if (!isPreview) {
                    sharedPrefsProvider.saveTransactionRecord(
                        TransactionRecord(
                            fromCurrency,
                            toCurrency,
                            amount,
                            0.0,
                            sharedPrefsProvider.getTransactionNumber(),
                            0.0,
                            Date().time,
                            "Failed Exchange from $fromCurrency to $toCurrency. Commission fee not available",
                            false
                        )
                    )
                }
                this
            }
        }
        val commissionRule = commissionResponse.body()!!.ruleApplied
        if (toBalance == null) {
            toBalance = Balance(toCurrency, 0.0)
        }


        val commissionFee = commissionRule.getFee(amount)

        val exchangingAmount = amount - commissionFee

        if (exchangingAmount < 0) {
            return Response.error<ExchangeOperationResponse?>(
                EMPTY_BODY,
                okhttp3.Response.Builder()
                    .request(EMPTY_REQUEST)
                    .protocol(okhttp3.Protocol.HTTP_1_1)
                    .code(400)
                    .message("Commission fee is higher then exchanging amount")
                    .build()
            ).run {
                if (!isPreview) {
                    sharedPrefsProvider.saveTransactionRecord(
                        TransactionRecord(
                            fromCurrency,
                            toCurrency,
                            amount,
                            0.0,
                            sharedPrefsProvider.getTransactionNumber(),
                            0.0,
                            Date().time,
                            "Failed Exchange from $fromCurrency to $toCurrency. Commission fee is higher then exchanging amount",
                            false
                        )
                    )
                }
                this
            }
        }

        val exchangeRate = exchangeRatesProvider.getExchangeRate(fromCurrency, toCurrency)
            ?: return Response.error<ExchangeOperationResponse?>(
                EMPTY_BODY,
                okhttp3.Response.Builder()
                    .request(EMPTY_REQUEST)
                    .protocol(okhttp3.Protocol.HTTP_1_1)
                    .code(400)
                    .message("Exchange between $fromCurrency and $toCurrency is not currently available")
                    .build()
            ).run {
                if (!isPreview) {
                    sharedPrefsProvider.saveTransactionRecord(
                        TransactionRecord(
                            fromCurrency,
                            toCurrency,
                            amount,
                            0.0,
                            sharedPrefsProvider.getTransactionNumber(),
                            0.0,
                            Date().time,
                            "Failed Exchange from $fromCurrency to $toCurrency. Exchange rate not available",
                            false
                        )
                    )
                }
                this
            }

        val exchangedAmount = exchangingAmount * exchangeRate
        val newFromBalance = fromBalance.copy(amount = fromBalance.amount - amount)
        val newToBalance = toBalance.copy(amount = toBalance.amount + exchangedAmount)
        if (!isPreview) {


            sharedPrefsProvider.saveBalance(newFromBalance)
            sharedPrefsProvider.saveBalance(newToBalance)
            sharedPrefsProvider.saveTransactionRecord(
                TransactionRecord(
                    fromCurrency,
                    toCurrency,
                    amount,
                    exchangeRate,
                    sharedPrefsProvider.getTransactionNumber(),
                    commissionFee,
                    Date().time,
                    "Successful Exchange from $fromCurrency to $toCurrency. Commission fee: $commissionFee $fromCurrency",
                    true
                )
            )
            sharedPrefsProvider.increaseTransactionNumber()
        }


        return Response.success(
            ExchangeOperationResponse(
                newFromBalance,
                newToBalance,
                exchangedAmount,
                commissionFee,
                exchangeRate,
                "Commission fee: $commissionFee $fromCurrency"
            )
        )
    }

    override suspend fun previewExchange(
        fromCurrency: String,
        toCurrency: String,
        amount: Double
    ): Response<ExchangeOperationResponse> {
        return exchange(fromCurrency, toCurrency, amount, true)
    }

    override suspend fun getBalances(): Response<List<Balance>> {
        return Response.success(sharedPrefsProvider.getAllBalances())
    }

    override suspend fun getBalance(currency: String): Response<Balance> {
        return sharedPrefsProvider.getBalance(currency)?.let { Response.success(it) }
            ?: Response.error(
                EMPTY_BODY,
                okhttp3.Response.Builder()
                    .request(EMPTY_REQUEST)
                    .protocol(okhttp3.Protocol.HTTP_1_1)
                    .code(400)
                    .message("Balance for currency $currency not found")
                    .build()
            )
    }

    override suspend fun getCommissionFeeForTransaction(
        fromCurrency: String,
        toCurrency: String,
        amount: Double,
        transactionNumber: Int
    ): Response<GetCommissionFeeForTransactionResponse> {
        val rule = commissionRulesProvider.getCommissionRules(fromCurrency, toCurrency).filter {
            it.amountRange.isInRange(amount)
                    && it.dateRange.isInRange(Date())
                    && it.transactionNumberRange.isInRange(transactionNumber)
        }.maxByOrNull { it.priority } ?: CommissionRule.DEFAULT_RULE
        return Response.success(
            GetCommissionFeeForTransactionResponse(
                rule.getFee(amount),
                rule
            )
        )
    }

    override suspend fun getTransactionHistory(): Response<List<TransactionRecord>> {
        return Response.success(sharedPrefsProvider.getTransactionHistory())
    }

    override fun getOperationNumber(): Response<Int> {
        return Response.success(sharedPrefsProvider.getTransactionNumber())
    }
}