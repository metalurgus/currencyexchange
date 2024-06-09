package com.example.currencyexchange.view.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class GenericAdapter<T : Any, VB : ViewBinding>(
    diffCallback: DiffCallback<T>
) : ListAdapter<T, GenericAdapter.GenericViewHolder<VB>>(diffCallback) {

    abstract fun inflateBinding(parent: ViewGroup): VB

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<VB> {
        val binding = inflateBinding(parent)
        return GenericViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenericViewHolder<VB>, position: Int) {
        onBindViewHolder(getItem(position), holder.binding)
    }

    abstract fun onBindViewHolder(item: T, binding: ViewBinding)

    override fun submitList(list: List<T>?) {
        super.submitList(list?.toMutableList()) //need to convert to mutable list for it to work
    }

    class GenericViewHolder<VB : ViewBinding>(val binding: VB) :
        RecyclerView.ViewHolder(binding.root)
}

abstract class DiffCallback<T> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T & Any, newItem: T & Any): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T & Any, newItem: T & Any): Boolean {
        return oldItem == newItem
    }
}