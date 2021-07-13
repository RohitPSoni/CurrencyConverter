package com.example.currencyexchange.mapper

import com.example.currencyexchange.local.entity.CurrencyExchangeEntity

interface LocalCacheMapper :
    Function2<String, Map<String, Double>, List<CurrencyExchangeEntity>>

class LocalCacheMapperImpl : LocalCacheMapper {

    override fun invoke(
        source: String,
        exchangeRate: Map<String, Double>
    ):
        List<CurrencyExchangeEntity> {
        val currencyRate = mutableListOf<CurrencyExchangeEntity>()
        exchangeRate.forEach {
            val baseCurrency = it.key.split(source)[1]
            currencyRate.add(
                CurrencyExchangeEntity(
                    country = baseCurrency,
                    rate = it.value
                )
            )
        }
        return currencyRate
    }
}
