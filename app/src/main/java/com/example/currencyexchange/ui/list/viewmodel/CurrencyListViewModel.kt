package com.example.currencyexchange.ui.list.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyexchange.repository.CurrencyExchangeRepository
import com.example.currencyexchange.uiState.UiState
import com.example.currencyexchange.uiState.UiState.Loading
import kotlinx.coroutines.launch

class CurrencyListViewModel(private val repository: CurrencyExchangeRepository): ViewModel() {
    val currencyData = MutableLiveData<UiState>()

    fun getLoadingData(){
        currencyData.value = Loading
        viewModelScope.launch {
            currencyData.postValue(repository.getAllCurrencyList())
        }
    }
}