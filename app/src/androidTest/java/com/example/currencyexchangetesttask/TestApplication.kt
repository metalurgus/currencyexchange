package com.example.currencyexchangetesttask

import android.app.Application
import com.example.currencyexchange.data.network.networkModule
import com.example.currencyexchange.data.provider.providerModule
import com.example.currencyexchange.data.usecase.useCaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TestApplication)
            androidLogger()
            modules(
                providerModule,
                networkModule,
                useCaseModule,
            )
        }
    }
}