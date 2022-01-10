package com.example.twowaits.repository.dashboardRepositories

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.apiCalls.dashboardApiCalls.StudentProfileDetailsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileRepository {
    val profileMutableLiveData = MutableLiveData<StudentProfileDetailsResponse>()
    val errorMutableLiveData = MutableLiveData<String>()
    val saveRefreshTokenMutableLiveData = MutableLiveData<String>()

    fun profileDetails(authToken: String) {

        GlobalScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.getInstance().profileDetails(authToken)
            when {
                response.isSuccessful -> {
                    profileMutableLiveData.postValue(response.body())
                }
                response.code() == 400 -> {
                    errorMutableLiveData.postValue("Token has Expired")
                }
                else -> {
                    errorMutableLiveData.postValue("Error code is ${response.code()}\n${response.body()?.detail}")
                }
            }
        }
    }

    fun uploadProfilePic(uri: Uri) {
//        val requestUserId = RequestBody.create(MediaType.parse("multipart/form-data"), userId);
//        val requestImage: MultipartBody.Part? = null;
    }

    fun getNewAccessToken(refreshToken: String){

        GlobalScope.launch (Dispatchers.IO){
            val response = RetrofitClient.getInstance().getNewAccessToken(refreshToken)
            if (response.isSuccessful){
                saveRefreshTokenMutableLiveData.postValue(response.body()?.access)
            } else{
                saveRefreshTokenMutableLiveData.postValue("Error")
            }
        }
    }
}