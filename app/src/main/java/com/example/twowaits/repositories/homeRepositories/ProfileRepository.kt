package com.example.twowaits.repositories.homeRepositories

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.utils.Utils
import com.example.twowaits.network.ServiceBuilder
import com.example.twowaits.network.dashboardApiCalls.FacultyProfileDetailsResponse
import com.example.twowaits.network.dashboardApiCalls.StudentProfileDetailsResponse
import com.example.twowaits.models.home.UpdateProfileDetailsBody
import com.example.twowaits.network.dashboardApiCalls.RecentQuizzesResponse
import com.example.twowaits.sealedClasses.Response
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback

@DelicateCoroutinesApi
class ProfileRepository {
    private val profileStudentMutableLiveData = MutableLiveData<StudentProfileDetailsResponse>()
    val profileStudentLiveData: LiveData<StudentProfileDetailsResponse> =
        profileStudentMutableLiveData
    private val errorStudentMutableLiveData = MutableLiveData<String>()
    val errorStudentLiveData: LiveData<String> = errorStudentMutableLiveData
    private val uploadImageMutableLiveData = MutableLiveData<String>()
    val uploadImageLiveData: LiveData<String> = uploadImageMutableLiveData
    private val updateProfileDetailsData = MutableLiveData<StudentProfileDetailsResponse>()
    val updateProfileDetailsLiveData: LiveData<StudentProfileDetailsResponse> =
        updateProfileDetailsData
    private val errorUpdateProfileDetailsData = MutableLiveData<String>()
    val errorUpdateProfileDetailsLiveData: LiveData<String> = errorUpdateProfileDetailsData

    fun profileDetailsFaculty(): MutableLiveData<Response<FacultyProfileDetailsResponse>> {
        val liveData = MutableLiveData<Response<FacultyProfileDetailsResponse>>()
        val call = ServiceBuilder.getInstance().profileDetailsFaculty()

        call.enqueue(object : Callback<FacultyProfileDetailsResponse> {
            override fun onResponse(
                call: Call<FacultyProfileDetailsResponse>,
                response: retrofit2.Response<FacultyProfileDetailsResponse>
            ) {
                when {
                    response.isSuccessful ->
                        liveData.postValue(Response.Success(response.body()))

                    response.code() == 400 -> {
                        val result = Utils().getNewAccessToken()
//                        if (result == "success") profileDetailsFaculty()
//                        else liveData.postValue(Response.Error("Some error has occurred!\nPlease try again"))
                        profileDetailsFaculty()
                    }

                    else -> liveData.postValue(Response.Error(response.message() + "\nPlease try again"))
                }
            }

            override fun onFailure(call: Call<FacultyProfileDetailsResponse>, t: Throwable) {
                liveData.postValue(Response.Error(t.message + "\nPlease try again"))
            }
        })
        return liveData
    }

    fun profileDetailsStudent() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = ServiceBuilder.getInstance().profileDetailsStudent()
            when {
                response.isSuccessful -> {
                    profileStudentMutableLiveData.postValue(response.body())
                }
                response.code() == 400 -> {
                    val result = Utils().getNewAccessToken()
//                    if (result == "success") profileDetailsStudent()
//                    else
//                        errorStudentMutableLiveData.postValue("Some error has occurred!\nPlease try again")
                    profileDetailsStudent()
                }
                else -> errorStudentMutableLiveData.postValue("Error code is ${response.code()}")
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
            val response =
                ServiceBuilder.getInstance().updateProfileDetails(updateProfileDetailsBody)
            when {
                response.isSuccessful -> {
                    updateProfileDetailsData.postValue(response.body())
                }
                response.code() == 400 -> {
                    val result = Utils().getNewAccessToken()
//                    if (result == "success") updateProfileDetails(updateProfileDetailsBody)
//                    else
//                        errorUpdateProfileDetailsData.postValue("Some error has occurred!\nPlease try again")
                    updateProfileDetails(updateProfileDetailsBody)
                }
                else -> {
                    errorUpdateProfileDetailsData.postValue("Error code is ${response.code()}")
                }
            }
        }
    }
}