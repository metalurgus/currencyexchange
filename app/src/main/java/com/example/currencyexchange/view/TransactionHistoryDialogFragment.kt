package com.example.currencyexchange.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyexchange.view.adapter.TransactionHistoryAdapter
import com.example.currencyexchange.viewmodel.TransactionHistoryViewModel
import com.example.currencyexchangetesttask.R
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransactionHistoryDialogFragment : DialogFragment() {
    private var adapter: TransactionHistoryAdapter? = null
    private val viewModel: TransactionHistoryViewModel by viewModel()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(
            requireContext(),
            android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen
        ).apply {
            window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_transaction_history, container, false)

        adapter = TransactionHistoryAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewTransactionHistory)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            (recyclerView.layoutManager as LinearLayoutManager).orientation
        )
        recyclerView.addItemDecoration(dividerItemDecoration)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.transactionHistoryViewState.collect {
                    adapter?.submitList(it.history)
                }
            }
        }
        viewModel.getTransactionHistory()
    }
}