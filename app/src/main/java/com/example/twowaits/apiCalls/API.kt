package com.example.twowaits.apiCalls

import com.example.twowaits.apiCalls.authApiCalls.LoginResponse
import com.example.twowaits.apiCalls.authApiCalls.SendOtpResponse
import com.example.twowaits.apiCalls.authApiCalls.SignUpResponse
import com.example.twowaits.apiCalls.authApiCalls.VerifyOtpResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface API {
    @FormUrlEncoded
    @POST("account/signup/")
    fun signUp(
        @Field("email")email: String,
        @Field("password")password: String
    ): Call<SignUpResponse>

    @FormUrlEncoded
    @POST("account/send-otp/")
    fun sendOtp(
        @Field("email")email: String,
    ): Call<SendOtpResponse>

    @FormUrlEncoded
    @POST("account/login/")
    fun login(
        @Field("email")email: String,
        @Field("password")password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("account/otp-verify/")
    fun verifyOtp(
        @Field("email")email: String,
        @Field("otp")otp: String
    ): Call<VerifyOtpResponse>

    @FormUrlEncoded
    @POST("account/forgot-reset/")
    fun resetPassword(
        @Field("email")email: String,
        @Field("new_password")password: String
    ): Call<VerifyOtpResponse>

}