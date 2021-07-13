package com.example.currencyexchange.ui.exchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.currencyexchange.databinding.FragmentCurrencyExchangeBinding
import com.example.currencyexchange.ext.showHideUI
import com.example.currencyexchange.ui.exchange.viewModel.CurrencyExchangeViewModel
import com.example.currencyexchange.uiState.UiState.Countries
import com.example.currencyexchange.uiState.UiState.ExchangeRate
import com.example.currencyexchange.uiState.UiState.FailedUiState
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrencyExchangeFragment : Fragment() {

    private val currencyExchangeViewModel: CurrencyExchangeViewModel by viewModel()
    private lateinit var viewBinder: FragmentCurrencyExchangeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinder = FragmentCurrencyExchangeBinding.inflate(inflater, container, false)
        observeLiveData()
        populateData()
        return viewBinder.root
    }

    private fun populateData() {
        currencyExchangeViewModel.getAllCurrency()
        with(viewBinder) {
            convert.setOnClickListener {
                currencyExchangeViewModel.formatCurrency(
                    sourceCurrency = spinnerSource.selectedItem.toString(),
                    destinationCurrency = spinnerDestination.selectedItem.toString(),
                    amount = totalExchange.text.toString().toDouble()
                )
            }
        }
    }

    private fun observeLiveData() {
        currencyExchangeViewModel.uistate.observe(viewLifecycleOwner, {
            when (it) {
                is Countries -> {
                    showHideUI(View.GONE, viewBinder.errorLayout.root)
                    context?.let { cntx ->
                        val arrayAdapter =
                            ArrayAdapter(cntx, android.R.layout.simple_list_item_1, it.countries)
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                        with(viewBinder) {
                            spinnerSource.adapter = arrayAdapter
                            spinnerDestination.adapter = arrayAdapter
                        }
                    }
                }
                is ExchangeRate -> {
                    viewBinder.convertedCurrency.text = it.rate.toString()
                }
                is FailedUiState -> {
                    showHideUI(View.VISIBLE, viewBinder.errorLayout.root)
                    showHideUI(
                        visibility = View.GONE, views = arrayOf(
                            viewBinder.spinnerSource,
                            viewBinder.spinnerDestination,
                            viewBinder.convertedCurrency,
                            viewBinder.convert
                        )
                    )
                }
            }
        })
    }
}