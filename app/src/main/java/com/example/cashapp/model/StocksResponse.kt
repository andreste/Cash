package com.example.cashapp.model

import kotlinx.serialization.Serializable

@Serializable
data class StocksResponse(
    val stocks: List<Stock>
)