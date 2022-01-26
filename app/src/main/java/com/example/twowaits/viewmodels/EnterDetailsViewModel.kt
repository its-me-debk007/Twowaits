package com.example.twowaits.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.twowaits.authPages.CreateFacultyProfileBody
import com.example.twowaits.authPages.CreateStudentProfileBody
import com.example.twowaits.repository.authRepositories.CreateProfileRepository
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class EnterDetailsViewModel: ViewModel() {
    lateinit var createStudentProfileLiveData: LiveData<String>
    lateinit var createFacultyProfileLiveData: LiveData<String>

    fun createStudentProfileDetails(name: String, college: String, course: String, branch: String,
                                    year: String, gender: String,
                                    dob: String, uri: Uri){
        val repository = CreateProfileRepository()
        repository.createStudentProfileDetails(name, college, course, branch, year, gender, dob, uri)
        createStudentProfileLiveData = repository.createStudentProfileLiveData
    }

    fun createFacultyProfileDetails(name: String, department: String, college: String, gender: String,
                                    dob: String, uri: Uri){
        val repository = CreateProfileRepository()
        repository.createFacultyProfileDetails(name, department, college, gender, dob, uri)
        createFacultyProfileLiveData = repository.createFacultyProfileLiveData
    }
}