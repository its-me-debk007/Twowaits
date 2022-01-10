package com.example.twowaits.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.twowaits.apiCalls.dashboardApiCalls.QnAResponseItem
import com.example.twowaits.apiCalls.dashboardApiCalls.StudentProfileDetailsResponse
import com.example.twowaits.repository.dashboardRepositories.ProfileRepository
import com.example.twowaits.repository.dashboardRepositories.QnARepository

class ProfileDetailsViewModel: ViewModel() {

    val repository = ProfileRepository()
    var profileMutableLiveData = MutableLiveData<StudentProfileDetailsResponse>()
    var errorMutableLiveData = MutableLiveData<String>()
    var q_n_aMutableLiveData = MutableLiveData<List<QnAResponseItem>>()
    var saveRefreshTokenMutableLiveData = MutableLiveData<String>()

    fun profileDetails(authToken: String) {
        repository.profileDetails(authToken)
        profileMutableLiveData = repository.profileMutableLiveData
        errorMutableLiveData = repository.errorMutableLiveData
    }

    fun uploadProfilePic() {
//        repository.uploadProfilePic()
    }

    fun getQnA(){
        val q_n_a_repository = QnARepository()
        q_n_a_repository.getQnA()
        q_n_aMutableLiveData = q_n_a_repository.q_n_aMutableLiveData
        }

    fun getNewAccessToken(refreshToken: String){
        repository.getNewAccessToken(refreshToken)
        saveRefreshTokenMutableLiveData = repository.saveRefreshTokenMutableLiveData
    }
}