package com.example.twowaits.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.twowaits.apiCalls.dashboardApiCalls.FacultyProfileDetailsResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.QnAResponseItem
import com.example.twowaits.apiCalls.dashboardApiCalls.StudentProfileDetailsResponse
import com.example.twowaits.repository.dashboardRepositories.ProfileRepository
import com.example.twowaits.repository.dashboardRepositories.QnARepository

class ProfileDetailsViewModel: ViewModel() {

    lateinit var profileLiveData: LiveData<FacultyProfileDetailsResponse>
    lateinit var errorLiveData: LiveData<String>
    lateinit var saveRefreshTokenMutableLiveData: LiveData<String>
    lateinit var uploadImageLiveData: LiveData<String>

    fun profileDetails(authToken: String) {
        val repository = ProfileRepository()
        repository.profileDetails(authToken)
        profileLiveData = repository.profileLiveData
        errorLiveData = repository.errorLiveData
    }

    fun uploadProfilePic(uri: Uri) {
        val repository = ProfileRepository()
        repository.uploadProfilePic(uri)
        uploadImageLiveData = repository.uploadImageLiveData
    }

    fun getNewAccessToken(refreshToken: String){
        val repository = ProfileRepository()
        repository.getNewAccessToken(refreshToken)
        saveRefreshTokenMutableLiveData = repository.saveRefreshTokenLiveData
    }
}