package com.example.twowaits.repositories.homeRepositories.quizRepositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.utils.Utils
import com.example.twowaits.network.ServiceBuilder
import com.example.twowaits.network.dashboardApiCalls.quizApiCalls.*
import com.example.twowaits.homePages.quiz.AttemptQuizBody
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
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

    val detailedQuizScoreData = MutableLiveData<DetailedQuizResultResponse>()
    val errorDetailedQuizScoreData = MutableLiveData<String>()

    fun getQuizData(attemptQuizBody: AttemptQuizBody) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = ServiceBuilder.getInstance().getQuizData(attemptQuizBody)
            when {
                response.isSuccessful -> {
                    getQuizDataMutableLiveData.postValue(response.body())
                }
                response.code() == 400 -> {
                    val result = Utils().getNewAccessToken()
//                    if (result == "success") getQuizData(attemptQuizBody)
//                    else
//                        errorGetQuizData.postValue("Some error has occurred!\nPlease try again")
                    getQuizData(attemptQuizBody)
                }
                else -> {
                    errorGetQuizData.postValue("Error code is ${response.code()}\n${response.message()}")
                }
            }
        }
    }

    fun attemptQuiz(attemptQuizBody: AttemptQuizBody) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = ServiceBuilder.getInstance().attemptQuiz(attemptQuizBody)
            when {
                response.code() == 208 -> {
                    errorAttemptQuizData.postValue(response.body()?.message)
                }
                response.code() == 201 -> {
                    attemptQuizData.postValue(response.body())
                }
                response.code() == 400 -> {
                    val result = Utils().getNewAccessToken()
//                    if (result == "success") attemptQuiz(attemptQuizBody)
//                    else
//                        errorAttemptQuizData.postValue("Some error has occurred!\nPlease try again")
                    attemptQuiz(attemptQuizBody)
                }
                else -> {
                    errorAttemptQuizData.postValue("Error code is ${response.code()}\n${response.message()}")
                }
            }
        }
    }

    fun registerResponse(registerResponseBody: RegisterResponseBody) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = ServiceBuilder.getInstance().registerResponse(registerResponseBody)
            when {
                response.isSuccessful -> {
                    registerResponseData.postValue(response.body())
                }
                response.code() == 400 -> {
                    val result = Utils().getNewAccessToken()
//                    if (result == "success") registerResponse(registerResponseBody)
//                    else
//                        errorRegisterResponseData.postValue("Some error has occurred!\nPlease try again")
                    registerResponse(registerResponseBody)
                }
                else -> {
                    errorRegisterResponseData.postValue("Error code is ${response.code()}\n${response.message()}")
                }
            }
        }
    }

    fun viewScore(attemptQuizBody: AttemptQuizBody) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = ServiceBuilder.getInstance().viewScore(attemptQuizBody)
            when {
                response.isSuccessful -> {
                    viewScoreData.postValue(response.body())
                }
                response.code() == 400 -> {
                    val result = Utils().getNewAccessToken()
//                    if (result == "success") viewScore(attemptQuizBody)
//                    else
//                        errorViewScoreData.postValue("Some error has occurred!\nPlease try again")
                    viewScore(attemptQuizBody)
                }
                else -> {
                    errorViewScoreData.postValue("${response.message()}/nPlease try again")
                }
            }
        }
    }

    fun detailedQuizResult(id: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = ServiceBuilder.getInstance().detailedQuizResult(id)
            when {
                response.isSuccessful -> detailedQuizScoreData.postValue(response.body())
                response.code() == 400 -> {
                    val result = Utils().getNewAccessToken()
//                    if (result == "success") detailedQuizResult(id)
//                    else errorDetailedQuizScoreData.postValue("Some error has occurred!\nPlease try again")
                    detailedQuizResult(id)
                }
                else -> errorDetailedQuizScoreData.postValue("${response.message()}/nPlease try again")
            }
        }
    }
}