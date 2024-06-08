package com.example.currencyexchange.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.currencyexchange.data.model.Balance
import com.example.currencyexchangetesttask.databinding.ListItemBalanceBinding

class BalanceAdapter : GenericAdapter<Balance, ListItemBalanceBinding>(BalanceDiffCallback()) {

    override fun inflateBinding(parent: ViewGroup): ListItemBalanceBinding {
        val inflater = LayoutInflater.from(parent.context)
        return ListItemBalanceBinding.inflate(inflater, parent, false)
    }

    override fun onBindViewHolder(item: Balance, binding: ViewBinding) {
        (binding as ListItemBalanceBinding).textViewBalance.text = item.toString()
    }
}

class BalanceDiffCallback : DiffCallback<Balance>()