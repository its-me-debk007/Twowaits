package com.example.twowaits.repository.dashboardRepositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.apiCalls.dashboardApiCalls.QnAResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class QnARepository {
    private val q_n_aMutableLiveData = MutableLiveData<List<QnAResponseItem>>()
    val q_n_aLiveData: LiveData<List<QnAResponseItem>> = q_n_aMutableLiveData
    private val errorMutableLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = errorMutableLiveData

    fun getQnA() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.getInstance().getQnA()
            if (response.isSuccessful) {
                q_n_aMutableLiveData.postValue(response.body())
            } else {
                errorMutableLiveData.postValue("Error code is ${response.code()}")
            }
        }
    }
}