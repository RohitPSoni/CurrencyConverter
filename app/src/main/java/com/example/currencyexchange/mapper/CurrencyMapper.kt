package com.example.currencyexchange.mapper

import com.example.currencyexchange.local.entity.CurrencyExchangeEntity
import com.example.currencyexchange.ui.model.Country

interface CurrencyMapper: Function1<List<CurrencyExchangeEntity>, List<Country>>

class CurrencyMapperImpl : CurrencyMapper {
    override fun invoke(p1: List<CurrencyExchangeEntity>): List<Country> {
        val list = mutableListOf<Country>()
        p1.forEach {
            list.add(
                Country(
                    initial = it.country,
                    exchange = it.rate
                )
            )
        }
        return list
    }
}
