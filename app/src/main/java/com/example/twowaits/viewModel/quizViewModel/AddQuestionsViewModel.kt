package com.example.twowaits.viewModel.quizViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.twowaits.network.dashboardApiCalls.quizApiCalls.AddCorrectOptionResponse
import com.example.twowaits.network.dashboardApiCalls.quizApiCalls.AddQuestionsResponse
import com.example.twowaits.homePages.quiz.AddCorrectOptionBody
import com.example.twowaits.homePages.quiz.CreateQuestionBody
import com.example.twowaits.repository.homeRepository.quizRepositories.AddQuizRepository
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class AddQuestionsViewModel: ViewModel() {
    var optionCount = 0
    lateinit var addQuestionsLiveData: LiveData<AddQuestionsResponse>
    lateinit var errorAddQuestionsLiveData: LiveData<String>
    lateinit var addCorrectOptionLiveData: LiveData<AddCorrectOptionResponse>
    lateinit var errorAddCorrectOptionLiveData: LiveData<String>

    fun createQuestion(createQuestionBody: CreateQuestionBody) {
        val repository = AddQuizRepository()
        repository.createQuestion(createQuestionBody)
        addQuestionsLiveData = repository.addQuestionsLiveData
        errorAddQuestionsLiveData = repository.errorAddQuestionsLiveData
    }

    fun addCorrectOption(addCorrectOptionBody: AddCorrectOptionBody){
        val repository = AddQuizRepository()
        repository.addCorrectOption(addCorrectOptionBody)
        addCorrectOptionLiveData = repository.addCorrectOptionLiveData
        errorAddCorrectOptionLiveData = repository.errorAddCorrectOptionLiveData
    }
}