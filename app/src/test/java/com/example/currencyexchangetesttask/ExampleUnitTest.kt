@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.currencyexchangetesttask

import com.example.currencyexchange.data.api.CurrencyInteractionApi
import com.example.currencyexchange.data.network.networkModule
import com.example.currencyexchange.data.provider.ExchangeRatesProvider
import com.example.currencyexchange.data.usecase.GetExchangeRatesUseCase
import com.example.currencyexchange.data.usecase.UseCase
import com.example.currencyexchange.data.usecase.useCaseModule
import com.example.currencyexchangetesttask.data.testProviderModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject


class ExampleUnitTest : KoinTest {
    val currencyInteractionApi: CurrencyInteractionApi by inject()
    val getExchangeRatesUseCase: GetExchangeRatesUseCase by inject()
    val exchangeRatesProvider: ExchangeRatesProvider by inject()

    @Before
    fun setUp() {
        startKoin {
            modules(
                testProviderModule,
                networkModule,
                useCaseModule,
            )
        }
    }

    @After
    fun tearDown() {
        GlobalContext.stopKoin()
    }

    @Test
    fun initialBalanceCreation() = runTest {
        val balances = currencyInteractionApi.getBalances()
        assertTrue(balances.isSuccessful)
        assertNotNull(balances.body())
        assertEquals(1, balances.body()?.size)
    }

    @Test
    fun exchange500EurToUsd() = runTest {
        val ratesResponse = getExchangeRatesUseCase.run(UseCase.None)
        val toUsdRate = ratesResponse.rates.find { it.currency == "USD" }
        assertNotNull(toUsdRate)
        exchangeRatesProvider.setExchangeRates(ratesResponse)
        val exchangeResponse = currencyInteractionApi.exchange("EUR", "USD", 500.0)
        assertTrue(exchangeResponse.isSuccessful)
        val balancesResponse = currencyInteractionApi.getBalances()
        assertTrue(balancesResponse.isSuccessful)

        val balances = balancesResponse.body()
        assertNotNull(balances)
        assertEquals(2, balances?.size)

        val usdBalance = balances?.find { it.currency == "USD" }
        assertNotNull(usdBalance)

        val exchangeResp = exchangeResponse.body()
        assertNotNull(exchangeResp)
        assertEquals(500 * exchangeResp!!.rate - exchangeResp.commissionFee, usdBalance?.amount)

        val eurBalance = balances?.find { it.currency == "EUR" }
        assertNotNull(eurBalance)
        assertEquals(500.0, eurBalance?.amount)
        val transactionHistoryResponse = currencyInteractionApi.getTransactionHistory()
        assertTrue(transactionHistoryResponse.isSuccessful)
        val transactionHistory = transactionHistoryResponse.body()
        assertNotNull(transactionHistory)
        assertEquals(1, transactionHistory?.size)

    }
}