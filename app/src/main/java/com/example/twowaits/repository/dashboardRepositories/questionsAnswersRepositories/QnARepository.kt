package com.example.twowaits.repository.dashboardRepositories.questionsAnswersRepositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.apiCalls.dashboardApiCalls.QnAResponseItem
import com.example.twowaits.apiCalls.dashboardApiCalls.questionsAnswersApiCalls.AskQuestionResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.questionsAnswersApiCalls.BookmarkQuestionResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.questionsAnswersApiCalls.LikeAnswerResponse
import com.example.twowaits.homePages.BookmarkNoteBody
import com.example.twowaits.homePages.questionsAnswers.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
    val getSearchQnAData = MutableLiveData<List<QnAResponseItem>>()
    val errorGetSearchQnAData = MutableLiveData<String>()

    fun getYourQnA() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.getInstance().getYourQnA()
            if (response.isSuccessful) {
                q_n_aMutableLiveData.postValue(response.body())
            } else {
                errorMutableLiveData.postValue("Error code is ${response.code()}\n${response.message()}")
            }
        }
    }

    fun getQnA() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.getInstance().getQnA()
            if (response.isSuccessful) {
                getQnAData.postValue(response.body())
            } else {
                errorGetQnAData.postValue("Error code is ${response.code()}\n${response.message()}")
            }
        }
    }

    fun askQuestion(askQuestionBody: AskQuestionBody){
        GlobalScope.launch {
            val response = RetrofitClient.getInstance().askQuestion(askQuestionBody)
            when {
                response.isSuccessful -> {
                    askQuestionData.postValue(response.body())
                }
                response.code() == 400 -> {
                    errorAskQuestionData.postValue(response.body()?.message)
                }
                else -> {
                    errorAskQuestionData.postValue("Error code is ${response.code()}")
                }
            }
        }
    }

    fun likeAnswer(likeAnswerBody: LikeAnswerBody){
        GlobalScope.launch {
            val response = RetrofitClient.getInstance().likeAnswer(likeAnswerBody)
            when {
                response.isSuccessful -> {
                    likeAnswerData.postValue(response.body())
                } else -> {
                    errorLikeAnswerData.postValue("Error code is ${response.code()}")
                }
            }
        }
    }

    fun bookmarkQuestion(bookmarkQuestionBody: BookmarkQuestionBody){
        GlobalScope.launch {
            val response = RetrofitClient.getInstance().bookmarkQuestion(bookmarkQuestionBody)
            when {
                response.isSuccessful -> {
                    bookmarkQuestionData.postValue(response.body())
                } else -> {
                errorBookmarkQuestionData.postValue("Error code is ${response.code()}\n${response.message()}")
            }
            }
        }
    }

    fun bookmarkNote(bookmarkNoteBody: BookmarkNoteBody){
        GlobalScope.launch {
            val response = RetrofitClient.getInstance().bookmarkNote(bookmarkNoteBody)
            when {
                response.isSuccessful -> {
                    bookmarkNoteData.postValue("success")
                } else -> {
                    bookmarkNoteData.postValue(response.message())
                }
            }
        }
    }

    fun getYourBookmarkedQ(){
        GlobalScope.launch {
            val response = RetrofitClient.getInstance().getYourBookmarkedQ()
            when {
                response.isSuccessful -> {
                    getBookmarkedQData.postValue(response.body())
                } else -> {
                    errorGetBookmarkedQData.postValue("Error code is ${response.code()}\n${response.message()}")
            }
            }
        }
    }

    fun createAnswer(createAnswerBody: CreateAnswerBody){
        GlobalScope.launch {
            val response = RetrofitClient.getInstance().createAnswer(createAnswerBody)
            when {
                response.isSuccessful -> {
                    createAnswerData.postValue("success")
                } else -> {
                    createAnswerData.postValue("Error code is ${response.code()}\n${response.message()}")
            }
            }
        }
    }

    fun createComment(createCommentBody: CreateCommentBody){
        GlobalScope.launch {
            val response = RetrofitClient.getInstance().createComment(createCommentBody)
            when {
                response.isSuccessful -> {
                    createCommentData.postValue("success")
                } else -> {
                    createCommentData.postValue("Error code is ${response.code()}\n${response.message()}")
            }
            }
        }
    }

    fun searchQnA(search: String){
        GlobalScope.launch {
            val response = RetrofitClient.getInstance().searchQnA(search)
            when {
                response.isSuccessful -> {
                    getSearchQnAData.postValue(response.body())
                } else -> {
                    errorGetSearchQnAData.postValue("Error code is ${response.code()}\n${response.message()}")
            }
            }
        }
    }
}