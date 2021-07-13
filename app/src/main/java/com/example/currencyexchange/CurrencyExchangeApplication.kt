package com.example.currencyexchange

import androidx.multidex.MultiDexApplication
import com.example.currencyexchange.module.initKoin

class CurrencyExchangeApplication: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}