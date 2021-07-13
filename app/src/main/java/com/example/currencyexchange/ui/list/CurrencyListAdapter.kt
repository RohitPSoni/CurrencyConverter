package com.example.currencyexchange.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyexchange.databinding.ViewAvailableCurrenciesBinding
import com.example.currencyexchange.ui.list.CurrencyListAdapter.CurrencyListViewHolder
import com.example.currencyexchange.ui.model.Country

class CurrencyListAdapter(val list: List<Country>) :
    RecyclerView.Adapter<CurrencyListViewHolder>() {

    class CurrencyListViewHolder(private val binding: ViewAvailableCurrenciesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(country: Country) {
            with(binding) {
                currencyAbbrevation.text = country.initial
                currencyName.text = country.exchange.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyListViewHolder {
        return CurrencyListViewHolder(
            ViewAvailableCurrenciesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CurrencyListViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

}