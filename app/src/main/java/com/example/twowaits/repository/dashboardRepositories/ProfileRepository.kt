package com.example.twowaits.repository.dashboardRepositories

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.Data
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.apiCalls.dashboardApiCalls.FacultyProfileDetailsResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.StudentProfileDetailsResponse
import com.example.twowaits.homePages.UpdateProfileDetailsBody
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class ProfileRepository {
    private val profileFacultyMutableLiveData = MutableLiveData<FacultyProfileDetailsResponse>()
    val profileFacultyLiveData: LiveData<FacultyProfileDetailsResponse> = profileFacultyMutableLiveData
    private val profileStudentMutableLiveData = MutableLiveData<StudentProfileDetailsResponse>()
    val profileStudentLiveData: LiveData<StudentProfileDetailsResponse> = profileStudentMutableLiveData
    private val errorFacultyMutableLiveData = MutableLiveData<String>()
    val errorFacultyLiveData: LiveData<String> = errorFacultyMutableLiveData
    private val errorStudentMutableLiveData = MutableLiveData<String>()
    val errorStudentLiveData: LiveData<String> = errorStudentMutableLiveData
    private val uploadImageMutableLiveData = MutableLiveData<String>()
    val uploadImageLiveData: LiveData<String> = uploadImageMutableLiveData
    private val updateProfileDetailsData = MutableLiveData<StudentProfileDetailsResponse>()
    val updateProfileDetailsLiveData: LiveData<StudentProfileDetailsResponse> = updateProfileDetailsData
    private val errorUpdateProfileDetailsData = MutableLiveData<String>()
    val errorUpdateProfileDetailsLiveData: LiveData<String> = errorUpdateProfileDetailsData

    fun profileDetailsFaculty() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.getInstance().profileDetailsFaculty()
            when {
                response.isSuccessful -> {
                    profileFacultyMutableLiveData.postValue(response.body())
                }
                response.code() == 400 -> {
                    val result = Data().getNewAccessToken()
                    if (result == "success")
                        profileDetailsFaculty()
                    else
                        errorFacultyMutableLiveData.postValue("Some error has occurred!\nPlease try again")
                }
                else -> {
                    errorFacultyMutableLiveData.postValue("Error code is ${response.code()}\n${response.message()}")
                }
            }
        }
    }

    fun profileDetailsStudent() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.getInstance().profileDetailsStudent()
            when {
                response.isSuccessful -> {
                    profileStudentMutableLiveData.postValue(response.body())
                }
                response.code() == 400 -> {
                    val result = Data().getNewAccessToken()
                    if (result == "success")
                        profileDetailsStudent()
                    else
                        errorStudentMutableLiveData.postValue("Some error has occurred!\nPlease try again")
                }
                else -> {
                    errorStudentMutableLiveData.postValue("Error code is ${response.code()}")
                }
            }
        }
    }

    fun uploadProfilePic(uri: Uri, student_account_id: Int) {
        val imageRef = Firebase.storage.reference.child("${student_account_id}.jpg")
        imageRef.putFile(uri)
            .addOnSuccessListener {
                imageRef.downloadUrl
                    .addOnSuccessListener {
                        uploadImageMutableLiveData.postValue("Uploaded $it")
                    }
                    .addOnFailureListener {
                        uploadImageMutableLiveData.postValue(it.message)
                    }
            }
            .addOnFailureListener {
                uploadImageMutableLiveData.postValue(it.message)
            }
    }

    fun updateProfileDetails(updateProfileDetailsBody: UpdateProfileDetailsBody) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.getInstance().updateProfileDetails(updateProfileDetailsBody)
            when {
                response.isSuccessful -> {
                    updateProfileDetailsData.postValue(response.body())
                }
                response.code() == 400 -> {
                    val result = Data().getNewAccessToken()
                    if (result == "success")
                        updateProfileDetails(updateProfileDetailsBody)
                    else
                        errorUpdateProfileDetailsData.postValue("Some error has occurred!\nPlease try again")
                }
                else -> {
                    errorUpdateProfileDetailsData.postValue("Error code is ${response.code()}")
                }
            }
        }
    }
}