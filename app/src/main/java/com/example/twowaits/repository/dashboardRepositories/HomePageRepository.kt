package com.example.twowaits.repository.dashboardRepositories

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.Data
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.apiCalls.authApiCalls.SendOtpResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.RecentLecturesResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.RecentNotesResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.RecentQuizzesResponse
import com.example.twowaits.homePages.UploadLectureBody
import com.example.twowaits.homePages.UploadNoteBody
import com.example.twowaits.homePages.UploadNotePartialBody
import com.example.twowaits.homePages.navdrawerPages.ChangePasswordBody
import com.example.twowaits.homePages.navdrawerPages.Feedbackbody
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
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
    val feedbackData = MutableLiveData<String>()
    private val recentLecturesData = MutableLiveData<List<RecentLecturesResponse>>()
    val recentLecturesLiveData: LiveData<List<RecentLecturesResponse>> = recentLecturesData
    private val errorRecentLecturesData = MutableLiveData<String>()
    val errorRecentLecturesLiveData: LiveData<String> = errorRecentLecturesData
    val uploadLectureData = MutableLiveData<String>()

    fun recentQuizzes() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.getInstance().recentQuizzes()
            when {
                response.isSuccessful -> {
                    recentQuizzesData.postValue(response.body())
                }
                response.code() == 400 -> {
                    val result = Data().getNewAccessToken()
                    if (result == "success") {
                        recentQuizzes()
                    } else {
                        errorRecentQuizzesData.postValue("Some error has occurred!\nPlease try again")
                    }
                }
                else -> {
                    errorRecentQuizzesData.postValue("${response.message()}\nPlease try again")
                }
            }
        }
    }

    fun recentNotes() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.getInstance().recentNotes()
            when {
                response.isSuccessful -> {
                    recentNotesData.postValue(response.body())
                }
                response.code() == 400 -> {
                    val result = Data().getNewAccessToken()
                    if (result == "success") {
                        recentNotes()
                    } else {
                        errorRecentNotesData.postValue("Some error has occurred!\nPlease try again")
                    }
                }
                else -> {
                    errorRecentNotesData.postValue("${response.message()}\nPlease try again")
                }
            }
        }
    }

    fun recentLectures() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.getInstance().recentLectures()
            when {
                response.isSuccessful -> {
                    recentLecturesData.postValue(response.body())
                }
                response.code() == 400 -> {
                    val result = Data().getNewAccessToken()
                    if (result == "success") {
                        recentLectures()
                    } else {
                        errorRecentLecturesData.postValue("Some error has occurred!\nPlease try again")
                    }
                }
                else -> {
                    errorRecentLecturesData.postValue("${response.message()}\nPlease try again")
                }
            }
        }
    }

    fun changePassword(changePasswordBody: ChangePasswordBody) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.getInstance().changePassword(changePasswordBody)
            when {
                response.isSuccessful -> {
                    changePasswordData.postValue(response.body())
                }
                response.code() == 405 -> {
                    errorChangePassword.postValue("Invalid old password")
                }
                response.code() == 400 -> {
                    val result = Data().getNewAccessToken()
                    if (result == "success") {
                        changePassword(changePasswordBody)
                    } else {
                        errorChangePassword.postValue("Some error has occurred!\nPlease try again")
                    }
                }
                else -> {
                    errorChangePassword.postValue(
                        "${response.message()}\n" +
                                "Please try again"
                    )
                }
            }
        }
    }

    fun feedback(feedbackBody: Feedbackbody) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.getInstance().feedback(feedbackBody)
            when {
                response.isSuccessful -> {
                    feedbackData.postValue("success")
                }
                response.code() == 400 -> {
                    val result = Data().getNewAccessToken()
                    if (result == "success") {
                        feedback(feedbackBody)
                    } else {
                        feedbackData.postValue("Some error has occurred!\nPlease try again")
                    }
                }
                else -> {
                    feedbackData.postValue("${response.message()}\nPlease try again")
                }
            }
        }
    }

    fun uploadNote(uri: Uri, uploadPartialNoteBody: UploadNotePartialBody) {
        val sdf = SimpleDateFormat("yyyy/MM/dd_HH:mm:ss", Locale.UK)
        val file = sdf.format(Date()) + Random.nextInt(1000)
        val pdfRef = Firebase.storage.reference.child("${file}.pdf")
        pdfRef.putFile(uri)
            .addOnSuccessListener {
                pdfRef.downloadUrl
                    .addOnSuccessListener {
                        GlobalScope.launch(Dispatchers.IO) {
                            val uploadNoteBody = UploadNoteBody(
                                uploadPartialNoteBody.description,
                                uploadPartialNoteBody.title,
                                it.toString()
                            )
                            val response = RetrofitClient.getInstance().uploadNote(uploadNoteBody)
                            when {
                                response.isSuccessful -> {
                                    uploadPDF.postValue("success")
                                }
                                response.code() == 400 -> {
                                    val result = Data().getNewAccessToken()
                                    if (result == "success") {
                                        uploadNote(uri, uploadPartialNoteBody)
                                    } else {
                                        uploadPDF.postValue("Some error has occurred!\nPlease try again")
                                    }
                                }
                                else -> {
                                    uploadPDF.postValue(
                                        "${response.message()}\n" +
                                                "Please try again"
                                    )
                                }
                            }
                        }
                    }
                    .addOnFailureListener {
                        uploadPDF.postValue(it.message + "\nPlease try again")
                    }
            }
            .addOnFailureListener {
                uploadPDF.postValue(it.message + "\nPlease try again")
            }
    }

    fun uploadLecture(title: String, description: String, uri: Uri) {
        val sdf = SimpleDateFormat("yyyy/MM/dd_HH:mm:ss", Locale.UK)
        val file = sdf.format(Date()) + Random.nextInt(1000)
        val videoRef = Firebase.storage.reference.child("${file}.mp4")
        videoRef.putFile(uri)
            .addOnSuccessListener {
                videoRef.downloadUrl
                    .addOnSuccessListener {
                        GlobalScope.launch(Dispatchers.IO) {
                            val response = RetrofitClient.getInstance().uploadLecture(
                                UploadLectureBody(description, title, it.toString())
                            )
                            when {
                                response.isSuccessful -> {
                                    uploadLectureData.postValue("success")
                                }
                                response.code() == 400 -> {
                                    val result = Data().getNewAccessToken()
                                    if (result == "success") {
                                        uploadLecture(title, description, uri)
                                    } else {
                                        uploadLectureData.postValue("Some error has occurred!\nPlease try again")
                                    }
                                }
                                else -> {
                                    uploadLectureData.postValue(
                                        "${response.message()}\n" +
                                                "Please try again"
                                    )
                                }
                            }
                        }
                    }
                    .addOnFailureListener {
                        uploadLectureData.postValue(it.message + "\nPlease try again")
                    }
            }
            .addOnFailureListener {
                uploadLectureData.postValue(it.message + "\nPlease try again")
            }
    }
}