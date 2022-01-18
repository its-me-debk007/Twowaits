package com.example.twowaits.apiCalls

import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder().header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNjQyNTgwOTkwLCJpYXQiOjE2NDI0OTQ1OTAsImp0aSI6IjJjNzk1NzE5OWQ2YTRiZjliZmE0NDNiNWNiZmY5MTZkIiwidXNlcl9pZCI6Nn0.frlLrDP6S1JJ0ZIcY7jnLw9glgafpGoTRS_2eZGxxXg")
            .build()
        return chain.proceed(request)
    }
}