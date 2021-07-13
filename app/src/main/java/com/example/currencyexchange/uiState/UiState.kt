package com.example.currencyexchange.uiState

import com.example.currencyexchange.ui.model.Country

sealed class UiState {
    object Loading: UiState()
    class Currencies(val timeStamp: Long, val currencies: List<Country>): UiState()
    class FailedUiState(val throwable: Throwable): UiState()
    class Countries(val countries: List<String>): UiState()
    class ExchangeRate(val rate: Double): UiState()
}
