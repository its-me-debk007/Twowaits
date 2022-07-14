package com.example.twowaits.repository.authRepository

import androidx.lifecycle.MutableLiveData
import com.example.twowaits.network.ServiceBuilder
import com.example.twowaits.model.auth.VerifyOtpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPasswordRepository {
    val errorMutableLiveData = MutableLiveData<String>()

    fun resetPassword(email: String, password: String){
        ServiceBuilder.getInstance().resetPassword(email, password).enqueue(object :
            Callback<VerifyOtpResponse?> {
            override fun onResponse(
                call: Call<VerifyOtpResponse?>,
                response: Response<VerifyOtpResponse?>
            ) {
                if (response.isSuccessful) {
                    errorMutableLiveData.postValue("success")
                }
                else if (response.code() == 406){
                    errorMutableLiveData.postValue("New password cannot be same as old one")
                }
            }
            override fun onFailure(call: Call<VerifyOtpResponse?>, t: Throwable) {
                errorMutableLiveData.postValue(t.message)
            }
        })
    }
}