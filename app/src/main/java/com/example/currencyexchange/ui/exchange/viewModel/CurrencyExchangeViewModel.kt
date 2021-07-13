package com.example.currencyexchange.ui.exchange.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyexchange.repository.CurrencyExchangeRepository
import com.example.currencyexchange.uiState.UiState
import kotlinx.coroutines.launch

class CurrencyExchangeViewModel(
    private val repository: CurrencyExchangeRepository
) : ViewModel() {

    val uistate = MutableLiveData<UiState>()

    fun getAllCurrency() {
        viewModelScope.launch {
            uistate.postValue(repository.getAvailableCurrency())
        }
    }

    fun formatCurrency(sourceCurrency: String, destinationCurrency: String, amount: Double) {
        viewModelScope.launch {
            uistate.postValue(repository.formatCurrency(sourceCurrency, destinationCurrency, amount))
        }
    }
}