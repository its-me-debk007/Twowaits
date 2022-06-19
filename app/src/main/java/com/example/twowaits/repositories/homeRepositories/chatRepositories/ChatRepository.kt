package com.example.twowaits.repositories.homeRepositories.chatRepositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.utils.Utils
import com.example.twowaits.network.ServiceBuilder
import com.example.twowaits.network.dashboardApiCalls.chatApiCalls.FetchConversationsMessagesResponse
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
            val response = ServiceBuilder.getInstance().fetchConversationsMessages()
            when {
                response.isSuccessful -> {
                    fetchData.postValue(response.body())
                }
                response.code() == 400 -> {
                    val result = Utils().getNewAccessToken()
//                    if (result == "success") fetchConversationsMessages()
//                    else errorData.postValue("Some error has occurred!\nPlease try again")
                    fetchConversationsMessages()
                }
                else -> {
                    errorData.postValue("${response.message()}\nPlease try again")
                }
            }
        }
    }
}