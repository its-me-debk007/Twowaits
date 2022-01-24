package com.example.twowaits.apiCalls

import com.example.twowaits.apiCalls.authApiCalls.*
import com.example.twowaits.apiCalls.dashboardApiCalls.*
import com.example.twowaits.apiCalls.dashboardApiCalls.chatApiCalls.FetchConversationsMessagesResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.questionsAnswersApiCalls.AskQuestionResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.questionsAnswersApiCalls.BookmarkQuestionResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.questionsAnswersApiCalls.LikeAnswerResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.quizApiCalls.*
import com.example.twowaits.authPages.CreateFacultyProfileBody
import com.example.twowaits.authPages.CreateStudentProfileBody
import com.example.twowaits.homePages.UpdateProfileDetailsBody
import com.example.twowaits.homePages.questionsAnswers.AskQuestionBody
import com.example.twowaits.homePages.questionsAnswers.BookmarkQuestionBody
import com.example.twowaits.homePages.questionsAnswers.LikeAnswerBody
import com.example.twowaits.homePages.quiz.AddCorrectOptionBody
import com.example.twowaits.homePages.quiz.AttemptQuizBody
import com.example.twowaits.homePages.quiz.CreateQuestionBody
import com.example.twowaits.homePages.quiz.CreateQuizBody
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

    @PUT("profile/")
    suspend fun updateProfileDetails(
        @Body updateProfileDetailsBody: UpdateProfileDetailsBody
    ): Response<StudentProfileDetailsResponse>

    @POST("profile/student/")
    suspend fun createStudentProfileDetails(
        @Body createStudentProfileBody: CreateStudentProfileBody
    ): Response<StudentProfileDetailsResponse>

    @POST("profile/faculty/")
    suspend fun createFacultyProfileDetails(
        @Body createFacultyProfileBody: CreateFacultyProfileBody
    ): Response<FacultyProfileDetailsResponse>

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

    @POST("quiz/results/data/")
    suspend fun getQuizData(
        @Body attemptQuizBody: AttemptQuizBody
    ): Response<GetQuizDataResponse>

    @POST("quiz/results/attempt/")
    suspend fun attemptQuiz(
        @Body attemptQuizBody: AttemptQuizBody
    ): Response<AttemptQuizResponse>

    @POST("quiz/results/answer/")
    suspend fun registerResponse(
        @Body registerResponseBody: RegisterResponseBody
    ): Response<RegisterOptionsResponse>

    @POST("quiz/results/submit/")
    suspend fun viewScore(
        @Body attemptQuizBody: AttemptQuizBody
    ): Response<ViewScoreResponse>

    @POST("forum/question/")
    suspend fun askQuestion(
        @Body askQuestionBody: AskQuestionBody
    ): Response<AskQuestionResponse>

    @POST("forum/answer/like-unlike/")
    suspend fun likeAnswer(
        @Body likeAnswerBody: LikeAnswerBody
    ): Response<LikeAnswerResponse>

    @POST("forum/bookmark/")
    suspend fun bookmarkQuestion(
        @Body bookmarkQuestionBody: BookmarkQuestionBody
    ): Response<BookmarkQuestionResponse>

    @GET("forum/your-questions/")
    suspend fun getYourQnA(): Response<List<QnAResponseItem>>

    @GET("forum/your-bookmarked/")
    suspend fun getYourBookmarkedQ(): Response<List<QnAResponseItem>>

    @GET("quiz/testing")
    suspend fun recentQuizzes(): Response<List<RecentQuizzesResponse>>

    @GET("notes/view/")
    suspend fun recentNotes(): Response<List<RecentNotesResponse>>

    @GET("chat/conversation/")
    suspend fun fetchConversationsMessages(): Response<List<FetchConversationsMessagesResponse>>

}