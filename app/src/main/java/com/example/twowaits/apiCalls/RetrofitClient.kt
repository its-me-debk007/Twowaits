package com.example.twowaits.apiCalls

import com.example.twowaits.Data
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    fun getInstance(): API {
        val baseUrl = "http://3.110.33.189/"

        val tokenInterceptor = Interceptor { chain ->
            var request = chain.request()
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer ${Data.ACCESS_TOKEN}")
                .build()
            chain.proceed(request)
        }

        if (Data.ACCESS_TOKEN != null) {
            val tokenClient = OkHttpClient.Builder()
                .addInterceptor(tokenInterceptor)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(tokenClient)
                .build()
                .create(API::class.java)
        } else {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(API::class.java)
        }
    }
}