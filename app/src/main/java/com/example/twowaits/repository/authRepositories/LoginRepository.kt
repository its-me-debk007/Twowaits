package com.example.twowaits.repository.authRepositories

import androidx.lifecycle.MutableLiveData
import com.example.twowaits.CompanionObjects
import com.example.twowaits.apiCalls.authApiCalls.LoginResponse
import com.example.twowaits.apiCalls.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository {
    val errorMutableLiveData = MutableLiveData<String>()

    fun login(email: String, password: String){
        RetrofitClient.getInstance().login(email, password).enqueue(object :
            Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                when {
                    response.isSuccessful -> {
                        errorMutableLiveData.postValue("success")
                    }
                    response.code() == 406 -> {
                        errorMutableLiveData.postValue("No matching user found")
                    }
                    response.code() == 401 -> {
                        errorMutableLiveData.postValue("You've entered the wrong password. Please try again!")
                    }
                }
            }
            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                errorMutableLiveData.postValue(t.message)
            }
        })
    }
    fun getTokens(email: String, password: String){
        GlobalScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.getInstance().getTokens(email, password)
            if (response.isSuccessful){
                CompanionObjects.saveAccessToken("accessToken", response.body()!!.access)
                CompanionObjects.saveRefreshToken("refreshToken", response.body()!!.refresh)
            }
        }
    }
}