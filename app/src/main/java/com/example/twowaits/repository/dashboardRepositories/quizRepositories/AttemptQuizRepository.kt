package com.example.twowaits.repository.dashboardRepositories.quizRepositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.apiCalls.dashboardApiCalls.quizApiCalls.*
import com.example.twowaits.homePages.quiz.AttemptQuizBody
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class AttemptQuizRepository {
    private val getQuizDataMutableLiveData = MutableLiveData<GetQuizDataResponse>()
    val getQuizLiveData: LiveData<GetQuizDataResponse> = getQuizDataMutableLiveData

    private val errorGetQuizData = MutableLiveData<String>()
    val errorGetQuizLiveData: LiveData<String> = errorGetQuizData

    private val attemptQuizData = MutableLiveData<AttemptQuizResponse>()
    val attemptQuizLiveData: LiveData<AttemptQuizResponse> = attemptQuizData

    private val errorAttemptQuizData = MutableLiveData<String>()
    val errorAttemptQuizLiveData: LiveData<String> = errorAttemptQuizData

    private val registerResponseData = MutableLiveData<RegisterOptionsResponse>()
    val registerResponseLiveData: LiveData<RegisterOptionsResponse> = registerResponseData

    private val errorRegisterResponseData = MutableLiveData<String>()
    val errorRegisterResponseLiveData: LiveData<String> = errorRegisterResponseData

    private val viewScoreData = MutableLiveData<ViewScoreResponse>()
    val viewScoreLiveData: LiveData<ViewScoreResponse> = viewScoreData

    private val errorViewScoreData = MutableLiveData<String>()
    val errorViewScoreLiveData: LiveData<String> = errorViewScoreData

    fun getQuizData(attemptQuizBody: AttemptQuizBody){
        GlobalScope.launch {
            val response = RetrofitClient.getInstance().getQuizData(attemptQuizBody)
            when {
                response.isSuccessful -> {
                    getQuizDataMutableLiveData.postValue(response.body())
                }
                else -> {
                    errorGetQuizData.postValue("Error code is ${response.code()}\n${response.message()}")
                }
            }
        }
    }

    fun attemptQuiz(attemptQuizBody: AttemptQuizBody){
        GlobalScope.launch {
            val response = RetrofitClient.getInstance().attemptQuiz(attemptQuizBody)
            when {
                response.code() == 208 -> {
                    errorAttemptQuizData.postValue(response.body()?.message)
                }
                response.code() == 201 -> {
                    attemptQuizData.postValue(response.body())
                }
                else -> {
                    errorAttemptQuizData.postValue("Error code is ${response.code()}\n${response.message()}")
                }
            }
        }
    }

    fun registerResponse(registerResponseBody: RegisterResponseBody){
        GlobalScope.launch {
            val response = RetrofitClient.getInstance().registerResponse(registerResponseBody)
            when {
                response.isSuccessful -> {
                    registerResponseData.postValue(response.body())
                }
                else -> {
                    errorRegisterResponseData.postValue("Error code is ${response.code()}\n${response.message()}")
                }
            }
        }
    }

    fun viewScore(attemptQuizBody: AttemptQuizBody){
        GlobalScope.launch {
            val response = RetrofitClient.getInstance().viewScore(attemptQuizBody)
            when {
                response.isSuccessful -> {
                    viewScoreData.postValue(response.body())
                }
                else -> {
                    errorViewScoreData.postValue("Error code is ${response.code()}\n${response.message()}")
                }
            }
        }
    }
}