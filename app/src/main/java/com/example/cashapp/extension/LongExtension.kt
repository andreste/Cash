package com.example.cashapp.extension

import java.text.NumberFormat
import java.util.Locale

fun Long.toDollars(): String {
    val dollars = this / 100.0
    val format = NumberFormat.getCurrencyInstance(Locale.US)
    return format.format(dollars)
}