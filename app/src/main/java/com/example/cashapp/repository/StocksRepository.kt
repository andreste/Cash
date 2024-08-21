package com.example.cashapp.repository

import com.example.cashapp.service.StocksService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StocksRepository @Inject constructor(
    private val stocksService: StocksService
) {

    suspend fun getStocks() = stocksService.getStocks()

}