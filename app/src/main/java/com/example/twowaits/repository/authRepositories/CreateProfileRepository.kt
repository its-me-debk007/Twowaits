package com.example.twowaits.repository.authRepositories

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.authPages.CreateFacultyProfileBody
import com.example.twowaits.authPages.CreateStudentProfileBody
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random.Default.nextInt

@DelicateCoroutinesApi
class CreateProfileRepository {
    val createStudentProfileData = MutableLiveData<String>()
    val createFacultyProfileData = MutableLiveData<String>()

    fun createStudentProfileDetails(name: String, college: String, course: String, branch: String,
                                    year: String, gender: String,
                                    dob: String, uri: Uri){
        val sdf = SimpleDateFormat("yyyy/MM/dd_HH:mm:ss", Locale.UK)
        val file = sdf.format(Date())+nextInt(1000)
        val imageRef = Firebase.storage.reference.child("${file}.jpg")
        imageRef.putFile(uri)
            .addOnSuccessListener {
                imageRef.downloadUrl
                    .addOnSuccessListener {
                        GlobalScope.launch {
                            val response = RetrofitClient.getInstance().createStudentProfileDetails(
                                CreateStudentProfileBody(name, college, course, branch,
                                    year, gender, dob, it.toString()))
                            when {
                                response.isSuccessful -> createStudentProfileData.postValue("success")
                                else -> createStudentProfileData.postValue("${response.message()}\nPlease try again")
                            }
                        }
                    }
                    .addOnFailureListener {
                        createStudentProfileData.postValue(it.message)
                    }
            }
            .addOnFailureListener {
                createStudentProfileData.postValue(it.message)
            }
    }

    fun createFacultyProfileDetails(college: String, department: String, dob: String, gender: String,
                                    name: String, uri: Uri){
        val sdf = SimpleDateFormat("yyyy/MM/dd_HH:mm:ss", Locale.UK)
        val file = sdf.format(Date())+nextInt(1000)
        val imageRef = Firebase.storage.reference.child("${file}.jpg")
        imageRef.putFile(uri)
            .addOnSuccessListener {
                imageRef.downloadUrl
                    .addOnSuccessListener {
                        GlobalScope.launch {
                            val response = RetrofitClient.getInstance().createFacultyProfileDetails(
                                CreateFacultyProfileBody(college, department, dob, gender, name, it.toString()))
                            when {
                                response.isSuccessful -> createFacultyProfileData.postValue("success")
                                else -> createFacultyProfileData.postValue("${response.message()}\nPlease try again")
                            }
                        }
                    }
                    .addOnFailureListener {
                        createFacultyProfileData.postValue(it.message)
                    }
            }
            .addOnFailureListener {
                createFacultyProfileData.postValue(it.message)
            }
    }
}