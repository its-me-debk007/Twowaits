package com.example.twowaits.repository.dashboardRepositories

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.apiCalls.dashboardApiCalls.FacultyProfileDetailsResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.StudentProfileDetailsResponse
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileRepository {
    private val profileMutableLiveData = MutableLiveData<FacultyProfileDetailsResponse>()
    val profileLiveData: LiveData<FacultyProfileDetailsResponse> = profileMutableLiveData
    private val errorMutableLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = errorMutableLiveData
    private val saveRefreshTokenMutableLiveData = MutableLiveData<String>()
    val saveRefreshTokenLiveData: LiveData<String> = saveRefreshTokenMutableLiveData
    private val uploadImageMutableLiveData = MutableLiveData<String>()
    val uploadImageLiveData: LiveData<String> = uploadImageMutableLiveData

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
                    errorMutableLiveData.postValue("Error code is ${response.code()}")
                }
            }
        }
    }

    fun uploadProfilePic(uri: Uri) {
        var storageRef = Firebase.storage.reference
        val imageRef = storageRef.child("sample.jpg")
        imageRef.putFile(uri)
            .addOnSuccessListener {
                uploadImageMutableLiveData.postValue("Uploaded")
                imageRef.downloadUrl
                    .addOnSuccessListener {
                        Log.d("URI", it.toString())
                    }
            }
            .addOnFailureListener{
                uploadImageMutableLiveData.postValue(it.message)
            }
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