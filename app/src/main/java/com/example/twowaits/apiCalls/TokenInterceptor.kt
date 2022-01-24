package com.example.twowaits.apiCalls

import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNjQzMTE0NzI1LCJpYXQiOjE2NDMwMjgzMjUsImp0aSI6Ijg2MTlhNDg1YzViZjRhZTA4MWFjZTAxZjIzOThlMDc1IiwidXNlcl9pZCI6Nn0.AbxvrSW-HKn8NaagvylaqrRy9R9iab0KI3Z8-TK9ag8")
            .build()
        return chain.proceed(request)
    }
}