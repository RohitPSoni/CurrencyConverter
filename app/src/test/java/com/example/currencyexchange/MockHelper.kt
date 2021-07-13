package com.example.currencyexchange

import com.example.currencyexchange.local.entity.CurrencyExchangeEntity
import com.example.currencyexchange.network.data.LiveCurrencyData
import com.example.currencyexchange.ui.model.Country
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.mockito.Mockito
import java.io.InputStreamReader

/**
 * Reads JSON object and converts it to object
 */
inline fun <reified T> parseResource(path: String, gson: Gson = Gson()): T {
    val obj = object : TypeToken<T>() {}.type

    return gson.fromJson(
        InputStreamReader(T::class.java.classLoader!!.getResourceAsStream(path)),
        obj
    )
}

/**
 * Create mocks of a type casted Generic Java object
 */
inline fun <reified T : Any> mock(): T = Mockito.mock(T::class.java)

/**
 * Create a Entity from response
 */

fun formCurrencyExchangeEntity(liveCurrencyData: LiveCurrencyData):
    List<CurrencyExchangeEntity> {
    val list = mutableListOf<CurrencyExchangeEntity>()
    liveCurrencyData.currencies.forEach {
        CurrencyExchangeEntity(
            country = it.key,
            rate = it.value
        )
    }
    return list
}