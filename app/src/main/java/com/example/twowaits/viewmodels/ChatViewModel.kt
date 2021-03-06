package com.example.twowaits.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.twowaits.network.dashboardApiCalls.chatApiCalls.FetchConversationsMessagesResponse
import com.example.twowaits.repositories.homeRepositories.chatRepositories.ChatRepository
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class ChatViewModel: ViewModel() {
    lateinit var fetchLiveData: LiveData<List<FetchConversationsMessagesResponse>>
    lateinit var errorLiveData: LiveData<String>

    fun fetchConversationsMessages(){
        val repository = ChatRepository()
        repository.fetchConversationsMessages()
        fetchLiveData = repository.fetchLiveData
        errorLiveData = repository.errorLiveData
    }
}