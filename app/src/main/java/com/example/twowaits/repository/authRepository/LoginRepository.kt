package com.example.twowaits.repository.authRepository

import androidx.lifecycle.MutableLiveData
import com.example.twowaits.model.auth.GettingTokensResponse
import com.example.twowaits.model.auth.LoginBody
import com.example.twowaits.model.auth.LoginResponse
import com.example.twowaits.network.ServiceBuilder
import com.example.twowaits.sealedClass.Response
import retrofit2.Call
import retrofit2.Callback

class LoginRepository {

    private val loginLiveData = MutableLiveData<Response<LoginResponse>>()
    fun login(loginBody: LoginBody): MutableLiveData<Response<LoginResponse>> {
        ServiceBuilder.getInstance().login(loginBody).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: retrofit2.Response<LoginResponse>
            ) {
                when {
                    response.isSuccessful ->
                        loginLiveData.postValue(Response.Success(response.body()!!))

                    response.code() == 401 -> loginLiveData.postValue(
                        Response.Error("Incorrect password")
                    )

                    response.code() == 406 -> loginLiveData.postValue(
                        Response.Error("No matching user found")
                    )

                    else -> loginLiveData.postValue(Response.Error(response.message()))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loginLiveData.postValue(Response.Error(t.message!!))
            }
        })
        return loginLiveData
    }

    private val tokenLiveData = MutableLiveData<Response<GettingTokensResponse>>()
    fun getAuthTokens(
        email: String,
        password: String
    ): MutableLiveData<Response<GettingTokensResponse>> {

        ServiceBuilder.getInstance().getAuthTokens(email, password)
            .enqueue(object : Callback<GettingTokensResponse> {
                override fun onResponse(
                    call: Call<GettingTokensResponse>,
                    response: retrofit2.Response<GettingTokensResponse>
                ) {
                    tokenLiveData.postValue(Response.Success(response.body()!!))
                }

                override fun onFailure(call: Call<GettingTokensResponse>, t: Throwable) {
                    tokenLiveData.postValue(Response.Error(t.message + "\nPlease try again"))
                }
            })
        return tokenLiveData
    }
}