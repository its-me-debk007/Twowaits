package com.example.twowaits.repository.dashboardRepositories.quizRepositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.apiCalls.dashboardApiCalls.quizApiCalls.CreateQuizResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CreateQuizRepository {
    private val createQuizMutableLiveData = MutableLiveData<CreateQuizResponse>()
    val createQuizLiveData: LiveData<CreateQuizResponse> = createQuizMutableLiveData
    private val errorMutableLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = errorMutableLiveData

    fun createQuiz(title: String, description: String, no_of_question: Int, time_limit: Int ){
        GlobalScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.getInstance().createQuiz(title, description, no_of_question, time_limit)
            when {
                response.isSuccessful -> {
                    createQuizMutableLiveData.postValue(response.body())
                }
                response.code() == 400 -> {
                    errorMutableLiveData.postValue("Token has Expired")
                }
                else -> {
                    errorMutableLiveData.postValue("Error code is ${response.code()}\n${response.body()?.detail}")
                }
            }
        }
    }
}