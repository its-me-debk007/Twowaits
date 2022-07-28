package com.example.twowaits.repository.authRepository

import androidx.lifecycle.MutableLiveData
import com.example.twowaits.model.auth.SignUpResponse
import com.example.twowaits.network.ServiceBuilder
import com.example.twowaits.sealedClass.Response
import retrofit2.Call
import retrofit2.Callback

class SignUpRepository {
    private val signUpLiveData = MutableLiveData<Response<String>>()

    fun signUp(email: String, password: String): MutableLiveData<Response<String>> {
        ServiceBuilder.getInstance().signUp(email, password)
            .enqueue(object : Callback<SignUpResponse?> {
                override fun onResponse(
                    call: Call<SignUpResponse?>,
                    response: retrofit2.Response<SignUpResponse?>
                ) {
                    if (response.isSuccessful) signUpLiveData.postValue(Response.Success("success"))
                    else if (response.code() == 401)
                        signUpLiveData.postValue(Response.Error("User has been already registered!"))
                }

                override fun onFailure(call: Call<SignUpResponse?>, t: Throwable) {
                    signUpLiveData.postValue(Response.Error(if (t.message ==
                        "Failed to connect to /3.110.33.189:80") "No Internet" else t.message!! + "! Please try again"))
                }
            })
        return signUpLiveData
    }
}