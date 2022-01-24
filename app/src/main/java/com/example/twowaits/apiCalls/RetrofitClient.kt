package com.example.twowaits.apiCalls

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    fun getInstance(): API {
        val baseUrl = "http://3.110.33.189/"

        val tokenClient = OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(tokenClient)
            .build()
            .create(API::class.java)
    }
}