package com.example.currencyexchange.repository

import android.content.SharedPreferences
import com.example.currencyexchange.formCurrencyExchangeEntity
import com.example.currencyexchange.local.dao.CurrencyRateDao
import com.example.currencyexchange.mapper.CurrencyMapper
import com.example.currencyexchange.mapper.LocalCacheMapper
import com.example.currencyexchange.mock
import com.example.currencyexchange.network.Api
import com.example.currencyexchange.network.data.LiveCurrencyData
import com.example.currencyexchange.parseResource
import com.example.currencyexchange.uiState.UiState.Countries
import com.example.currencyexchange.uiState.UiState.Currencies
import com.example.currencyexchange.uiState.UiState.ExchangeRate
import com.example.currencyexchange.uiState.UiState.FailedUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class CurrencyExchangeRepositoryTest {

    private val liveData: LiveCurrencyData = parseResource("livedata.json")
    private val liveDataFalse: LiveCurrencyData = parseResource("livedatafalse.json")

    private lateinit var repo: CurrencyExchangeRepositoryImpl
    private val mapper: CurrencyMapper = mock()
    private val api: Api = mock()
    private val cacheMapper: LocalCacheMapper = mock()
    private val preferences: SharedPreferences = mock()
    private val currencyRateDao: CurrencyRateDao = mock()
    private val preferencesEditor: SharedPreferences.Editor = mock()

    @BeforeEach
    fun setUp() {
        repo = CurrencyExchangeRepositoryImpl(
            mapper = mapper,
            api = api,
            cacheMapper = cacheMapper,
            preferences = preferences,
            currencyRateDao = currencyRateDao
        )
        init()
    }

    private fun init() = runBlocking {
        Mockito.`when`(api.getLiveRate()).thenReturn(liveData)
        Mockito.`when`(preferences.edit()).thenReturn(preferencesEditor)
        Mockito.`when`(preferences.edit().putLong(anyString(), anyLong()))
            .thenReturn(preferencesEditor)
    }

    @Test
    fun `when api is successfull`() = runBlocking {
        Mockito.`when`(currencyRateDao.getAllExchange())
            .thenReturn(emptyList())
        Mockito.`when`(mapper.invoke(formCurrencyExchangeEntity(liveData))).thenReturn(emptyList())
        val uiState = repo.getAllCurrencyList()
        assertTrue(uiState is Currencies)
    }

    @Test
    fun `when api gives exception`() = runBlocking {
        Mockito.`when`(api.getLiveRate()).thenAnswer { throw Exception("500") }
        val uiState = repo.getAllCurrencyList()
        assertTrue(uiState is FailedUiState)
    }

    @Test
    fun `when success is false`() = runBlocking {
        Mockito.`when`(api.getLiveRate()).thenReturn(liveDataFalse)
        val uiState = repo.getAllCurrencyList()
        assertTrue(uiState is FailedUiState)
    }

    @Test
    fun `get country list`(): Unit = runBlocking {
        repo.getAvailableCurrency()
        Mockito.verify(currencyRateDao).getAllCountries()
    }

    @Test
    fun `format currency`() = runBlocking{
        Mockito.`when`(currencyRateDao.getExchangeRateBasedOnCountry("USD")).thenReturn(1.0)
        Mockito.`when`(currencyRateDao.getExchangeRateBasedOnCountry("INR")).thenReturn(74.0)
        val uiState = repo.formatCurrency("USD", "INR", 10.0)
        assertTrue(uiState is ExchangeRate)
        assertEquals((uiState as ExchangeRate).rate , 740.0)

        val uiStateReverse = repo.formatCurrency("INR", "USD", 10.0)
        assertTrue(uiStateReverse is ExchangeRate)
        assertEquals((uiStateReverse as ExchangeRate).rate , 0.13513513513513514)
    }

    @Test
    fun `format currency when fetch null`() = runBlocking {
        Mockito.`when`(currencyRateDao.getExchangeRateBasedOnCountry("USD")).thenReturn(null)
        val uiState = repo.formatCurrency("USD", "INR", 10.0)
        assertTrue(uiState is FailedUiState)
    }
}