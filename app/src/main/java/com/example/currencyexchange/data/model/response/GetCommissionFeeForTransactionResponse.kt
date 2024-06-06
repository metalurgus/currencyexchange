package com.example.currencyexchange.data.model.response

import com.example.currencyexchange.data.model.CommissionRule

data class GetCommissionFeeForTransactionResponse(
    val totalCommissionFee: Double,
    val ruleApplied: CommissionRule
)
