package com.example.currencyexchange.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.currencyexchange.local.entity.CurrencyExchangeEntity

@Dao
interface CurrencyRateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExchange(list: List<CurrencyExchangeEntity>)

    @Query("SELECT * FROM `CurrencyExchangeEntity`")
    suspend fun getAllExchange(): List<CurrencyExchangeEntity>?

    @Query("SELECT * FROM `CurrencyExchangeEntity` WHERE `country`= :countryCode")
    suspend fun getSelectedExchange(countryCode: String): CurrencyExchangeEntity?

    @Query("SELECT `country` FROM  `CurrencyExchangeEntity`")
    suspend fun getAllCountries(): List<String>?

    @Query("SELECT `rate` FROM `CurrencyExchangeEntity` WHERE `country`= :countryCode")
    suspend fun getExchangeRateBasedOnCountry(countryCode: String): Double?

}
