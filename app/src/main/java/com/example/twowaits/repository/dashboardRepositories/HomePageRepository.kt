package com.example.twowaits.repository.dashboardRepositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.apiCalls.dashboardApiCalls.RecentNotesResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.RecentQuizzesResponse
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class HomePageRepository {
    private val recentQuizzesData = MutableLiveData<List<RecentQuizzesResponse>>()
    val recentQuizzesLiveData: LiveData<List<RecentQuizzesResponse>> = recentQuizzesData

    private val errorRecentQuizzesData = MutableLiveData<String>()
    val errorRecentQuizzesLiveData: LiveData<String> = errorRecentQuizzesData

    private val recentNotesData = MutableLiveData<List<RecentNotesResponse>>()
    val recentNotesLiveData: LiveData<List<RecentNotesResponse>> = recentNotesData

    private val errorRecentNotesData = MutableLiveData<String>()
    val errorRecentNotesLiveData: LiveData<String> = errorRecentNotesData

    fun recentQuizzes(){
        GlobalScope.launch {
            val response = RetrofitClient.getInstance().recentQuizzes()
            when {
                response.isSuccessful -> {
                    recentQuizzesData.postValue(response.body())
                } else -> {
                    errorRecentQuizzesData.postValue("Error code is ${response.code()}\n${response.message()}")
            }
            }
        }
    }

    fun recentNotes(){
        GlobalScope.launch {
            val response = RetrofitClient.getInstance().recentNotes()
            when {
                response.isSuccessful -> {
                    recentNotesData.postValue(response.body())
                } else -> {
                    errorRecentNotesData.postValue("Error code is ${response.code()}\n${response.message()}")
            }
            }
        }
    }
}