package com.example.currencyexchange.module

import com.example.currencyexchange.CurrencyExchangeApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

fun CurrencyExchangeApplication.initKoin(){
    startKoin {
        androidContext(applicationContext)
        modules(
            listOf(
                netModule,
                repoModule,
                provideMapper,
                viewModelModule,
                cacheModule
            )
        )
    }
}
