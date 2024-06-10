package com.example.currencyexchange.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.currencyexchange.data.model.Balance
import com.example.currencyexchangetesttask.databinding.ListItemBalanceAndRateBinding

class BalanceAdapter :
    GenericAdapter<Balance, ListItemBalanceAndRateBinding>(BalanceDiffCallback()) {

    override fun inflateBinding(parent: ViewGroup): ListItemBalanceAndRateBinding {
        val inflater = LayoutInflater.from(parent.context)
        return ListItemBalanceAndRateBinding.inflate(inflater, parent, false)
    }

    override fun onBindViewHolder(item: Balance, binding: ViewBinding) {
        (binding as ListItemBalanceAndRateBinding).textViewBalanceAndRate.text = item.toString()
    }
}

class BalanceDiffCallback : DiffCallback<Balance>()