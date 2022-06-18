package com.example.twowaits.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.twowaits.network.dashboardApiCalls.FacultyProfileDetailsResponse
import com.example.twowaits.network.dashboardApiCalls.StudentProfileDetailsResponse
import com.example.twowaits.homePages.UpdateProfileDetailsBody
import com.example.twowaits.repositories.homeRepositories.ProfileRepository
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class ProfileDetailsViewModel : ViewModel() {

    lateinit var profileFacultyLiveData: LiveData<FacultyProfileDetailsResponse>
    lateinit var errorFacultyLiveData: LiveData<String>

    lateinit var profileStudentLiveData: LiveData<StudentProfileDetailsResponse>
    lateinit var errorStudentLiveData: LiveData<String>

    lateinit var saveRefreshTokenMutableLiveData: LiveData<String>
    lateinit var uploadImageLiveData: LiveData<String>

    lateinit var updateProfileDetailsLiveData: LiveData<StudentProfileDetailsResponse>
    lateinit var errorUpdateProfileDetailsLiveData: LiveData<String>

    lateinit var uriLiveData: LiveData<String>

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

    fun uploadProfilePic(uri: Uri, student_account_id: Int) {
        val repository = ProfileRepository()
        repository.uploadProfilePic(uri, student_account_id)
        uploadImageLiveData = repository.uploadImageLiveData
    }

    fun updateProfileDetails(updateProfileDetailsBody: UpdateProfileDetailsBody) {
        val repository = ProfileRepository()
        repository.updateProfileDetails(updateProfileDetailsBody)
        updateProfileDetailsLiveData = repository.updateProfileDetailsLiveData
        errorUpdateProfileDetailsLiveData = repository.errorUpdateProfileDetailsLiveData
    }
}