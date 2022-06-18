package com.example.twowaits.repositories.authRepositories

import androidx.lifecycle.MutableLiveData
import com.example.twowaits.network.ServiceBuilder
import com.example.twowaits.models.auth.SendOtpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SendOtpRepository {
    val errorMutableLiveData = MutableLiveData<String>()

    fun sendOtp(email: String){
        ServiceBuilder.getInstance().sendOtp(email).enqueue(object :
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