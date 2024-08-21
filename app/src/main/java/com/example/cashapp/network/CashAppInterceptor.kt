package com.example.cashapp.network

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CashAppInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Add the Authorization header here
        val builder = request.newBuilder().header(
            "Authorization",
            "",
        )

        return chain.proceed(builder.build())
    }
}
