package com.example.twowaits.repository.homeRepository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.model.ProfileDetails
import com.example.twowaits.model.ProfileDetailsExcludingId
import com.example.twowaits.model.home.UpdateProfilePic
import com.example.twowaits.network.ServiceBuilder
import com.example.twowaits.sealedClass.Response
import com.example.twowaits.utils.Utils
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import retrofit2.Call
import retrofit2.Callback

class ProfileRepository {
    private val uploadImageMutableLiveData = MutableLiveData<String>()
    val firebaseLiveData: LiveData<String> = uploadImageMutableLiveData

    private val profileLiveData = MutableLiveData<Response<ProfileDetails>>()
    fun getProfile(): MutableLiveData<Response<ProfileDetails>> {
        ServiceBuilder.getInstance().getProfile()
            .enqueue(object : Callback<ProfileDetails> {
                override fun onResponse(
                    call: Call<ProfileDetails>,
                    response: retrofit2.Response<ProfileDetails>
                ) {
                    when {
                        response.isSuccessful -> {
                            profileLiveData.postValue(Response.Success(response.body()))
                        }
                        response.code() == 400 -> {
                            Utils().getNewAccessToken()
                            getProfile()
                        }
                        else -> profileLiveData.postValue(Response.Error("Error code is ${response.code()}"))
                    }
                }

                override fun onFailure(call: Call<ProfileDetails>, t: Throwable) {
                    profileLiveData.postValue(Response.Error(t.message!!))
                }
            })
        return profileLiveData
    }

    fun uploadPicFirebase(uri: Uri, student_account_id: Int) {
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

    private val updatePicLiveData = MutableLiveData<String>()
    fun updateProfilePic(body: UpdateProfilePic):
            MutableLiveData<String> {

        ServiceBuilder.getInstance().updateProfilePic(body)
            .enqueue(object : Callback<ProfileDetails> {
                override fun onResponse(
                    call: Call<ProfileDetails>,
                    response: retrofit2.Response<ProfileDetails>
                ) {
                    when {
                        response.isSuccessful ->
                            updatePicLiveData.postValue("success")

                        response.code() == 400 -> {
                            Utils().getNewAccessToken()
                            getProfile()
                        }
                        else -> updatePicLiveData.postValue(response.message())
                    }
                }

                override fun onFailure(call: Call<ProfileDetails>, t: Throwable) {
                    updatePicLiveData.postValue(t.message)
                }
            })
        return updatePicLiveData
    }

    private val updateProfileLiveData = MutableLiveData<String>()
    fun updateProfile(body: ProfileDetailsExcludingId): MutableLiveData<String> {
        ServiceBuilder.getInstance().updateProfile(body).enqueue(object : Callback<ProfileDetails> {
            override fun onResponse(
                call: Call<ProfileDetails>,
                response: retrofit2.Response<ProfileDetails>
            ) {
                when {
                    response.isSuccessful -> {
                        updateProfileLiveData.postValue("success")
                        Log.e("UPDATE PROFILE REPO", response.code().toString())
                    }

                    response.code() == 400 -> {
                        Utils().getNewAccessToken()
                        updateProfile(body)
                    }
                    else -> updateProfileLiveData.postValue(response.message())
                }
            }

            override fun onFailure(call: Call<ProfileDetails>, t: Throwable) {
                updateProfileLiveData.postValue(t.message)
            }
        })
        return updateProfileLiveData
    }
}