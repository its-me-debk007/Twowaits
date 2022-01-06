package com.example.twowaits.viewmodels.dashboardViewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.twowaits.apiCalls.dashboardApiCalls.StudentProfileDetailsResponse
import com.example.twowaits.repository.dashboardRepositories.ProfileRepository

class ProfileDetailsViewModel: ViewModel() {

    val repository = ProfileRepository()
    var profileMutableLiveData = MutableLiveData<StudentProfileDetailsResponse>()
    var errorMutableLiveData = MutableLiveData<String>()

    fun profileDetails(authToken: String) {
        repository.profileDetails(authToken)
        profileMutableLiveData = repository.profileMutableLiveData
        errorMutableLiveData = repository.errorMutableLiveData
    }

    fun uploadProfilePic() {
        repository.uploadProfilePic()
    }
}