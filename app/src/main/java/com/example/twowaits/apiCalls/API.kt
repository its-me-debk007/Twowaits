package com.example.twowaits.apiCalls

import com.example.twowaits.apiCalls.authApiCalls.*
import com.example.twowaits.apiCalls.dashboardApiCalls.FacultyProfileDetailsResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.StudentProfileDetailsResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface API {
    @FormUrlEncoded
    @POST("account/signup/")
    fun signUp(
        @Field("email")email: String,
        @Field("password")password: String
    ): Call<SignUpResponse>

    @FormUrlEncoded
    @POST("account/token/")
    suspend fun getTokens(
        @Field("email")email: String,
        @Field("password")password: String
    ): Response<GettingTokensResponse>

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

    @GET("profile/")
    suspend  fun profileDetails(
        @Header("Authorization")authToken: String
    ): Response<StudentProfileDetailsResponse>

    @FormUrlEncoded
    @PUT("profile/")
    suspend  fun updateProfileDetails(
        @Header("Authorization")authToken: String,
        @Field("name")name: String,
        @Field("college")college: String,
        @Field("course")course: String,
        @Field("branch")branch: String,
        @Field("year")year: String,
        @Field("interest")interest: String,
    ): Response<StudentProfileDetailsResponse>

    @FormUrlEncoded
    @POST("profile/student/")
    suspend  fun createStudentProfileDetails(
        @Header("Authorization")authToken: String,
        @Field("name")name: String,
        @Field("college")college: String,
        @Field("course")course: String,
        @Field("branch")branch: String,
        @Field("year")year: String,
        @Field("interest")interest: String,
    ): Response<StudentProfileDetailsResponse>

    @FormUrlEncoded
    @POST("profile/faculty/")
    suspend  fun createFacultyProfileDetails(
        @Header("Authorization")authToken: String,
        @Field("name")name: String,
        @Field("department")department: String,
    ): Response<FacultyProfileDetailsResponse>

}