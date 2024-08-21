package com.example.cashapp.hilt

import com.example.cashapp.network.CashAppInterceptor
import com.example.cashapp.service.StocksService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@OptIn(ExperimentalSerializationApi::class)
private val json =
    Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        coerceInputValues = true
        namingStrategy = JsonNamingStrategy.SnakeCase
    }

@InstallIn(SingletonComponent::class)
@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        fieldwireInterceptor: CashAppInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(fieldwireInterceptor)
        }.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl("https://storage.googleapis.com/cash-homework/cash-stocks-api/")
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    internal fun provideStocksService(retrofit: Retrofit): StocksService {
        return retrofit.create(StocksService::class.java)
    }
}