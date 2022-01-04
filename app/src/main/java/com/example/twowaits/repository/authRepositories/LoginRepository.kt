package com.example.twowaits.repository.authRepositories

import androidx.lifecycle.MutableLiveData
import com.example.twowaits.apiCalls.API
import com.example.twowaits.apiCalls.authApiCalls.LoginResponse
import com.example.twowaits.apiCalls.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository(private val api: API) {
    val errorMutableLiveData = MutableLiveData<String>()

    fun login(email: String, password: String){
        RetrofitClient.getInstance().create(API::class.java).login(email, password).enqueue(object :
            Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                if (response.isSuccessful) {
                    errorMutableLiveData.postValue("success")
                } else if (response.code() == 406) {
                    errorMutableLiveData.postValue("No matching user found")
                }
                else if (response.code() == 401){
                    errorMutableLiveData.postValue("You've entered the wrong password. Please try again!")
                }
            }
            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                errorMutableLiveData.postValue(t.message)
            }
        })
    }
}