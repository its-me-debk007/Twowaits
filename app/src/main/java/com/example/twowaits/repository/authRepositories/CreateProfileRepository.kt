package com.example.twowaits.repository.authRepositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.authPages.CreateFacultyProfileBody
import com.example.twowaits.authPages.CreateStudentProfileBody
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class CreateProfileRepository {
    private val createStudentProfileData = MutableLiveData<String>()
    val createStudentProfileLiveData: LiveData<String> = createStudentProfileData

    private val createFacultyProfileData = MutableLiveData<String>()
    val createFacultyProfileLiveData: LiveData<String> = createFacultyProfileData

    fun createStudentProfileDetails(createStudentProfileBody: CreateStudentProfileBody){
        GlobalScope.launch {
            val response = RetrofitClient.getInstance().createStudentProfileDetails(createStudentProfileBody)
            when {
                response.isSuccessful -> createStudentProfileData.postValue("success")
                else -> createStudentProfileData.postValue("Error code ${response.code()}\n${response.message()}")
            }
        }
    }

    fun createFacultyProfileDetails(createFacultyProfileBody: CreateFacultyProfileBody){
        GlobalScope.launch {
            val response = RetrofitClient.getInstance().createFacultyProfileDetails(createFacultyProfileBody)
            when {
                response.isSuccessful -> createFacultyProfileData.postValue("success")
                else -> createFacultyProfileData.postValue("Error code ${response.code()}\n${response.message()}")
            }
        }
    }
}