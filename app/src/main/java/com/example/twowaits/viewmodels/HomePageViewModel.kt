package com.example.twowaits.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.twowaits.apiCalls.dashboardApiCalls.QnAResponseItem
import com.example.twowaits.apiCalls.dashboardApiCalls.RecentNotesResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.RecentQuizzesResponse
import com.example.twowaits.repository.dashboardRepositories.HomePageRepository
import com.example.twowaits.repository.dashboardRepositories.questionsAnswersRepositories.QnARepository
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class HomePageViewModel: ViewModel() {
    lateinit var getQnALiveData: LiveData<List<QnAResponseItem>>
    lateinit var errorGetQnALiveData: LiveData<String>

    lateinit var recentQuizzesLiveData: LiveData<List<RecentQuizzesResponse>>
    lateinit var errorRecentQuizzesLiveData: LiveData<String>

    lateinit var recentNotesLiveData: LiveData<List<RecentNotesResponse>>
    lateinit var errorRecentNotesLiveData: LiveData<String>

    fun getQnA() {
        val repository = QnARepository()
        repository.getQnA()
        getQnALiveData = repository.getQnALiveData
        errorGetQnALiveData = repository.errorGetQnALiveData
    }

    fun recentQuizzes(){
        val repository = HomePageRepository()
        repository.recentQuizzes()
        recentQuizzesLiveData = repository.recentQuizzesLiveData
        errorRecentQuizzesLiveData = repository.errorRecentQuizzesLiveData
    }

    fun recentNotes(){
        val repository = HomePageRepository()
        repository.recentNotes()
        recentNotesLiveData = repository.recentNotesLiveData
        errorRecentNotesLiveData = repository.errorRecentNotesLiveData
    }
}