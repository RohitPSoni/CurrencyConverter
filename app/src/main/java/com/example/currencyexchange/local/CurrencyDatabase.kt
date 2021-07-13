package com.example.currencyexchange.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.currencyexchange.local.dao.CurrencyRateDao
import com.example.currencyexchange.local.entity.CurrencyExchangeEntity

@Database(
    entities = [CurrencyExchangeEntity::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class CurrencyDatabase : RoomDatabase() {

    abstract fun getCurrentExchangeDao(): CurrencyRateDao
}