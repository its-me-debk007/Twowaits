package com.example.twowaits.repository.homeRepository.quizRepositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.model.AttemptQuizBody
import com.example.twowaits.network.ServiceBuilder
import com.example.twowaits.network.dashboardApiCalls.quizApiCalls.*
import com.example.twowaits.sealedClass.Response
import com.example.twowaits.util.Utils
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback

@DelicateCoroutinesApi
class AttemptQuizRepository {

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

    private val quizLiveData = MutableLiveData<Response<GetQuizDataResponse>>()
    fun getQuizData(attemptQuizBody: AttemptQuizBody): MutableLiveData<Response<GetQuizDataResponse>> {
        ServiceBuilder.getInstance().getQuizData(attemptQuizBody)
            .enqueue(object : Callback<GetQuizDataResponse> {
                override fun onResponse(
                    call: Call<GetQuizDataResponse>,
                    response: retrofit2.Response<GetQuizDataResponse>
                ) {
                    when {
                        response.isSuccessful -> quizLiveData.postValue(Response.Success(response.body()))

                        response.code() == 400 -> {
                            Utils().getNewAccessToken()
                            getQuizData(attemptQuizBody)
                        }

                        else -> quizLiveData.postValue(
                            Response.Error(
                                "${response.message()}! Please try again"
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<GetQuizDataResponse>, t: Throwable) {
                    quizLiveData.postValue(Response.Error("${t.message}! Please try again"))
                }

            })
        return quizLiveData
    }

    private val attemptQuizLiveData = MutableLiveData<Response<AttemptQuizResponse>>()
    fun attemptQuiz(attemptQuizBody: AttemptQuizBody): MutableLiveData<Response<AttemptQuizResponse>> {
        ServiceBuilder.getInstance().attemptQuiz(attemptQuizBody)
            .enqueue(object : Callback<AttemptQuizResponse> {
                override fun onResponse(
                    call: Call<AttemptQuizResponse>,
                    response: retrofit2.Response<AttemptQuizResponse>
                ) {
                    when {
                        response.code() == 208 ->
                            attemptQuizLiveData.postValue(Response.Error(response.body()?.message!!))

                        response.code() == 201 -> attemptQuizLiveData.postValue(
                            Response.Success(
                                response.body()
                            )
                        )

                        response.code() == 400 -> {
                            Utils().getNewAccessToken()
                            attemptQuiz(attemptQuizBody)
                        }
                        else ->
                            attemptQuizLiveData.postValue(Response.Error("${response.message()}! Please try again"))
                    }
                }

                override fun onFailure(call: Call<AttemptQuizResponse>, t: Throwable) {
                    attemptQuizLiveData.postValue(Response.Error("${t.message}! Please try again"))
                }

            })
        return attemptQuizLiveData
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