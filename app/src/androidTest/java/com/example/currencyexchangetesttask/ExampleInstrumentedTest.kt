package com.example.currencyexchangetesttask

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.currencyexchange.data.network.networkModule
import com.example.currencyexchange.data.provider.providerModule
import com.example.currencyexchange.data.usecase.useCaseModule
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import com.example.currencyexchange.data.api.CurrencyInteractionApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest : KoinTest {
    val currencyInteractionApi: CurrencyInteractionApi by inject()

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        startKoin {
            androidContext(appContext)
            androidLogger()
            modules(
                providerModule,
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
    fun initialBalanceCreation() = runBlocking {
        val balances = currencyInteractionApi.getBalances()
        assertTrue(balances.isSuccessful)
        assertNotNull(balances.body())
        assertEquals(1, balances.body()?.size)
    }

    @Test
    fun exchange500EurToUsd() = runBlocking {
        currencyInteractionApi.exchange("EUR", "USD", 500.0)
        val balancesResponse = currencyInteractionApi.getBalances()
        assertTrue(balancesResponse.isSuccessful)

        val balances = balancesResponse.body()
        assertNotNull(balances)
        assertEquals(2, balances?.size)
    }
}