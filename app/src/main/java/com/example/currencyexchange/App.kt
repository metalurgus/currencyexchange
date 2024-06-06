package com.example.currencyexchange

import android.app.Application
import com.example.currencyexchange.data.network.networkModule
import com.example.currencyexchange.data.provider.providerModule
import com.example.currencyexchange.data.usecase.useCaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            androidLogger()
            modules(
                providerModule,
                networkModule,
                useCaseModule,
            )
        }
    }
}