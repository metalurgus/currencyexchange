package com.example.currencyexchange.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.currencyexchange.data.model.ExchangeRate
import com.example.currencyexchangetesttask.databinding.ListItemBalanceBinding

class CurrencyRateAdapter :
    GenericAdapter<ExchangeRate, ListItemBalanceBinding>(CurrencyRateDiffCallback()) {

    override fun inflateBinding(parent: ViewGroup): ListItemBalanceBinding {
        val inflater = LayoutInflater.from(parent.context)
        return ListItemBalanceBinding.inflate(inflater, parent, false)
    }

    override fun onBindViewHolder(item: ExchangeRate, binding: ViewBinding) {
        (binding as ListItemBalanceBinding).textViewBalance.text = item.toString()
    }
}

class CurrencyRateDiffCallback : DiffCallback<ExchangeRate>()