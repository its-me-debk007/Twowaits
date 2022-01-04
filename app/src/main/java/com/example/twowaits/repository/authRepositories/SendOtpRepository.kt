package com.example.twowaits.repository.authRepositories

import androidx.lifecycle.MutableLiveData
import com.example.twowaits.apiCalls.API
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.apiCalls.authApiCalls.SendOtpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SendOtpRepository(private val api: API) {
    val errorMutableLiveData = MutableLiveData<String>()

    fun sendOtp(email: String){
        RetrofitClient.getInstance().create(API::class.java).sendOtp(email).enqueue(object :
            Callback<SendOtpResponse?> {
            override fun onResponse(
                call: Call<SendOtpResponse?>,
                response: Response<SendOtpResponse?>
            ) {
                if (response.isSuccessful) {
                    errorMutableLiveData.postValue("success")
                } else if (response.code() == 400) {
                    errorMutableLiveData.postValue("User has not been registered. Please sign up first!")
                }
            }
            override fun onFailure(call: Call<SendOtpResponse?>, t: Throwable) {
                errorMutableLiveData.postValue(t.message)
            }
        })
    }
}