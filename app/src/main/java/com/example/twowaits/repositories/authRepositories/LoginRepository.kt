package com.example.twowaits.repositories.authRepositories

import androidx.lifecycle.MutableLiveData
import com.example.twowaits.models.auth.LoginBody
import com.example.twowaits.network.ServiceBuilder
import com.example.twowaits.models.auth.GettingTokensResponse
import com.example.twowaits.models.auth.LoginResponse
import com.example.twowaits.sealedClasses.ErrorPojoClass
import com.example.twowaits.sealedClasses.Response
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback

class LoginRepository {
    fun login(loginBody: LoginBody): MutableLiveData<Response<LoginResponse>> {
        val liveData = MutableLiveData<Response<LoginResponse>>()
        val call = ServiceBuilder.getInstance().login(loginBody)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: retrofit2.Response<LoginResponse>
            ) {
                when {
                    response.isSuccessful ->
                        liveData.postValue(Response.Success(response.body()!!))

                    else -> {
                        val gson: Gson = GsonBuilder().create()
                        val mError = gson.fromJson(
                            response.errorBody()?.string(),
                            ErrorPojoClass::class.java
                        )
                        liveData.postValue(mError.message?.let { Response.Error(it) })
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                liveData.postValue(Response.Error(t.message + "\nPlease try again"))
            }
        })
        return liveData
    }

    fun getAuthTokens(
        email: String,
        password: String
    ): MutableLiveData<Response<GettingTokensResponse>> {
        val liveData = MutableLiveData<Response<GettingTokensResponse>>()
        val call = ServiceBuilder.getInstance().getAuthTokens(email, password)

        call.enqueue(object : Callback<GettingTokensResponse> {
            override fun onResponse(
                call: Call<GettingTokensResponse>,
                response: retrofit2.Response<GettingTokensResponse>
            ) {
                when {
                    response.isSuccessful ->
                        liveData.postValue(Response.Success(response.body()!!))

                    else -> {
                        val gson: Gson = GsonBuilder().create()
                        val mError = gson.fromJson(
                            response.errorBody()?.string(),
                            ErrorPojoClass::class.java
                        )
                        liveData.postValue(mError.message?.let { Response.Error(it) })
                    }
                }
            }

            override fun onFailure(call: Call<GettingTokensResponse>, t: Throwable) {
                liveData.postValue(Response.Error(t.message + "\nPlease try again"))
            }
        })
        return liveData
    }
}