package com.example.twowaits.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twowaits.network.dashboardApiCalls.FacultyProfileDetailsResponse
import com.example.twowaits.network.dashboardApiCalls.StudentProfileDetailsResponse
import com.example.twowaits.model.home.UpdateProfileDetailsBody
import com.example.twowaits.repository.homeRepository.ProfileRepository
import com.example.twowaits.sealedClass.Response
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class ProfileDetailsViewModel : ViewModel() {

    lateinit var profileFacultyLiveData: LiveData<Response<FacultyProfileDetailsResponse>>

    lateinit var profileStudentLiveData: LiveData<StudentProfileDetailsResponse>
    lateinit var errorStudentLiveData: LiveData<String>

    lateinit var uploadImageLiveData: LiveData<String>

    lateinit var updateProfileDetailsLiveData: LiveData<StudentProfileDetailsResponse>
    lateinit var errorUpdateProfileDetailsLiveData: LiveData<String>


    fun profileDetailsFaculty() = viewModelScope.launch{
        profileFacultyLiveData = ProfileRepository().profileDetailsFaculty()
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