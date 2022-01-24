package com.example.twowaits.repository.dashboardRepositories.chatRepositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.apiCalls.dashboardApiCalls.chatApiCalls.FetchConversationsMessagesResponse
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class ChatRepository {
    private val fetchData = MutableLiveData<List<FetchConversationsMessagesResponse>>()
    val fetchLiveData: LiveData<List<FetchConversationsMessagesResponse>> = fetchData
    private val errorData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = errorData

    fun fetchConversationsMessages(){
        GlobalScope.launch {
            val response = RetrofitClient.getInstance().fetchConversationsMessages()
            when {
                response.isSuccessful -> {
                    fetchData.postValue(response.body())
                } else -> {
                    errorData.postValue("Error code is ${response.code()}\n${response.message()}")
                }
            }
        }
    }
}