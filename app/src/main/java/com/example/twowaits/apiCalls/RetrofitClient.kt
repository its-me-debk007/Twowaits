package com.example.twowaits.apiCalls

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    fun getInstance(): API {
        val baseUrl = "http://3.109.121.225/"

//        val okHttpClient = OkHttpClient.Builder()
//            .addInterceptor(networkInterceptor)
//            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
            .create(API::class.java)
    }
}