package com.example.currencyexchange.network

import com.example.currencyexchange.BuildConfig
import com.example.currencyexchange.network.data.AvailableCurrencyData
import com.example.currencyexchange.network.data.LiveCurrencyData
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("list")
    suspend fun getAllAvailableCurrencies(
        @Query("access_key") apikey: String = BuildConfig.API_KEY
    ): AvailableCurrencyData

    @GET("live")
    suspend fun getLiveRate(
        @Query("access_key") apikey: String = BuildConfig.API_KEY,
    ): LiveCurrencyData
}