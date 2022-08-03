package com.example.twowaits.network

import com.example.twowaits.network.dashboardApiCalls.*
import com.example.twowaits.network.dashboardApiCalls.questionsAnswersApiCalls.*
import com.example.twowaits.network.dashboardApiCalls.quizApiCalls.*
import com.example.twowaits.ui.fragment.auth.CreateFacultyProfileBody
import com.example.twowaits.ui.fragment.auth.CreateStudentProfileBody
import com.example.twowaits.ui.fragment.auth.VerifyOtpBody
import com.example.twowaits.model.home.UpdateProfilePic
import com.example.twowaits.model.home.UploadLectureBody
import com.example.twowaits.model.home.UploadNoteBody
import com.example.twowaits.ui.fragment.navDrawer.ChangePasswordBody
import com.example.twowaits.ui.fragment.navDrawer.Feedbackbody
import com.example.twowaits.model.AddCorrectOptionBody
import com.example.twowaits.model.AttemptQuizBody
import com.example.twowaits.model.CreateQuestionBody
import com.example.twowaits.model.CreateQuizBody
import com.example.twowaits.model.*
import com.example.twowaits.model.auth.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {
    @FormUrlEncoded
    @POST("account/signup/")
    fun signUp(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<SignUpResponse>

    @FormUrlEncoded
    @POST("account/token/")
    fun getAuthTokens(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<TokenResponse>

    @FormUrlEncoded
    @POST("account/token/refresh/")
    suspend fun generateNewToken(
        @Field("refresh") refresh: String
    ): Response<TokenResponse>

    @FormUrlEncoded
    @POST("account/send-otp/")
    fun sendOtp(
        @Field("email") email: String
    ): Call<SendOtpResponse>

    @POST("account/login/")
    fun login(
        @Body loginBody: LoginBody
    ): Call<LoginResponse>

    @POST("account/otp-verify/")
    fun verifyOtp(
        @Body verifyOtpBody: VerifyOtpBody
    ): Call<VerifyOtpResponse>

    @FormUrlEncoded
    @POST("account/forgot-reset/")
    fun resetPassword(
        @Field("email") email: String,
        @Field("new_password") password: String
    ): Call<VerifyOtpResponse>

    @GET("profile/")
    fun getProfile(): Call<ProfileDetails>

    @PUT("profile/")
    fun updateProfilePic(
        @Body body: UpdateProfilePic
    ): Call<ProfileDetails>

    @PUT("profile/")
    fun updateProfile(
        @Body body: ProfileDetailsExcludingId
    ): Call<ProfileDetails>

    @POST("profile/student/")
    suspend fun createStudentProfileDetails(
        @Body createStudentProfileBody: CreateStudentProfileBody
    ): Response<StudentProfileDetailsResponse>

    @POST("profile/faculty/")
    suspend fun createFacultyProfileDetails(
        @Body createFacultyProfileBody: CreateFacultyProfileBody
    ): Response<ProfileDetails>

    @GET("forum/")
    fun getQnA(): Call<List<QnAResponseItem>>

    @POST("quiz/")
    fun createQuiz(@Body createQuizBody: CreateQuizBody): Call<CreateQuizResponse>

    @POST("quiz/question/")
    suspend fun createQuestion(@Body createQuestionBody: CreateQuestionBody): Response<AddQuestionsResponse>

    @POST("quiz/question/correct/")
    suspend fun addCorrectOption(@Body addCorrectOptionBody: AddCorrectOptionBody): Response<AddCorrectOptionResponse>

    @POST("quiz/results/data/")
    fun getQuizData(
        @Body attemptQuizBody: AttemptQuizBody
    ): Call<GetQuizDataResponse>

    @POST("quiz/results/attempt/")
    fun attemptQuiz(
        @Body attemptQuizBody: AttemptQuizBody
    ): Call<AttemptQuizResponse>

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

    @POST("forum/answer/")
    fun createAnswer(
        @Body createAnswerBody: CreateAnswerBody
    ): Call<CreateAnswerResponse>

    @POST("forum/comment/")
    fun createComment(
        @Body createCommentBody: CreateCommentBody
    ): Call<CreateCommentResponse>

    @POST("forum/bookmark/")
    suspend fun bookmarkQuestion(
        @Body bookmarkQuestionBody: BookmarkQuestionBody
    ): Response<BookmarkQuestionResponse>

    @POST("notes/bookmark/")
    suspend fun bookmarkNote(@Body bookmarkNoteBody: BookmarkNoteBody): Response<BookmarkNoteResponse>

    @POST("lecture/wishlist/")
    suspend fun addToWishlist(@Body addToWishlistBody: AddToWishlistBody): Response<AddToWishlistResponse>

    @GET("forum/your-questions/")
    suspend fun getYourQnA(): Response<List<QnAResponseItem>>

    @GET("notes/your-bookmarked/")
    suspend fun getBookmarkedNotes(): Response<List<RecentNotesResponse>>

    @GET("lecture/your-wishlist/")
    fun getWishlist(): Call<List<RecentLecturesResponse>>

    @GET("forum/your-bookmarked/")
    suspend fun getYourBookmarkedQ(): Response<List<QnAResponseItem>>

    @GET("quiz/testing")
    fun recentQuizzes(): Call<List<RecentQuizzesResponse>>

    @GET("notes/view/")
    fun recentNotes(): Call<List<RecentNotesResponse>>

    @GET("lecture/view/")
    fun recentLectures(): Call<List<RecentLecturesResponse>>

    @POST("notes/")
    suspend fun uploadNote(
        @Body uploadNoteBody: UploadNoteBody
    ): Response<UploadNotesResponse>

    @POST("lecture/add/")
    suspend fun uploadLecture(
        @Body uploadLectureBody: UploadLectureBody
    ): Response<UploadLectureResponse>

    @POST("account/change-password/")
    fun changePassword(
        @Body changePasswordBody: ChangePasswordBody
    ): Call<SendOtpResponse>

    @GET("forum/")
    suspend fun searchQnA(
        @Query("search") search: String
    ): Response<List<QnAResponseItem>>

    @POST("profile/feedback/")
    fun feedback(
        @Body feedbackBody: Feedbackbody
    ): Call<FeedbackResponse>

    @GET("quiz/results/view/{id}/")
    suspend fun detailedQuizResult(@Path("id") id: Int): Response<DetailedQuizResultResponse>
}