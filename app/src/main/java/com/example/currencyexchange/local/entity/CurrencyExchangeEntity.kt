package com.example.currencyexchange.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CurrencyExchangeEntity(
    @PrimaryKey val country: String,
    val rate: Double)