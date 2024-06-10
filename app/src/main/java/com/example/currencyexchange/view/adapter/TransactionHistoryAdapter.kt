package com.example.currencyexchange.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyexchange.data.model.TransactionRecord
import com.example.currencyexchange.data.util.floorTo2DecimalPlaces
import com.example.currencyexchangetesttask.R
import java.text.SimpleDateFormat
import java.util.*

class TransactionHistoryAdapter :
    ListAdapter<TransactionRecord, TransactionHistoryAdapter.TransactionViewHolder>(
        TransactionComparator()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_transaction_history, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fromToCurrency: TextView = itemView.findViewById(R.id.fromToCurrency)
        private val fromToAmount: TextView = itemView.findViewById(R.id.fromToAmount)
        private val dateTextView: TextView = itemView.findViewById(R.id.date)
        private val statusTextView: TextView = itemView.findViewById(R.id.status)

        fun bind(transaction: TransactionRecord) {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            val transactionDate = Date(transaction.date)
            dateTextView.text = sdf.format(transactionDate)
            fromToCurrency.text = itemView.context.getString(
                R.string.from_to_currency,
                transaction.fromCurrency,
                transaction.toCurrency
            )
            fromToAmount.text = itemView.context.getString(
                R.string.from_to_amount,
                transaction.amount.floorTo2DecimalPlaces(),
                ((transaction.amount - transaction.commissionFee) * transaction.rate).floorTo2DecimalPlaces()
            )
            statusTextView.text = itemView.context.getString(
                if (transaction.isSuccess) R.string.success else R.string.failed
            )
        }
    }
}

class TransactionComparator : DiffUtil.ItemCallback<TransactionRecord>() {
    override fun areItemsTheSame(oldItem: TransactionRecord, newItem: TransactionRecord): Boolean {
        return oldItem.transactionNumber == newItem.transactionNumber
    }

    override fun areContentsTheSame(
        oldItem: TransactionRecord,
        newItem: TransactionRecord
    ): Boolean {
        return oldItem == newItem
    }
}