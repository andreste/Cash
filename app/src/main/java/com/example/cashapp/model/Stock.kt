package com.example.cashapp.model

import kotlinx.serialization.Serializable

@Serializable
data class Stock(
    val ticker: String,
    val name: String,
    val currency: String,
    val currentPriceCents: Double,
    val quantity: Int? = null,
    val currentPriceTimestamp: String? = null
)