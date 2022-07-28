package com.example.twowaits.repository.authRepository

import androidx.lifecycle.MutableLiveData
import com.example.twowaits.model.auth.VerifyOtpResponse
import com.example.twowaits.network.ServiceBuilder
import com.example.twowaits.sealedClass.Response
import com.example.twowaits.ui.fragment.auth.VerifyOtpBody
import kotlinx.coroutines.DelicateCoroutinesApi
import retrofit2.Call
import retrofit2.Callback

@DelicateCoroutinesApi
class VerifyOtpRepository {
    private val liveData = MutableLiveData<Response<String>>()

    fun verifyOtp(verifyOtpBody: VerifyOtpBody): MutableLiveData<Response<String>> {
        ServiceBuilder.getInstance().verifyOtp(verifyOtpBody)
            .enqueue(object : Callback<VerifyOtpResponse> {
                override fun onResponse(
                    call: Call<VerifyOtpResponse>,
                    response: retrofit2.Response<VerifyOtpResponse>
                ) {
                    when {
                        response.isSuccessful -> liveData.postValue(Response.Success("success"))
                        response.code() == 400 -> liveData.postValue(Response.Error("OTP is incorrect\nPlease try again"))
                        else -> liveData.postValue(Response.Error("${response.message()}\nPlease Try again"))
                    }
                }

                override fun onFailure(call: Call<VerifyOtpResponse>, t: Throwable) {
                    liveData.postValue(
                        Response.Error(
                            if (t.message ==
                                "Failed to connect to /3.110.33.189:80"
                            ) "No Internet" else t.message!!
                        )
                    )
                }
            })
        return liveData
    }
}