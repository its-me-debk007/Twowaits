package com.example.twowaits.viewmodels.quizViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.twowaits.network.dashboardApiCalls.quizApiCalls.CreateQuizResponse
import com.example.twowaits.homePages.quiz.CreateQuizBody
import com.example.twowaits.repositories.homeRepositories.quizRepositories.CreateQuizRepository

class CreateQuizViewModel: ViewModel() {
    lateinit var createQuizLiveData: LiveData<CreateQuizResponse>
    lateinit var errorLiveData: LiveData<String>

    fun createQuiz(createQuizBody: CreateQuizBody){
        val repository = CreateQuizRepository()
        repository.createQuiz(createQuizBody)
        createQuizLiveData = repository.createQuizLiveData
        errorLiveData = repository.errorLiveData
    }
}