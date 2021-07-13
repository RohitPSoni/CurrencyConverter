package com.example.currencyexchange.mapper

import com.example.currencyexchange.network.data.LiveCurrencyData
import com.example.currencyexchange.parseResource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LocalCacheMapperImplTest{

    private lateinit var mapper: LocalCacheMapperImpl
    private val liveData: LiveCurrencyData = parseResource("livedata.json")

    @BeforeEach
    fun setUp(){
        mapper = LocalCacheMapperImpl()
    }

    @Test
    fun `form Entity`(){
        val list = mapper.invoke(source = liveData.source, exchangeRate = liveData.currencies)
        assertEquals(list.size, liveData.currencies.entries.size)
    }
}