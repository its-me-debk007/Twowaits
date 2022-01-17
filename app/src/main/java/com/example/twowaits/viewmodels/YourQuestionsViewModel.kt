package com.example.twowaits.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.twowaits.apiCalls.dashboardApiCalls.QnAResponseItem
import com.example.twowaits.repository.dashboardRepositories.QnARepository

class YourQuestionsViewModel: ViewModel() {
    val repository = QnARepository()
    lateinit var q_n_aLiveData: LiveData<List<QnAResponseItem>>
    lateinit var errorLiveData: LiveData<String>

    fun getQnA(){
        repository.getQnA()
        q_n_aLiveData = repository.q_n_aLiveData
        errorLiveData = repository.errorLiveData
    }
}