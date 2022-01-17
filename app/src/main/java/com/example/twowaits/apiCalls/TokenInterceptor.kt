package com.example.twowaits.apiCalls

import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder().header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNjQyNTAyNDg5LCJpYXQiOjE2NDI0MTYwODksImp0aSI6Ijc5MjJhMjVlN2FjMTQ2MzJiNzQ0YzU1ZWRkM2ZmNDIxIiwidXNlcl9pZCI6Nn0.e6Xpfkj1QKYHgJkbr2_YJuZCqFHpWcK3WK2HXE1DvB0")
            .build()
        return chain.proceed(request)
    }
}