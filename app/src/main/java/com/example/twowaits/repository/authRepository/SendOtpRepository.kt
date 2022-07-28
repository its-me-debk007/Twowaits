package com.example.twowaits.repository.authRepository

import androidx.lifecycle.MutableLiveData
import com.example.twowaits.network.ServiceBuilder
import com.example.twowaits.model.auth.SendOtpResponse
import com.example.twowaits.sealedClass.Response
import retrofit2.Call
import retrofit2.Callback

class SendOtpRepository {
    private val liveData = MutableLiveData<Response<String>>()

    fun sendOtp(email: String): MutableLiveData<Response<String>>{
        ServiceBuilder.getInstance().sendOtp(email).enqueue(object :
            Callback<SendOtpResponse?> {
            override fun onResponse(
                call: Call<SendOtpResponse?>,
                response: retrofit2.Response<SendOtpResponse?>
            ) {
                if (response.isSuccessful) {
                    liveData.postValue(Response.Success("success"))
                } else if (response.code() == 400) {
                    liveData.postValue(Response.Error("User has not been registered. Please sign up first!"))
                }
            }
            override fun onFailure(call: Call<SendOtpResponse?>, t: Throwable) {
                liveData.postValue(Response.Error(if (t.message ==
                    "Failed to connect to /3.110.33.189:80") "No Internet" else t.message!! + "! Please try again"))
            }
        })
        return liveData
    }
}