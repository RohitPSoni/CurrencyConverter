package com.example.currencyexchange.mapper

import com.example.currencyexchange.formCurrencyExchangeEntity
import com.example.currencyexchange.network.data.LiveCurrencyData
import com.example.currencyexchange.parseResource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CurrencyMapperImplTest {

    private val liveData: LiveCurrencyData = parseResource("livedata.json")
    private val entity = formCurrencyExchangeEntity(liveData)
    private lateinit var mapper: CurrencyMapperImpl

    @BeforeEach
    fun setUp() {
        mapper = CurrencyMapperImpl()
    }

    @Test
    fun `form list country`() {
        val list = mapper.invoke(entity)
        assertEquals(entity.size, list.size)
    }
}