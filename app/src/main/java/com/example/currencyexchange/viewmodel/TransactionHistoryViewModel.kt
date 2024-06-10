package com.example.currencyexchange.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyexchange.data.Result
import com.example.currencyexchange.data.model.TransactionRecord
import com.example.currencyexchange.data.usecase.GetUserTransactionHistoryUseCase
import com.example.currencyexchange.data.usecase.UseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionHistoryViewModel(
    private val transactionHistoryUseCase: GetUserTransactionHistoryUseCase
) : ViewModel() {
    private val _transactionHistoryViewStateFlow = MutableStateFlow(TransactionHistoryState())
    val transactionHistoryViewState = _transactionHistoryViewStateFlow.asStateFlow()

    fun getTransactionHistory() {
        viewModelScope.launch {
            transactionHistoryUseCase.run(UseCase.None).collect { transactionHistoryResult ->
                when (transactionHistoryResult) {
                    is Result.Success -> {
                        _transactionHistoryViewStateFlow.update { state ->
                            state.copy(history = transactionHistoryResult.data)
                        }
                    }

                    else -> Unit
                }

            }
        }
    }
}

data class TransactionHistoryState(val history: List<TransactionRecord> = emptyList())
