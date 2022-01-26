package com.example.twowaits.apiCalls

import com.example.twowaits.CompanionObjects
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
//        val token = if (CompanionObjects.ACCESS_TOKEN == null) "ABC" else CompanionObjects.ACCESS_TOKEN
//        val request = chain.request().newBuilder().header("Authorization", "Bearer $token")
        val request = chain.request().newBuilder().header("Authorization", "Bearer ${CompanionObjects.ACCESS_TOKEN}")
            .build()
        return chain.proceed(request)
    }
}