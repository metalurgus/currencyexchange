package com.example.currencyexchange

import android.app.Application
import com.example.currencyexchange.data.network.networkModule
import com.example.currencyexchange.data.provider.providerModule
import com.example.currencyexchange.data.repository.repositoryModule
import com.example.currencyexchange.data.usecase.useCaseModule
import com.example.currencyexchange.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            androidLogger()
            modules(
                providerModule,
                networkModule,
                repositoryModule,
                useCaseModule,
                appModule
            )
        }
    }

    val appModule = module {
        viewModel { MainViewModel(get(), get(), get(), get()) }
        single<(Int) -> String> { { id -> get<Application>().getString(id) } }
    }


}