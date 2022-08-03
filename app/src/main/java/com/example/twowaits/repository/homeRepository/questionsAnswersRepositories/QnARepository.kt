package com.example.twowaits.repository.homeRepository.questionsAnswersRepositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.model.*
import com.example.twowaits.network.ServiceBuilder
import com.example.twowaits.network.dashboardApiCalls.AddToWishlistBody
import com.example.twowaits.network.dashboardApiCalls.QnAResponseItem
import com.example.twowaits.network.dashboardApiCalls.RecentLecturesResponse
import com.example.twowaits.network.dashboardApiCalls.RecentNotesResponse
import com.example.twowaits.network.dashboardApiCalls.questionsAnswersApiCalls.*
import com.example.twowaits.sealedClass.Response
import com.example.twowaits.util.Utils
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback

@DelicateCoroutinesApi
class QnARepository {
    private val q_n_aMutableLiveData = MutableLiveData<List<QnAResponseItem>>()
    val q_n_aLiveData: LiveData<List<QnAResponseItem>> = q_n_aMutableLiveData
    private val getQnAData = MutableLiveData<List<QnAResponseItem>>()
    val getQnALiveData: LiveData<List<QnAResponseItem>> = getQnAData
    private val errorGetQnAData = MutableLiveData<String>()
    val errorGetQnALiveData: LiveData<String> = errorGetQnAData
    private val errorMutableLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = errorMutableLiveData
    private val askQuestionData = MutableLiveData<AskQuestionResponse>()
    val askQuestionLiveData: LiveData<AskQuestionResponse> = askQuestionData
    private val errorAskQuestionData = MutableLiveData<String>()
    val errorAskQuestionLiveData: LiveData<String> = errorAskQuestionData
    private val likeAnswerData = MutableLiveData<LikeAnswerResponse>()
    val likeAnswerLiveData: LiveData<LikeAnswerResponse> = likeAnswerData
    private val errorLikeAnswerData = MutableLiveData<String>()
    val errorLikeAnswerLiveData: LiveData<String> = errorLikeAnswerData
    private val bookmarkQuestionData = MutableLiveData<BookmarkQuestionResponse>()
    val bookmarkQuestionLiveData: LiveData<BookmarkQuestionResponse> = bookmarkQuestionData
    private val errorBookmarkQuestionData = MutableLiveData<String>()
    val errorBookmarkQuestionLiveData: LiveData<String> = errorBookmarkQuestionData
    private val getBookmarkedQData = MutableLiveData<List<QnAResponseItem>>()
    val getBookmarkedQLiveData: LiveData<List<QnAResponseItem>> = getBookmarkedQData
    private val errorGetBookmarkedQData = MutableLiveData<String>()
    val errorGetBookmarkedQLiveData: LiveData<String> = errorGetBookmarkedQData

    val bookmarkNoteData = MutableLiveData<String>()
    val addToWishlistData = MutableLiveData<String>()
    val getSearchQnAData = MutableLiveData<List<QnAResponseItem>>()
    val errorGetSearchQnAData = MutableLiveData<String>()
    val bookmarkedNotesData = MutableLiveData<List<RecentNotesResponse>>()
    val errorBookmarkedNotesData = MutableLiveData<String>()

