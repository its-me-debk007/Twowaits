package com.example.twowaits.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.twowaits.repository.authRepositories.CreateProfileRepository
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class EnterDetailsViewModel: ViewModel() {
    lateinit var createStudentProfileLiveData: MutableLiveData<String>
    lateinit var createFacultyProfileLiveData: MutableLiveData<String>

    fun createStudentProfileDetails(name: String, college: String, course: String, branch: String,
                                    year: String, gender: String,
                                    dob: String, uri: Uri){
        val repository = CreateProfileRepository()
        repository.createStudentProfileDetails(name, college, course, branch, year, gender, dob, uri)
        createStudentProfileLiveData = repository.createStudentProfileData
    }

    fun createFacultyProfileDetails(college: String, department: String, dob: String, gender: String,
                                    name: String, uri: Uri){
        val repository = CreateProfileRepository()
        repository.createFacultyProfileDetails(college, department, dob, gender, name, uri)
        createFacultyProfileLiveData = repository.createFacultyProfileData
    }
}