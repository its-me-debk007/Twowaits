package com.example.twowaits.repository.authRepositories

import androidx.lifecycle.MutableLiveData
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.apiCalls.authApiCalls.SignUpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpRepository {

    val errorMutableLiveData = MutableLiveData<String>()

    fun signUp(email: String, password: String){
        RetrofitClient.getInstance().signUp(email, password).enqueue(object : Callback<SignUpResponse?> {
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