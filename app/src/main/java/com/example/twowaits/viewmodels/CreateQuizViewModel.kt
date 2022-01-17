package com.example.twowaits.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.twowaits.apiCalls.dashboardApiCalls.quizApiCalls.CreateQuizResponse
import com.example.twowaits.repository.dashboardRepositories.quizRepositories.CreateQuizRepository

class CreateQuizViewModel: ViewModel() {
    lateinit var createQuizLiveData: LiveData<CreateQuizResponse>
    lateinit var errorLiveData: LiveData<String>

    fun createQuiz(title: String, description: String, no_of_question: Int, time_limit: Int){
        val repository = CreateQuizRepository()
        repository.createQuiz(title, description, no_of_question, time_limit)
        createQuizLiveData = repository.createQuizLiveData
        errorLiveData = repository.errorLiveData
    }
}