    fun getYourQnA() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = ServiceBuilder.getInstance().getYourQnA()
            when {
                response.isSuccessful -> q_n_aMutableLiveData.postValue(response.body())
                response.code() == 400 -> {
                    Utils().getNewAccessToken()
                    getYourQnA()
                }
                else -> errorMutableLiveData.postValue("${response.message()}\nPlease try again")
            }
        }
    }

    fun getQnA(): MutableLiveData<Response<List<QnAResponseItem>>> {
        val liveData = MutableLiveData<Response<List<QnAResponseItem>>>()
        val call = ServiceBuilder.getInstance().getQnA()

        call.enqueue(object : Callback<List<QnAResponseItem>> {
            override fun onResponse(
                call: Call<List<QnAResponseItem>>,
                response: retrofit2.Response<List<QnAResponseItem>>
            ) {
                when {
                    response.isSuccessful ->
                        liveData.postValue(Response.Success(response.body()))

                    response.code() == 400 -> {
                        val result = Utils().getNewAccessToken()
                        Log.e("dddd", "token expire")
//                        if (result == "success") getQnA()
//                        else liveData.postValue(Response.Error("Some error has occurred!\nPlease try again"))
                        getQnA()
                    }

                    else -> liveData.postValue(Response.Error(response.message() + "\nPlease try again"))
                }
            }

            override fun onFailure(call: Call<List<QnAResponseItem>>, t: Throwable) {
                liveData.postValue(Response.Error(t.message + "\nPlease try again"))
            }
        })
        return liveData
    }

    fun askQuestion(askQuestionBody: AskQuestionBody) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = ServiceBuilder.getInstance().askQuestion(askQuestionBody)
            when {
                response.isSuccessful -> {
                    askQuestionData.postValue(response.body())
                }
                response.code() == 400 -> {
                    val result = Utils().getNewAccessToken()
//                    if (result == "success") askQuestion(askQuestionBody)
//                    else
//                        errorAskQuestionData.postValue("Some error has occurred!\nPlease try again")
                    askQuestion(askQuestionBody)
                }
                else ->
                    errorAskQuestionData.postValue("${response.code()}\nPlease try again")
            }
        }
    }

    fun likeAnswer(likeAnswerBody: LikeAnswerBody) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = ServiceBuilder.getInstance().likeAnswer(likeAnswerBody)
            when {
                response.isSuccessful -> {
                    likeAnswerData.postValue(response.body())
                }
                response.code() == 400 -> {
                    val result = Utils().getNewAccessToken()
//                    if (result == "success") likeAnswer(likeAnswerBody)
//                    else
//                        errorLikeAnswerData.postValue("Some error has occurred!\nPlease try again")
                    likeAnswer(likeAnswerBody)
                }
                else ->
                    errorLikeAnswerData.postValue("${response.message()}\nPlease refresh the page")
            }
        }
    }

    fun bookmarkQuestion(bookmarkQuestionBody: BookmarkQuestionBody) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = ServiceBuilder.getInstance().bookmarkQuestion(bookmarkQuestionBody)
            when {
                response.isSuccessful -> {
                    bookmarkQuestionData.postValue(response.body())
                }
                response.code() == 400 -> {
                    val result = Utils().getNewAccessToken()
//                    if (result == "success") bookmarkQuestion(bookmarkQuestionBody)
//                    else
//                        errorBookmarkQuestionData.postValue("Some error has occurred!\nPlease try again")
                    bookmarkQuestion(bookmarkQuestionBody)
                }
                else ->
                    errorBookmarkQuestionData.postValue("${response.message()}\nPlease refresh the page")
            }
        }
    }

    fun bookmarkNote(bookmarkNoteBody: BookmarkNoteBody) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = ServiceBuilder.getInstance().bookmarkNote(bookmarkNoteBody)
            when {
                response.isSuccessful -> {
                    bookmarkNoteData.postValue("success")
                }
                response.code() == 400 -> {
                    val result = Utils().getNewAccessToken()
//                    if (result == "success") bookmarkNote(bookmarkNoteBody)
//                    else
//                        bookmarkNoteData.postValue("Some error has occurred!\nPlease try again")
                    bookmarkNote(bookmarkNoteBody)
                }
                else -> {
                    bookmarkNoteData.postValue("${response.message()}\nPlease refresh the page")
                }
            }
        }
    }

    fun addToWishlist(addToWishlistBody: AddToWishlistBody) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = ServiceBuilder.getInstance().addToWishlist(addToWishlistBody)
            when {
                response.isSuccessful -> {
                    addToWishlistData.postValue("success")
                }
                response.code() == 400 -> {
                    val result = Utils().getNewAccessToken()
//                    if (result == "success") addToWishlist(addToWishlistBody)
//                    else
//                        addToWishlistData.postValue("Some error has occurred!\nPlease try again")
                    addToWishlist(addToWishlistBody)
                }
                else ->
                    addToWishlistData.postValue("${response.message()}\nPlease refresh the page")
            }
        }
    }

    fun getYourBookmarkedQ() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = ServiceBuilder.getInstance().getYourBookmarkedQ()
            when {
                response.isSuccessful -> {
                    getBookmarkedQData.postValue(response.body())
                }
                response.code() == 400 -> {
                    Utils().getNewAccessToken()
                    getYourBookmarkedQ()
                }
                else ->
                    errorGetBookmarkedQData.postValue("${response.message()}\nPlease refresh the page")
            }
        }
    }

    fun getBookmarkedNotes() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = ServiceBuilder.getInstance().getBookmarkedNotes()
            when {
                response.isSuccessful -> {
                    bookmarkedNotesData.postValue(response.body())
                }
                response.code() == 400 -> {
                    val result = Utils().getNewAccessToken()
//                    if (result == "success") getBookmarkedNotes()
//                    else
//                        errorBookmarkedNotesData.postValue("Some error has occurred!\nPlease try again")
                    getBookmarkedNotes()
                }
                else ->
                    errorBookmarkedNotesData.postValue("${response.message()}\nPlease refresh the page")
            }
        }
    }

    private val createAnswerData = MutableLiveData<Response<String>>()
    fun createAnswer(createAnswerBody: CreateAnswerBody): MutableLiveData<Response<String>> {
            ServiceBuilder.getInstance().createAnswer(createAnswerBody).enqueue(object : Callback<CreateAnswerResponse> {
                override fun onResponse(
                    call: Call<CreateAnswerResponse>,
                    response: retrofit2.Response<CreateAnswerResponse>
                ) {
                    when {
                        response.isSuccessful -> createAnswerData.postValue(Response.Success("success"))

                        response.code() == 400 -> {
                            Utils().getNewAccessToken()
                            createAnswer(createAnswerBody)
                        }
                        else -> createAnswerData.postValue(Response.Error(
                            "Error code is ${response.code()}\n${response.message()}"))
                    }
                }

                override fun onFailure(call: Call<CreateAnswerResponse>, t: Throwable) {
                    createAnswerData.postValue(Response.Error(if (t.message ==
                        "Failed to connect to /3.110.33.189:80") "No Internet" else t.message!!))
                }
            })
            return createAnswerData
    }

    private val createCommentData = MutableLiveData<Response<String>>()
    fun createComment(createCommentBody: CreateCommentBody): MutableLiveData<Response<String>> {
            ServiceBuilder.getInstance().createComment(createCommentBody).enqueue(object : Callback<CreateCommentResponse> {
                override fun onResponse(
                    call: Call<CreateCommentResponse>,
                    response: retrofit2.Response<CreateCommentResponse>
                ) {
                    when {
                        response.isSuccessful -> createCommentData.postValue(Response.Success("success"))
                        response.code() == 400 -> {
                            Utils().getNewAccessToken()
                            createComment(createCommentBody)
                        }
                        else -> createCommentData.postValue(Response.Error(
                            "Error code is ${response.code()}\n${response.message()}"))
                    }
                }

                override fun onFailure(call: Call<CreateCommentResponse>, t: Throwable) {
                    createCommentData.postValue(Response.Error(if (t.message ==
                        "Failed to connect to /3.110.33.189:80") "No Internet" else t.message!!))
                }
            })
            return createCommentData
    }

    fun searchQnA(search: String) {
        GlobalScope.launch {
            val response = ServiceBuilder.getInstance().searchQnA(search)
            when {
                response.isSuccessful -> {
                    getSearchQnAData.postValue(response.body())
                }
                response.code() == 400 -> {
                    val result = Utils().getNewAccessToken()
//                    if (result == "success") searchQnA(search)
//                    else
//                        errorGetSearchQnAData.postValue("Some error has occurred!\nPlease try again")
                    searchQnA(search)
                }
                else -> {
                    errorGetSearchQnAData.postValue(
                        "${response.message()}\n" +
                                "Please refresh the page"
                    )
                }
            }
        }
    }

    private val wishlistLiveData = MutableLiveData<Response<List<RecentLecturesResponse>>>()
    fun getWishlist(): MutableLiveData<Response<List<RecentLecturesResponse>>> {
        ServiceBuilder.getInstance().getWishlist()
            .enqueue(object : Callback<List<RecentLecturesResponse>> {
                override fun onResponse(
                    call: Call<List<RecentLecturesResponse>>,
                    response: retrofit2.Response<List<RecentLecturesResponse>>
                ) {
                    when {
                        response.isSuccessful ->
                            wishlistLiveData.postValue(Response.Success(response.body()))
                        response.code() == 400 -> {
                            Utils().getNewAccessToken()
                            getWishlist()
                        }
                        else ->
                            wishlistLiveData.postValue(Response.Error("${response.message()}\nPlease refresh the page"))
                    }
                }

                override fun onFailure(call: Call<List<RecentLecturesResponse>>, t: Throwable) {
                    wishlistLiveData.postValue(Response.Error(t.message + "\nPlease try again"))
                }
            })
        return wishlistLiveData
    }
}