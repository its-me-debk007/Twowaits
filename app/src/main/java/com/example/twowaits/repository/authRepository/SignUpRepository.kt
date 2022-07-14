package com.example.twowaits.repository.authRepository

import androidx.lifecycle.MutableLiveData
import com.example.twowaits.network.ServiceBuilder
import com.example.twowaits.model.auth.SignUpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpRepository {

    val errorMutableLiveData = MutableLiveData<String>()

    fun signUp(email: String, password: String){
        ServiceBuilder.getInstance().signUp(email, password).enqueue(object : Callback<SignUpResponse?> {
            override fun onResponse(
                call: Call<SignUpResponse?>,
                response: Response<SignUpResponse?>
            ) {
                if (response.isSuccessful) {
                    errorMutableLiveData.postValue("success")
                } else if (response.code() == 401) {
                    errorMutableLiveData.postValue("User has been already registered!")
                }
            }
            override fun onFailure(call: Call<SignUpResponse?>, t: Throwable) {
                errorMutableLiveData.postValue(t.message)
            }
        })
        }
    }