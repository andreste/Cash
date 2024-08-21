package com.example.cashapp.service

import com.example.cashapp.model.StocksResponse
import retrofit2.Response
import retrofit2.http.GET

interface StocksService {

    @GET("/portfolio.json")
    suspend fun getStocks(): Response<StocksResponse>
}