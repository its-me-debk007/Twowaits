package com.example.twowaits.network

import android.util.Log
import com.example.twowaits.utils.Utils
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ServiceBuilder {
    private const val BASE_URL = "http://3.110.33.189/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())

    fun getInstance(): ApiInterface {
        Log.e("ACCESS TOKEN STATUS", Utils.ACCESS_TOKEN.toString())
        if (Utils.ACCESS_TOKEN != null && Utils.ACCESS_TOKEN != "") {
            val tokenClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request()
                        .newBuilder()
                        .addHeader("Authorization", "Bearer ${Utils.ACCESS_TOKEN}")
                        .build()
                    chain.proceed(request)
                }
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            return retrofit.client(tokenClient)
                .build()
                .create(ApiInterface::class.java)

        } else {
            return retrofit.build()
                .create(ApiInterface::class.java)
        }
    }
}