package com.example.twowaits.network

import com.example.twowaits.utils.Utils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceBuilder {
    private const val baseURL = "http://3.110.33.189/"

    fun getInstance(): ApiInterface {

        val tokenInterceptor = Interceptor { chain ->
            var request = chain.request()
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer ${Utils.ACCESS_TOKEN}")
                .build()
            chain.proceed(request)
        }

        if (Utils.ACCESS_TOKEN != null) {
            val tokenClient = OkHttpClient.Builder()
                .addInterceptor(tokenInterceptor)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(tokenClient)
                .build()
                .create(ApiInterface::class.java)
        } else {
            return Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface::class.java)
        }
    }
}