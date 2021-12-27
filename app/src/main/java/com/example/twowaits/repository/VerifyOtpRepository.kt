package com.example.twowaits.repository

import androidx.lifecycle.MutableLiveData
import com.example.twowaits.apiCalls.API
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.apiCalls.authApiCalls.VerifyOtpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerifyOtpRepository(private val api: API) {
    val errorMutableLiveData = MutableLiveData<String>()

    fun verifyOtp(email: String, otp: String){
        RetrofitClient.getInstance().create(API::class.java).verifyOtp(email, otp).enqueue(object :
            Callback<VerifyOtpResponse?> {
            override fun onResponse(
                call: Call<VerifyOtpResponse?>,
                response: Response<VerifyOtpResponse?>
            ) {
                if (response.isSuccessful) {
                    errorMutableLiveData.postValue("success")
                } else if (response.code() == 406) {
                    errorMutableLiveData.postValue("OTP expired! Please tap on Resend OTP button to get a new OTP")
                } else if (response.code() == 400) {
                    errorMutableLiveData.postValue("Wrong OTP!")
                }
            }
            override fun onFailure(call: Call<VerifyOtpResponse?>, t: Throwable) {
                errorMutableLiveData.postValue(t.message)
            }
        })
    }
}