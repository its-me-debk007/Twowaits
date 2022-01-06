package com.example.twowaits.repository.dashboardRepositories

import androidx.lifecycle.MutableLiveData
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.apiCalls.dashboardApiCalls.StudentProfileDetailsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileRepository {
    val profileMutableLiveData = MutableLiveData<StudentProfileDetailsResponse>()
    val errorMutableLiveData = MutableLiveData<String>()

    fun profileDetails(authToken: String) {

        GlobalScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.getInstance().profileDetails(authToken)
            if (response.isSuccessful) {
                profileMutableLiveData.postValue(response.body())
            } else {
                errorMutableLiveData.postValue("Error code is ${response.code()}\n${response.body()?.detail}")
            }
        }
    }

    fun uploadProfilePic() {

    }
}