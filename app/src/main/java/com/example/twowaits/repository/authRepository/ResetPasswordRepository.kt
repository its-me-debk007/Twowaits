package com.example.twowaits.repository.authRepository

import androidx.lifecycle.MutableLiveData
import com.example.twowaits.model.auth.VerifyOtpResponse
import com.example.twowaits.network.ServiceBuilder
import com.example.twowaits.sealedClass.Response
import retrofit2.Call
import retrofit2.Callback

class ResetPasswordRepository {
    private val liveData = MutableLiveData<Response<String>>()

    fun resetPassword(email: String, password: String): MutableLiveData<Response<String>> {
        ServiceBuilder.getInstance().resetPassword(email, password).enqueue(object :
            Callback<VerifyOtpResponse?> {
            override fun onResponse(
                call: Call<VerifyOtpResponse?>,
                response: retrofit2.Response<VerifyOtpResponse?>
            ) {
                if (response.isSuccessful)
                    liveData.postValue(Response.Success("success"))
                else if (response.code() == 406)
                    liveData.postValue(Response.Error("New password cannot be same as old one"))
            }

            override fun onFailure(call: Call<VerifyOtpResponse?>, t: Throwable) {
                liveData.postValue(
                    Response.Error(
                        if (t.message ==
                            "Failed to connect to /3.110.33.189:80"
                        ) "No Internet" else t.message!! + "! Please try again"
                    )
                )
            }
        })
        return liveData
    }
}