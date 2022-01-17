package com.example.twowaits.repository.dashboardRepositories.quizRepositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.apiCalls.dashboardApiCalls.quizApiCalls.AddQuestionsResponse
import com.example.twowaits.homePages.quiz.OptionsForRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddQuizRepository {
    private val addQuestionsMutableLiveData = MutableLiveData<AddQuestionsResponse>()
    val addQuestionsLiveData: LiveData<AddQuestionsResponse> = addQuestionsMutableLiveData
    private val errorMutableLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = errorMutableLiveData

    fun createQuestion(authToken: String, quiz_id: Int, question_text: String, options: List<OptionsForRetrofit>) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.getInstance().createQuestion(authToken, quiz_id, question_text, options)
            when {
                response.isSuccessful -> {
                    addQuestionsMutableLiveData.postValue(response.body())
                }
                response.code() == 400 -> {
                    errorMutableLiveData.postValue("Token has Expired")
                }
                else -> {
                    errorMutableLiveData.postValue("Error code is ${response.code()}")
                }
            }
        }
    }
}