package com.example.currencyexchange.repository

import android.content.SharedPreferences
import com.example.currencyexchange.ext.isCachedTimeout
import com.example.currencyexchange.local.LAST_CACHE_TIME
import com.example.currencyexchange.local.dao.CurrencyRateDao
import com.example.currencyexchange.mapper.CurrencyMapper
import com.example.currencyexchange.mapper.LocalCacheMapper
import com.example.currencyexchange.network.Api
import com.example.currencyexchange.uiState.UiState
import com.example.currencyexchange.uiState.UiState.Countries
import com.example.currencyexchange.uiState.UiState.Currencies
import com.example.currencyexchange.uiState.UiState.ExchangeRate
import com.example.currencyexchange.uiState.UiState.FailedUiState
import kotlinx.coroutines.async
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext

interface CurrencyExchangeRepository {

    suspend fun getAllCurrencyList(): UiState

    suspend fun getAvailableCurrency(): UiState

    suspend fun formatCurrency(source: String, destination: String, amount: Double): UiState
}

class CurrencyExchangeRepositoryImpl(
    private val mapper: CurrencyMapper,
    private val api: Api,
    private val cacheMapper: LocalCacheMapper,
    private val preferences: SharedPreferences,
    private val currencyRateDao: CurrencyRateDao
) : CurrencyExchangeRepository {

    override suspend fun getAllCurrencyList(): UiState {
        return runCatching {
            val timeStamp = fetchLiveData()
            val list = currencyRateDao.getAllExchange()
            list?.let {
                Currencies(
                    currencies = mapper.invoke(it),
                    timeStamp = timeStamp.first!!
                )
            } ?: run {
                FailedUiState(timeStamp.second ?: Throwable(message = "Database fetch "))
            }
        }.getOrElse {
            FailedUiState(it)
        }
    }

    private suspend fun fetchLiveData(): Pair<Long?, Throwable?> {
        val lastSaved = preferences.getLong(LAST_CACHE_TIME, 0L)
        return if (lastSaved.isCachedTimeout()) {
            val live = api.getLiveRate()
            if (live.success) {
                val list = cacheMapper.invoke(
                    live.source,
                    live.currencies
                )
                preferences.edit().putLong(LAST_CACHE_TIME, System.currentTimeMillis())
                    .apply()
                currencyRateDao.insertExchange(list)
                Pair(live.timestamp, null)
            } else {
                val error = live.error
                return Pair(null, Throwable(message = error!!.info))
            }
        } else {
            Pair(lastSaved, null)
        }
    }

    override suspend fun getAvailableCurrency() =
        Countries(countries = currencyRateDao.getAllCountries() ?: emptyList())

    override suspend fun formatCurrency(
        source: String,
        destination: String,
        amount: Double
    ): UiState {
        return withContext(currentCoroutineContext()) {
            val sourceRateAsync = async { currencyRateDao.getExchangeRateBasedOnCountry(source) }
            val destinationRateAsync =
                async { currencyRateDao.getExchangeRateBasedOnCountry(destination) }

            val sourceRate = sourceRateAsync.await()
            val destinationRate = destinationRateAsync.await()
            if (sourceRate != null && destinationRate != null) {
                val converted = (destinationRate / sourceRate) * amount
                ExchangeRate(converted)
            } else {
                FailedUiState(Throwable(message = "Currency is not present "))
            }
        }
    }
}
