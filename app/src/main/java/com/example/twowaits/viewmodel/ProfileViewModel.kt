package com.example.twowaits.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twowaits.model.ProfileDetails
import com.example.twowaits.model.ProfileDetailsExcludingId
import com.example.twowaits.model.home.UpdateProfilePic
import com.example.twowaits.repository.homeRepository.ProfileRepository
import com.example.twowaits.sealedClass.Response
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val repository = ProfileRepository()

    lateinit var profileLiveData: LiveData<Response<ProfileDetails>>
    fun getProfile() = viewModelScope.launch {
        profileLiveData = repository.getProfile()
    }

    lateinit var firebaseLiveData: LiveData<String>
    fun uploadPicFirebase(uri: Uri, student_account_id: Int) {
        repository.uploadPicFirebase(uri, student_account_id)
        firebaseLiveData = repository.firebaseLiveData
    }

    lateinit var updateProfilePicLiveData: LiveData<String>
    fun updateProfilePic(name: String, uri: String) =
        viewModelScope.launch {
            updateProfilePicLiveData =
                repository.updateProfilePic(UpdateProfilePic(name, uri))
        }

    lateinit var updateProfileLiveData: LiveData<String>
    fun updateProfile(body: ProfileDetailsExcludingId) =
        viewModelScope.launch {
            updateProfileLiveData =
                repository.updateProfile(body)
        }
}