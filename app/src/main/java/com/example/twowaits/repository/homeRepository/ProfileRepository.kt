package com.example.twowaits.repository.homeRepository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.model.home.UpdateProfileDetailsBody
import com.example.twowaits.network.ServiceBuilder
import com.example.twowaits.network.dashboardApiCalls.FacultyProfileDetailsResponse
import com.example.twowaits.network.dashboardApiCalls.StudentProfileDetailsResponse
import com.example.twowaits.sealedClass.Response
import com.example.twowaits.utils.Utils
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback

class ProfileRepository {
    private val profileStudentMutableLiveData = MutableLiveData<StudentProfileDetailsResponse>()
    val profileStudentLiveData: LiveData<StudentProfileDetailsResponse> =
        profileStudentMutableLiveData
    private val errorStudentMutableLiveData = MutableLiveData<String>()
    val errorStudentLiveData: LiveData<String> = errorStudentMutableLiveData
    private val uploadImageMutableLiveData = MutableLiveData<String>()
    val uploadImageLiveData: LiveData<String> = uploadImageMutableLiveData

    val profileDetailsFacultyLiveData = MutableLiveData<Response<FacultyProfileDetailsResponse>>()

    fun profileDetailsFaculty(): MutableLiveData<Response<FacultyProfileDetailsResponse>> {
        ServiceBuilder.getInstance().profileDetailsFaculty()
            .enqueue(object : Callback<FacultyProfileDetailsResponse> {
                override fun onResponse(
                    call: Call<FacultyProfileDetailsResponse>,
                    response: retrofit2.Response<FacultyProfileDetailsResponse>
                ) {
                    when {
                        response.isSuccessful ->
                            profileDetailsFacultyLiveData.postValue(Response.Success(response.body()))

                        response.code() == 400 -> {
                            Utils().getNewAccessToken()
                            profileDetailsFaculty()
                        }

                        else -> profileDetailsFacultyLiveData.postValue(Response.Error(response.message() + "\nPlease try again"))
                    }
                }

                override fun onFailure(call: Call<FacultyProfileDetailsResponse>, t: Throwable) {
                    profileDetailsFacultyLiveData.postValue(Response.Error(t.message + "\nPlease try again"))
                }
            })
        return profileDetailsFacultyLiveData
    }

    fun profileDetailsStudent() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = ServiceBuilder.getInstance().profileDetailsStudent()
            when {
                response.isSuccessful -> {
                    profileStudentMutableLiveData.postValue(response.body())
                }
                response.code() == 400 -> {
                    Utils().getNewAccessToken()
                    profileDetailsStudent()
                }
                else -> errorStudentMutableLiveData.postValue("Error code is ${response.code()}")
            }
        }
    }

    fun uploadProfilePic(uri: Uri, student_account_id: Int) {
        val imageRef = Firebase.storage.reference.child("${student_account_id}.jpg")
        imageRef.putFile(uri).addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
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

    private val updateProfileLiveData = MutableLiveData<Response<StudentProfileDetailsResponse>>()
    suspend fun updateProfileDetails(updateProfileDetailsBody: UpdateProfileDetailsBody):
            MutableLiveData<Response<StudentProfileDetailsResponse>> {
        try {
            val response=
                ServiceBuilder.getInstance().updateProfileDetails(updateProfileDetailsBody)
            Log.e("dddd", response.body().toString())
            when {
                response.isSuccessful ->
                    updateProfileLiveData.postValue(Response.Success(response.body()))
                response.code() == 400 -> {
                    Utils().getNewAccessToken()
                    updateProfileDetails(updateProfileDetailsBody)
                }
                else ->
                    updateProfileLiveData.postValue(Response.Error("Error code is ${response.code()}"))
            }
        } catch (e: Exception) {
            updateProfileLiveData.postValue(Response.Error(e.message!!))
        }
        return updateProfileLiveData
    }
}