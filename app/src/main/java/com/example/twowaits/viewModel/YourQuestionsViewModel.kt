package com.example.twowaits.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.twowaits.network.dashboardApiCalls.QnAResponseItem
import com.example.twowaits.repository.homeRepository.questionsAnswersRepositories.QnARepository
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class YourQuestionsViewModel : ViewModel() {
    lateinit var q_n_aLiveData: LiveData<List<QnAResponseItem>>
    lateinit var errorLiveData: LiveData<String>

    fun getYourQnA() {
        val repository = QnARepository()
        repository.getYourQnA()
        q_n_aLiveData = repository.q_n_aLiveData
        errorLiveData = repository.errorLiveData
    }
}