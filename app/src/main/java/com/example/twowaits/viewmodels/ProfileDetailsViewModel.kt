package com.example.twowaits.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.twowaits.apiCalls.dashboardApiCalls.FacultyProfileDetailsResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.StudentProfileDetailsResponse
import com.example.twowaits.repository.dashboardRepositories.ProfileRepository
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class ProfileDetailsViewModel : ViewModel() {

    lateinit var profileFacultyLiveData: LiveData<FacultyProfileDetailsResponse>
    lateinit var errorFacultyLiveData: LiveData<String>

    lateinit var profileStudentLiveData: LiveData<StudentProfileDetailsResponse>
    lateinit var errorStudentLiveData: LiveData<String>

    lateinit var saveRefreshTokenMutableLiveData: LiveData<String>
    lateinit var uploadImageLiveData: LiveData<String>

    fun profileDetailsFaculty() {
        val repository = ProfileRepository()
        repository.profileDetailsFaculty()
        profileFacultyLiveData = repository.profileFacultyLiveData
        errorFacultyLiveData = repository.errorFacultyLiveData
    }

    fun profileDetailsStudent() {
        val repository = ProfileRepository()
        repository.profileDetailsStudent()
        profileStudentLiveData = repository.profileStudentLiveData
        errorStudentLiveData = repository.errorStudentLiveData
    }

    fun uploadProfilePic(uri: Uri) {
        val repository = ProfileRepository()
        repository.uploadProfilePic(uri)
        uploadImageLiveData = repository.uploadImageLiveData
    }

    fun getNewAccessToken(refreshToken: String) {
        val repository = ProfileRepository()
        repository.getNewAccessToken(refreshToken)
        saveRefreshTokenMutableLiveData = repository.saveRefreshTokenLiveData
    }
}