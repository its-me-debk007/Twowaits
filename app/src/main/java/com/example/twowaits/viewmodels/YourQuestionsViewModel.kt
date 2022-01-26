package com.example.twowaits.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.twowaits.apiCalls.dashboardApiCalls.QnAResponseItem
import com.example.twowaits.repository.dashboardRepositories.questionsAnswersRepositories.QnARepository
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class YourQuestionsViewModel : ViewModel() {
    lateinit var q_n_aLiveData: LiveData<List<QnAResponseItem>>
    lateinit var errorLiveData: LiveData<String>
    var isClicked = false

    fun getYourQnA() {
        val repository = QnARepository()
        repository.getYourQnA()
        q_n_aLiveData = repository.q_n_aLiveData
        errorLiveData = repository.errorLiveData
    }
}