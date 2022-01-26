package com.example.twowaits.apiCalls

import android.util.Log
import com.example.twowaits.CompanionObjects
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    fun getInstance(): API {
        val baseUrl = "http://3.110.33.189/"

        if (CompanionObjects.USER[0] == 'F' || CompanionObjects.USER[0] == 'S') {
            Log.d("VVVV", "Yes bearer token")
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
        else {
            Log.d("VVVV", "No bearer token")
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(API::class.java)
        }
    }
}