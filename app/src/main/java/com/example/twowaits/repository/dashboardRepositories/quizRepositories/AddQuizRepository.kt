package com.example.twowaits.repository.dashboardRepositories.quizRepositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.apiCalls.dashboardApiCalls.quizApiCalls.AddCorrectOptionResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.quizApiCalls.AddQuestionsResponse
import com.example.twowaits.homePages.quiz.AddCorrectOptionBody
import com.example.twowaits.homePages.quiz.CreateQuestionBody
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class AddQuizRepository {
    private val addQuestionsMutableLiveData = MutableLiveData<AddQuestionsResponse>()
    val addQuestionsLiveData: LiveData<AddQuestionsResponse> = addQuestionsMutableLiveData

    private val errorAddQuestionsMutableLiveData = MutableLiveData<String>()
    val errorAddQuestionsLiveData: LiveData<String> = errorAddQuestionsMutableLiveData

    private val addCorrectOptionMutableLiveData = MutableLiveData<AddCorrectOptionResponse>()
    val addCorrectOptionLiveData: LiveData<AddCorrectOptionResponse> = addCorrectOptionMutableLiveData

    private val errorAddCorrectOptionMutableLiveData = MutableLiveData<String>()
    val errorAddCorrectOptionLiveData: LiveData<String> = errorAddCorrectOptionMutableLiveData

    fun createQuestion(createQuestionBody: CreateQuestionBody) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.getInstance().createQuestion(createQuestionBody)
            when {
                response.isSuccessful -> {
                    addQuestionsMutableLiveData.postValue(response.body())
                }
                response.code() == 400 -> {
                    errorAddQuestionsMutableLiveData.postValue("Token has Expired")
                }
                else -> {
                    errorAddQuestionsMutableLiveData.postValue("Error code is ${response.code()}")
                }
            }
        }
    }

    fun addCorrectOption(addCorrectOptionBody: AddCorrectOptionBody){
        GlobalScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.getInstance().addCorrectOption(addCorrectOptionBody)
            when {
                response.isSuccessful -> {
                    addCorrectOptionMutableLiveData.postValue(response.body())
                }
                response.code() == 400 -> {
                    errorAddCorrectOptionMutableLiveData.postValue("Token has Expired")
                }
                else -> {
                    errorAddCorrectOptionMutableLiveData.postValue("Error code is ${response.code()}\n${response.message()}")
                }
            }
        }
    }
}