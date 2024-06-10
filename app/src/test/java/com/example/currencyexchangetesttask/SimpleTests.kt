@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.currencyexchangetesttask

import com.example.currencyexchange.data.Result
import com.example.currencyexchange.data.model.Balance
import com.example.currencyexchange.data.model.response.ExchangeRatesResponse
import com.example.currencyexchange.data.network.networkModule
import com.example.currencyexchange.data.provider.ExchangeRatesProvider
import com.example.currencyexchange.data.repository.repositoryModule
import com.example.currencyexchange.data.usecase.ExchangeCurrencyUseCase
import com.example.currencyexchange.data.usecase.GetExchangeRatesUseCase
import com.example.currencyexchange.data.usecase.GetUserBalanceUseCase
import com.example.currencyexchange.data.usecase.GetUserBalancesUseCase
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


class SimpleTests : KoinTest {
    val getExchangeRatesUseCase: GetExchangeRatesUseCase by inject()
    val getUserBalancesUseCase: GetUserBalancesUseCase by inject()
    val getUserBalanceUseCase: GetUserBalanceUseCase by inject()
    val exchangeCurrencyUseCase: ExchangeCurrencyUseCase by inject()
    val exchangeRatesProvider: ExchangeRatesProvider by inject()

    @Before
    fun setUp() {
        startKoin {
            modules(
                testProviderModule,
                networkModule,
                repositoryModule,
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
        val balancesFlow = getUserBalancesUseCase.run(UseCase.None)
        val emittedResults = mutableListOf<Result<List<Balance>>>()
        balancesFlow.collect {
            emittedResults.add(it)
        }
        assertEquals(2, emittedResults.size)
        assertTrue(emittedResults[0] is Result.Loading)
        assertTrue(emittedResults[1] is Result.Success)
        val balances = (emittedResults[1] as Result.Success).data
        assertEquals(1, balances.size)
    }

    @Test
    fun getBalanceFail() = runTest {
        val balanceFlow = getUserBalanceUseCase.run("INCORRECT")
        val emittedResults = mutableListOf<Result<Balance>>()
        balanceFlow.collect {
            emittedResults.add(it)
        }
        assertEquals(2, emittedResults.size)
        assertTrue(emittedResults[0] is Result.Loading)
        assertTrue(emittedResults[1] is Result.Error)
        assertEquals("Balance for currency INCORRECT not found", (emittedResults[1] as Result.Error).exception.cause?.message)

    }

    @Test
    fun exchange500EURtoUSD() = runTest {
        val ratesFlow = getExchangeRatesUseCase.run(UseCase.None)
        val emittedResults = mutableListOf<Result<ExchangeRatesResponse>>()
        ratesFlow.collect {
            emittedResults.add(it)
        }
        assertEquals(2, emittedResults.size)
        assertTrue(emittedResults[0] is Result.Loading)
        assertTrue(emittedResults[1] is Result.Success)
        val ratesResult = (emittedResults[1] as Result.Success).data
        val toUsdRate = ratesResult.rates.find { it.currency == "USD" }
        assertNotNull(toUsdRate)
        exchangeRatesProvider.setExchangeRates(ratesResult)
        val exchangeFlow =
            exchangeCurrencyUseCase.run(ExchangeCurrencyUseCase.Params("EUR", "USD", 500.0))
        val exchangeEmittedResults = mutableListOf<Result<String>>()
        exchangeFlow.collect {
            exchangeEmittedResults.add(it)
        }
        assertEquals(2, exchangeEmittedResults.size)
        assertTrue(exchangeEmittedResults[0] is Result.Loading)
        assertTrue(exchangeEmittedResults[1] is Result.Success)
        val exchangeResult = (exchangeEmittedResults[1] as Result.Success).data
        assertEquals("Commission fee: 0.0 EUR", exchangeResult)

        val balancesFlow = getUserBalancesUseCase.run(UseCase.None)
        val balancesEmittedResults = mutableListOf<Result<List<Balance>>>()
        balancesFlow.collect {
            balancesEmittedResults.add(it)
        }
        assertEquals(2, balancesEmittedResults.size)
        assertTrue(balancesEmittedResults[0] is Result.Loading)
        assertTrue(balancesEmittedResults[1] is Result.Success)
        val balances = (balancesEmittedResults[1] as Result.Success).data
        assertEquals(2, balances.size)
        val eurBalance = balances.find { it.currency == "EUR" }
        val usdBalance = balances.find { it.currency == "USD" }
        assertNotNull(eurBalance)
        assertNotNull(usdBalance)
        assertEquals(500.0, eurBalance!!.amount, 0.0)
        assertEquals(500.0 * toUsdRate!!.rate, usdBalance!!.amount, 0.0)
    }
}