package com.example.twowaits.repository.dashboardRepositories.quizRepositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.apiCalls.dashboardApiCalls.quizApiCalls.CreateQuizResponse
import com.example.twowaits.homePages.quiz.CreateQuizBody
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateQuizRepository {
    private val createQuizMutableLiveData = MutableLiveData<CreateQuizResponse>()
    val createQuizLiveData: LiveData<CreateQuizResponse> = createQuizMutableLiveData
    private val errorMutableLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = errorMutableLiveData

    fun createQuiz(createQuizBody: CreateQuizBody){
//        GlobalScope.launch(Dispatchers.IO) {
//            val response = RetrofitClient.getInstance().createQuiz(title, description, no_of_question, time_limit)
//            when {
//                response.isSuccessful -> {
//                    createQuizMutableLiveData.postValue(response.body())
//                }
////                response.code() == 400 -> {
////                    errorMutableLiveData.postValue("Token has Expired")
////                }
//                else -> {
//                    errorMutableLiveData.postValue("Error code is ${response.code()}\n${response.body()?.detail}")
//                }
//            }
//        }
        RetrofitClient.getInstance().createQuiz(createQuizBody).enqueue(object : Callback<CreateQuizResponse>{
            override fun onResponse(
                call: Call<CreateQuizResponse>,
                response: Response<CreateQuizResponse>
            ) {
                when {
                    response.isSuccessful -> {
                        createQuizMutableLiveData.postValue(response.body())
                    }
                    response.code() == 400 -> {
                        errorMutableLiveData.postValue("Token has Expired")
                    }
                    else -> {
                        errorMutableLiveData.postValue(response.code().toString() + response.message())
                    }
                }
            }

            override fun onFailure(call: Call<CreateQuizResponse>, t: Throwable) {
                errorMutableLiveData.postValue(t.message)
            }
        })
    }
}