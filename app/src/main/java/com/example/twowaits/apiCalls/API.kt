package com.example.twowaits.apiCalls

import com.example.twowaits.apiCalls.authApiCalls.*
import com.example.twowaits.apiCalls.dashboardApiCalls.FacultyProfileDetailsResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.GetNewRefreshTokenResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.QnAResponseItem
import com.example.twowaits.apiCalls.dashboardApiCalls.StudentProfileDetailsResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.quizApiCalls.AddCorrectOptionResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.quizApiCalls.AddQuestionsResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.quizApiCalls.CreateQuizResponse
import com.example.twowaits.homePages.quiz.AddCorrectOptionBody
import com.example.twowaits.homePages.quiz.CreateQuestionBody
import com.example.twowaits.homePages.quiz.CreateQuizBody
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface API {
    @FormUrlEncoded
    @POST("account/signup/")
    fun signUp(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<SignUpResponse>

    @FormUrlEncoded
    @POST("account/token/")
    suspend fun getAuthTokens(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<GettingTokensResponse>

    @FormUrlEncoded
    @POST("account/token/refresh/")
    suspend fun getNewAccessToken(
        @Field("refresh") refresh: String
    ): Response<GetNewRefreshTokenResponse>

    @FormUrlEncoded
    @POST("account/send-otp/")
    fun sendOtp(
        @Field("email") email: String
    ): Call<SendOtpResponse>

    @FormUrlEncoded
    @POST("account/login/")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("account/otp-verify/")
    fun verifyOtp(
        @Field("email") email: String,
        @Field("otp") otp: String
    ): Call<VerifyOtpResponse>

    @FormUrlEncoded
    @POST("account/forgot-reset/")
    fun resetPassword(
        @Field("email") email: String,
        @Field("new_password") password: String
    ): Call<VerifyOtpResponse>

    @GET("profile/")
    suspend fun profileDetailsFaculty(): Response<FacultyProfileDetailsResponse>

    @GET("profile/")
    suspend fun profileDetailsStudent(): Response<StudentProfileDetailsResponse>
    @FormUrlEncoded
    @PUT("profile/")
    suspend fun updateProfileDetails(
        @Field("name") name: String,
        @Field("college") college: String,
        @Field("course") course: String,
        @Field("branch") branch: String,
        @Field("year") year: String,
        @Field("interest") interest: String
    ): Response<StudentProfileDetailsResponse>

    @FormUrlEncoded
    @POST("profile/student/")
    suspend fun createStudentProfileDetails(
        @Field("name") name: String,
        @Field("college") college: String,
        @Field("course") course: String,
        @Field("branch") branch: String,
        @Field("year") year: String,
        @Field("interest") interest: String
    ): Response<StudentProfileDetailsResponse>

    @FormUrlEncoded
    @POST("profile/faculty/")
    suspend fun createFacultyProfileDetails(
        @Field("name") name: String,
        @Field("department") department: String
    ): Response<FacultyProfileDetailsResponse>

    @Multipart
    @PUT("profile/")
    suspend fun uploadProfilePic(
        @Part profile_pic: MultipartBody.Part,
        @Part("name") name: String
    ): Response<StudentProfileDetailsResponse>

    @GET("forum/")
    suspend fun getQnA(): Response<List<QnAResponseItem>>

    @POST("quiz/")
    fun createQuiz(
        @Body createQuizBody: CreateQuizBody
    ): Call<CreateQuizResponse>

    @POST("quiz/question/")
    suspend fun createQuestion(
        @Body createQuestionBody: CreateQuestionBody
    ): Response<AddQuestionsResponse>

    @POST("quiz/question/correct/")
    suspend fun addCorrectOption(
        @Body addCorrectOptionBody: AddCorrectOptionBody
    ): Response<AddCorrectOptionResponse>
}