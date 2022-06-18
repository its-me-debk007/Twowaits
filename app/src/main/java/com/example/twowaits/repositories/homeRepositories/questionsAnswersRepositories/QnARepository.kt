package com.example.twowaits.repositories.homeRepositories.questionsAnswersRepositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.utils.Utils
import com.example.twowaits.network.ServiceBuilder
import com.example.twowaits.network.dashboardApiCalls.*
import com.example.twowaits.network.dashboardApiCalls.questionsAnswersApiCalls.AskQuestionResponse
import com.example.twowaits.network.dashboardApiCalls.questionsAnswersApiCalls.BookmarkQuestionResponse
import com.example.twowaits.network.dashboardApiCalls.questionsAnswersApiCalls.LikeAnswerResponse
import com.example.twowaits.homePages.BookmarkNoteBody
import com.example.twowaits.homePages.questionsAnswers.*
import com.example.twowaits.sealedClasses.Response
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
    val createAnswerData = MutableLiveData<String>()
    val createCommentData = MutableLiveData<String>()
    val bookmarkNoteData = MutableLiveData<String>()
    val addToWishlistData = MutableLiveData<String>()
    val getSearchQnAData = MutableLiveData<List<QnAResponseItem>>()
    val errorGetSearchQnAData = MutableLiveData<String>()
    val bookmarkedNotesData = MutableLiveData<List<RecentNotesResponse>>()
    val errorBookmarkedNotesData = MutableLiveData<String>()
    val getWishlistData = MutableLiveData<List<RecentLecturesResponse>>()
    val errorGetWishlistData = MutableLiveData<String>()

    fun getYourQnA() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = ServiceBuilder.getInstance().getYourQnA()
            when {
                response.isSuccessful -> {
                    q_n_aMutableLiveData.postValue(response.body())
                }
                response.code() == 400 -> {
                    val result = Utils().getNewAccessToken()
                    if (result == "success")
                        getYourQnA()
                    else
                        errorMutableLiveData.postValue("Some error has occurred!\nPlease try again")
                }
                else -> {
                    errorMutableLiveData.postValue("${response.message()}\nPlease try again")
                }
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
                        if (result == "success") getQnA()
                        else liveData.postValue(Response.Error("Some error has occurred!\nPlease try again"))
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
                    if (result == "success")
                        askQuestion(askQuestionBody)
                    else
                        errorAskQuestionData.postValue("Some error has occurred!\nPlease try again")
                }
                else -> {
                    errorAskQuestionData.postValue("${response.code()}\nPlease try again")
                }
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
                    if (result == "success")
                        likeAnswer(likeAnswerBody)
                    else
                        errorLikeAnswerData.postValue("Some error has occurred!\nPlease try again")
                }
                else -> {
                    errorLikeAnswerData.postValue("${response.message()}\nPlease refresh the page")
                }
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
                    if (result == "success")
                        bookmarkQuestion(bookmarkQuestionBody)
                    else
                        errorBookmarkQuestionData.postValue("Some error has occurred!\nPlease try again")
                }
                else -> {
                    errorBookmarkQuestionData.postValue("${response.message()}\nPlease refresh the page")
                }
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
                    if (result == "success")
                        bookmarkNote(bookmarkNoteBody)
                    else
                        bookmarkNoteData.postValue("Some error has occurred!\nPlease try again")
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
                    if (result == "success")
                        addToWishlist(addToWishlistBody)
                    else
                        addToWishlistData.postValue("Some error has occurred!\nPlease try again")
                }
                else -> {
                    addToWishlistData.postValue("${response.message()}\nPlease refresh the page")
                }
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
                    val result = Utils().getNewAccessToken()
                    if (result == "success")
                        getYourBookmarkedQ()
                    else
                        errorGetBookmarkedQData.postValue("Some error has occurred!\nPlease try again")
                }
                else -> {
                    errorGetBookmarkedQData.postValue("${response.message()}\nPlease refresh the page")
                }
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
                    if (result == "success")
                        getBookmarkedNotes()
                    else
                        errorBookmarkedNotesData.postValue("Some error has occurred!\nPlease try again")
                }
                else -> {
                    errorBookmarkedNotesData.postValue("${response.message()}\nPlease refresh the page")
                }
            }
        }
    }

    fun createAnswer(createAnswerBody: CreateAnswerBody) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = ServiceBuilder.getInstance().createAnswer(createAnswerBody)
            when {
                response.isSuccessful -> {
                    createAnswerData.postValue("success")
                }
                response.code() == 400 -> {
                    val result = Utils().getNewAccessToken()
                    if (result == "success")
                        createAnswer(createAnswerBody)
                    else
                        createAnswerData.postValue("Some error has occurred!\nPlease try again")
                }
                else -> {
                    createAnswerData.postValue("Error code is ${response.code()}\n${response.message()}")
                }
            }
        }
    }

    fun createComment(createCommentBody: CreateCommentBody) {
        GlobalScope.launch {
            val response = ServiceBuilder.getInstance().createComment(createCommentBody)
            when {
                response.isSuccessful -> {
                    createCommentData.postValue("success")
                }
                response.code() == 400 -> {
                    val result = Utils().getNewAccessToken()
                    if (result == "success")
                        createComment(createCommentBody)
                    else
                        createCommentData.postValue("Some error has occurred!\nPlease try again")
                }
                else -> {
                    createCommentData.postValue("Error code is ${response.code()}\n${response.message()}")
                }
            }
        }
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
                    if (result == "success")
                        searchQnA(search)
                    else
                        errorGetSearchQnAData.postValue("Some error has occurred!\nPlease try again")
                }
                else -> {
                    errorGetSearchQnAData.postValue("${response.message()}\n" +
                            "Please refresh the page")
                }
            }
        }
    }

    fun getWishlist() {
        GlobalScope.launch {
            val response = ServiceBuilder.getInstance().getWishlist()
            when {
                response.isSuccessful -> {
                    getWishlistData.postValue(response.body())
                }
                response.code() == 400 -> {
                    val result = Utils().getNewAccessToken()
                    if (result == "success")
                        getWishlist()
                    else
                        errorGetWishlistData.postValue("Some error has occurred!\nPlease try again")
                }
                else -> {
                    errorGetWishlistData.postValue("${response.message()}\nPlease refresh the page")
                }
            }
        }
    }
}