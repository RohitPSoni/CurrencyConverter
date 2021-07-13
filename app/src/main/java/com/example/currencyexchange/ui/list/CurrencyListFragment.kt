package com.example.currencyexchange.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.currencyexchange.R
import com.example.currencyexchange.databinding.FragmentCurrencyListBinding
import com.example.currencyexchange.ext.showHideUI
import com.example.currencyexchange.ext.toDisplayString
import com.example.currencyexchange.ui.list.viewmodel.CurrencyListViewModel
import com.example.currencyexchange.uiState.UiState.Currencies
import com.example.currencyexchange.uiState.UiState.FailedUiState
import com.example.currencyexchange.uiState.UiState.Loading
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrencyListFragment : Fragment() {

    private val currencyListViewModel: CurrencyListViewModel by viewModel()

    private lateinit var viewBinder: FragmentCurrencyListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinder = FragmentCurrencyListBinding.inflate(layoutInflater, container, false)
        observeLiveData()
        currencyListViewModel.getLoadingData()
        viewBinder.btnExchange.setOnClickListener {
            findNavController().navigate(R.id.action_currencyList_to_currencyExchangeFragment)
        }
        return viewBinder.root
    }

    private fun observeLiveData() {
        currencyListViewModel.currencyData.observe(viewLifecycleOwner, {
            when (it) {
                is Loading -> {
                    showHideUI(visibility = VISIBLE, views = arrayOf(viewBinder.loading))
                    showHideUI(
                        visibility = GONE, views = arrayOf(
                            viewBinder.txvCurrent,
                            viewBinder.txvAstUpdated,
                            viewBinder.btnExchange,
                            viewBinder.currencyList,
                            viewBinder.errorLayout.root
                        )
                    )
                }
                is Currencies -> {
                    showHideUI(visibility = GONE, views = arrayOf(viewBinder.loading))
                    showHideUI(
                        visibility = VISIBLE, views = arrayOf(
                            viewBinder.txvCurrent,
                            viewBinder.txvAstUpdated,
                            viewBinder.btnExchange,
                            viewBinder.currencyList
                        )
                    )
                    with(viewBinder) {
                        txvAstUpdated.text = getString(
                            R.string.last_updated,
                            it.timeStamp.toDisplayString()
                        )
                        val adapter = CurrencyListAdapter(it.currencies)
                        currencyList.adapter = adapter
                    }
                }
                is FailedUiState -> {
                    showHideUI(visibility = GONE, views = arrayOf(viewBinder.loading))
                    showHideUI(
                        visibility = VISIBLE, views = arrayOf(
                            viewBinder.errorLayout.root
                        )
                    )
                    viewBinder.errorLayout.errorInfo.text = it.throwable.message
                }
            }
        })
    }
}