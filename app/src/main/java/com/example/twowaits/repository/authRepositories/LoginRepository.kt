package com.example.twowaits.repository.authRepositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.CompanionObjects
import com.example.twowaits.apiCalls.authApiCalls.LoginResponse
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.apiCalls.authApiCalls.GettingTokensResponse
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@DelicateCoroutinesApi
class LoginRepository {
    private val loginMutableLiveData = MutableLiveData<LoginResponse>()
    val loginLiveData: LiveData<LoginResponse> = loginMutableLiveData

    private val errorMutableLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = errorMutableLiveData

    fun login(email: String, password: String){
        GlobalScope.launch (Dispatchers.IO){
            val response = RetrofitClient.getInstance().login(email, password)
            when {
                response.isSuccessful -> {
                    loginMutableLiveData.postValue(response.body())
                }
                response.code() == 406 -> {
                    errorMutableLiveData.postValue("No matching user found")
                }
                response.code() == 401 -> {
                    errorMutableLiveData.postValue("You've entered the wrong password. Please try again!")
                }
                else -> {
                    errorMutableLiveData.postValue(response.body()?.message)
                }
            }
        }
    }

    fun getAuthTokens(email: String, password: String){
        GlobalScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.getInstance().getAuthTokens(email, password)
            if (response.isSuccessful){
                CompanionObjects.saveAccessToken("accessToken", response.body()!!.access)
                CompanionObjects.saveRefreshToken("refreshToken", response.body()!!.refresh)
                Log.d("TOKENS", response.body()!!.access)
            }
        }
    }
}