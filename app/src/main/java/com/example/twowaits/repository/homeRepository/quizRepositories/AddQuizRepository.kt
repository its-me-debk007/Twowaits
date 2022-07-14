package com.example.twowaits.repository.homeRepository.quizRepositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.utils.Utils
import com.example.twowaits.network.ServiceBuilder
import com.example.twowaits.network.dashboardApiCalls.quizApiCalls.AddCorrectOptionResponse
import com.example.twowaits.network.dashboardApiCalls.quizApiCalls.AddQuestionsResponse
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
            val response = ServiceBuilder.getInstance().createQuestion(createQuestionBody)
            when {
                response.isSuccessful -> {
                    addQuestionsMutableLiveData.postValue(response.body())
                }
                response.code() == 400 -> {
                    val result = Utils().getNewAccessToken()
//                    if (result == "success") createQuestion(createQuestionBody)
//                    else
//                        errorAddQuestionsMutableLiveData.postValue("Some error has occurred!\nPlease try again")
                    createQuestion(createQuestionBody)
                }
                else ->
                    errorAddQuestionsMutableLiveData.postValue("Error code is ${response.code()}")
            }
        }
    }

    fun addCorrectOption(addCorrectOptionBody: AddCorrectOptionBody){
        GlobalScope.launch(Dispatchers.IO) {
            val response = ServiceBuilder.getInstance().addCorrectOption(addCorrectOptionBody)
            when {
                response.isSuccessful -> {
                    addCorrectOptionMutableLiveData.postValue(response.body())
                }
                response.code() == 400 -> {
                    val result = Utils().getNewAccessToken()
//                    if (result == "success") addCorrectOption(addCorrectOptionBody)
//                    else
//                        errorAddCorrectOptionMutableLiveData.postValue("Some error has occurred!\nPlease try again")
                    addCorrectOption(addCorrectOptionBody)
                }
                else ->
                    errorAddCorrectOptionMutableLiveData.postValue("Error code is ${response.code()}\n${response.message()}")
            }
        }
    }
}