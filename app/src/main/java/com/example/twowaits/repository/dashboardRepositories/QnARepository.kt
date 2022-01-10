package com.example.twowaits.repository.dashboardRepositories

import androidx.lifecycle.MutableLiveData
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.apiCalls.dashboardApiCalls.QnAResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class QnARepository {
    val q_n_aMutableLiveData = MutableLiveData<List<QnAResponseItem>>()
    val errorMutableLiveData = MutableLiveData<String>()

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