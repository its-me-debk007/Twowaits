package com.example.twowaits.repository.dashboardRepositories

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.apiCalls.authApiCalls.SendOtpResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.RecentNotesResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.RecentQuizzesResponse
import com.example.twowaits.homePages.UploadNoteBody
import com.example.twowaits.homePages.UploadNotePartialBody
import com.example.twowaits.homePages.navdrawerPages.ChangePasswordBody
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

@DelicateCoroutinesApi
class HomePageRepository {
    private val recentQuizzesData = MutableLiveData<List<RecentQuizzesResponse>>()
    val recentQuizzesLiveData: LiveData<List<RecentQuizzesResponse>> = recentQuizzesData
    private val errorRecentQuizzesData = MutableLiveData<String>()
    val errorRecentQuizzesLiveData: LiveData<String> = errorRecentQuizzesData
    private val recentNotesData = MutableLiveData<List<RecentNotesResponse>>()
    val recentNotesLiveData: LiveData<List<RecentNotesResponse>> = recentNotesData
    private val errorRecentNotesData = MutableLiveData<String>()
    val errorRecentNotesLiveData: LiveData<String> = errorRecentNotesData
    private val changePasswordData = MutableLiveData<SendOtpResponse>()
    val changePasswordLiveData: LiveData<SendOtpResponse> = changePasswordData
    private val errorChangePassword = MutableLiveData<String>()
    val errorChangePasswordData: LiveData<String> = errorChangePassword
    val uploadPDF = MutableLiveData<String>()

    fun recentQuizzes(){
        GlobalScope.launch {
            val response = RetrofitClient.getInstance().recentQuizzes()
            when {
                response.isSuccessful -> {
                    recentQuizzesData.postValue(response.body())
                } else -> {
                    errorRecentQuizzesData.postValue("Error code is ${response.code()}\n${response.message()}")
            }
            }
        }
    }

    fun recentNotes(){
        GlobalScope.launch {
            val response = RetrofitClient.getInstance().recentNotes()
            when {
                response.isSuccessful -> {
                    recentNotesData.postValue(response.body())
                } else -> {
                    errorRecentNotesData.postValue("Error code is ${response.code()}\n${response.message()}")
            }
            }
        }
    }

    fun changePassword(changePasswordBody: ChangePasswordBody){
        GlobalScope.launch {
            val response = RetrofitClient.getInstance().changePassword(changePasswordBody)
            when {
                response.isSuccessful -> {
                    changePasswordData.postValue(response.body())
                }
                response.code() == 405 ->{
                    errorChangePassword.postValue("Invalid old password")
                } else -> {
                    errorChangePassword.postValue("Error code is ${response.code()}\n${response.message()}")
                }
            }
        }
    }

    fun uploadNote(uri: Uri, uploadPartialNoteBody: UploadNotePartialBody){
        val sdf = SimpleDateFormat("yyyy/MM/dd_HH:mm:ss", Locale.UK)
        val file = sdf.format(Date())+ Random.nextInt(1000)
        val pdfRef = Firebase.storage.reference.child("${file}.pdf")
        pdfRef.putFile(uri)
            .addOnSuccessListener {
                pdfRef.downloadUrl
                    .addOnSuccessListener {
                        GlobalScope.launch {
                            val uploadNoteBody = UploadNoteBody(uploadPartialNoteBody.description, uploadPartialNoteBody.title, it.toString())
                            val response = RetrofitClient.getInstance().uploadNote(uploadNoteBody)
                            when {
                                response.isSuccessful -> {
                                    uploadPDF.postValue("success")
                                } else -> {
                                    uploadPDF.postValue("Error code is ${response.code()}\n${response.message()}")
                            }
                            }
                        }
                    }
                    .addOnFailureListener {
                        uploadPDF.postValue(it.message)
                    }
            }
            .addOnFailureListener {
                uploadPDF.postValue(it.message)
            }
    }
}