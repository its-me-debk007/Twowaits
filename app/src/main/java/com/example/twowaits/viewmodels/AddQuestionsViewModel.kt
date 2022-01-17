package com.example.twowaits.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.twowaits.apiCalls.dashboardApiCalls.quizApiCalls.AddQuestionsResponse
import com.example.twowaits.homePages.quiz.OptionsForRetrofit
import com.example.twowaits.repository.dashboardRepositories.quizRepositories.AddQuizRepository

class AddQuestionsViewModel: ViewModel() {
    var optionCount = 0
    lateinit var addQuestionsLiveData: LiveData<AddQuestionsResponse>
    lateinit var errorLiveData: LiveData<String>

    fun createQuestion(authToken: String, quiz_id: Int, question_text: String, options: List<OptionsForRetrofit>) {
        val repository = AddQuizRepository()
        repository.createQuestion(authToken, quiz_id, question_text, options)
        addQuestionsLiveData = repository.addQuestionsLiveData
        errorLiveData = repository.errorLiveData
    }

}