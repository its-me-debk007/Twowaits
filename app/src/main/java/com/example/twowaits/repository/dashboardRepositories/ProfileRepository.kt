package com.example.twowaits.repository.dashboardRepositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.apiCalls.API
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.apiCalls.authApiCalls.SignUpResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.ProfileDetailsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class ProfileRepository {
    val profileMutableLiveData = MutableLiveData<ProfileDetailsResponse>()
    val errorMutableLiveData = MutableLiveData<String>()

    fun profileDetails(authToken: String) {
        RetrofitClient.getInstance().create(API::class.java).profileDetails(authToken).enqueue(object : Callback<ProfileDetailsResponse?>
        {
            override fun onResponse(
                call: Call<ProfileDetailsResponse?>,
                response: Response<ProfileDetailsResponse?>
            ) {
                if (response.isSuccessful) {
                    profileMutableLiveData.postValue(response.body())
                } else {
                    errorMutableLiveData.postValue("Error code is ${response.code()}\n${response.body()?.detail}")
                }
            }

            override fun onFailure(call: Call<ProfileDetailsResponse?>, t: Throwable) {
                errorMutableLiveData.postValue(t.message)
            }
        }
        )
        }
}