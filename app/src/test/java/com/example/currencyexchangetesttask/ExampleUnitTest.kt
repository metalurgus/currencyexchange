package com.example.currencyexchangetesttask

import com.example.currencyexchange.data.network.networkModule
import com.example.currencyexchange.data.provider.providerModule
import com.example.currencyexchange.data.usecase.useCaseModule
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import androidx.test.platform.app.InstrumentationRegistry
import com.example.currencyexchange.data.api.CurrencyInteractionApi
import kotlinx.coroutines.test.runTest
import org.koin.java.KoinJavaComponent
import org.koin.test.KoinTest
import org.koin.test.inject


class ExampleUnitTest : KoinTest {
    val currencyInteractionApi: CurrencyInteractionApi by inject()

    @Before
    fun setUp() {
        startKoin {
            androidContext(InstrumentationRegistry.getInstrumentation().targetContext)
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
    fun initialBalanceCreation() = runTest {
        val balances = currencyInteractionApi.getBalances()
        assertTrue(balances.isSuccessful)
        assertNotNull(balances.body())
        assertEquals(1, balances.body()?.size)
    }
}