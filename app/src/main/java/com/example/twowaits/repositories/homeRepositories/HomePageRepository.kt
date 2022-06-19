package com.example.twowaits.repositories.homeRepositories

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.utils.Utils
import com.example.twowaits.sealedClasses.Response
import com.example.twowaits.network.ServiceBuilder
import com.example.twowaits.models.auth.SendOtpResponse
import com.example.twowaits.network.dashboardApiCalls.RecentLecturesResponse
import com.example.twowaits.network.dashboardApiCalls.RecentNotesResponse
import com.example.twowaits.network.dashboardApiCalls.RecentQuizzesResponse
import com.example.twowaits.models.home.UploadLectureBody
import com.example.twowaits.models.home.UploadNoteBody
import com.example.twowaits.models.home.UploadNotePartialBody
import com.example.twowaits.homePages.navdrawerPages.ChangePasswordBody
import com.example.twowaits.homePages.navdrawerPages.Feedbackbody
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

@DelicateCoroutinesApi
class HomePageRepository {
    private val changePasswordData = MutableLiveData<SendOtpResponse>()
    val changePasswordLiveData: LiveData<SendOtpResponse> = changePasswordData
    private val errorChangePassword = MutableLiveData<String>()
    val errorChangePasswordData: LiveData<String> = errorChangePassword
    val uploadPDF = MutableLiveData<String>()
    val feedbackData = MutableLiveData<String>()
    val uploadLectureData = MutableLiveData<String>()

    fun recentQuizzes(): MutableLiveData<Response<List<RecentQuizzesResponse>>> {
        val liveData = MutableLiveData<Response<List<RecentQuizzesResponse>>>()
        val call = ServiceBuilder.getInstance().recentQuizzes()

        call.enqueue(object : Callback<List<RecentQuizzesResponse>> {
            override fun onResponse(
                call: Call<List<RecentQuizzesResponse>>,
                response: retrofit2.Response<List<RecentQuizzesResponse>>
            ) {
                when {
                    response.isSuccessful ->
                        liveData.postValue(Response.Success(response.body()))

                    response.code() == 400 -> {
                        val result = Utils().getNewAccessToken()
//                        if (result == "success") recentQuizzes()
//                        else liveData.postValue(Response.Error("Some error has occurred!\nPlease try again"))
                        recentQuizzes()
                    }

                    else -> liveData.postValue(Response.Error(response.message() + "\nPlease try again"))
                }
            }

            override fun onFailure(call: Call<List<RecentQuizzesResponse>>, t: Throwable) {
                liveData.postValue(Response.Error(t.message + "\nPlease try again"))
            }
        })
        return liveData
    }

    fun recentNotes(): MutableLiveData<Response<List<RecentNotesResponse>>> {
        val liveData = MutableLiveData<Response<List<RecentNotesResponse>>>()
        val call = ServiceBuilder.getInstance().recentNotes()

        call.enqueue(object : Callback<List<RecentNotesResponse>> {
            override fun onResponse(
                call: Call<List<RecentNotesResponse>>,
                response: retrofit2.Response<List<RecentNotesResponse>>
            ) {
                when {
                    response.isSuccessful ->
                        liveData.postValue(Response.Success(response.body()))

                    response.code() == 400 -> {
                        val result = Utils().getNewAccessToken()
                        Log.e("dddd", "token expire")
//                        if (result == "success") recentNotes()
//                        else liveData.postValue(Response.Error("Some error has occurred!\nPlease try again"))
                        recentNotes()
                    }

                    else -> liveData.postValue(Response.Error(response.message() + "\nPlease try again"))
                }
            }

            override fun onFailure(call: Call<List<RecentNotesResponse>>, t: Throwable) {
                liveData.postValue(Response.Error(t.message + "\nPlease try again"))
            }
        })
        return liveData
    }

    fun recentLectures(): MutableLiveData<Response<List<RecentLecturesResponse>>> {
        val liveData = MutableLiveData<Response<List<RecentLecturesResponse>>>()
        val call = ServiceBuilder.getInstance().recentLectures()
        call.enqueue(object : Callback<List<RecentLecturesResponse>> {
            override fun onResponse(
                call: Call<List<RecentLecturesResponse>>,
                response: retrofit2.Response<List<RecentLecturesResponse>>
            ) {
                when {
                    response.isSuccessful ->
                        liveData.postValue(Response.Success(response.body()))

                    response.code() == 400 -> {
                        val result = Utils().getNewAccessToken()
                        Log.e("dddd", "token expire")
//                        if (result == "success") recentLectures()
//                        else liveData.postValue(Response.Error("Some error has occurred!\nPlease try again"))
                        recentLectures()
                    }

                    else -> liveData.postValue(Response.Error(response.message() + "\nPlease try again"))
                }
            }

            override fun onFailure(call: Call<List<RecentLecturesResponse>>, t: Throwable) {
                liveData.postValue(Response.Error(t.message + "\nPlease try again"))
            }
        })
        return liveData
    }

    fun changePassword(changePasswordBody: ChangePasswordBody): MutableLiveData<Response<SendOtpResponse>> {
        val liveData = MutableLiveData<Response<SendOtpResponse>>()
        val call = ServiceBuilder.getInstance().changePassword(changePasswordBody)
        call.enqueue(object : Callback<SendOtpResponse> {
            override fun onResponse(
                call: Call<SendOtpResponse>,
                response: retrofit2.Response<SendOtpResponse>
            ) {
                when {
                    response.isSuccessful ->
                        liveData.postValue(Response.Success(response.body()))

                    response.code() == 400 -> {
                        val result = Utils().getNewAccessToken()
                        Log.e("dddd", "token expire")
//                        if (result == "success") changePassword(changePasswordBody)
//                        else liveData.postValue(Response.Error("Some error has occurred!\nPlease try again"))
                        changePassword(changePasswordBody)
                    }

                    else -> liveData.postValue(Response.Error(response.message() + "\nPlease try again"))
                }
            }

            override fun onFailure(call: Call<SendOtpResponse>, t: Throwable) {
                liveData.postValue(Response.Error(t.message + "\nPlease try again"))
            }
        })
        return liveData
    }

    fun feedback(feedbackBody: Feedbackbody) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = ServiceBuilder.getInstance().feedback(feedbackBody)
            when {
                response.isSuccessful -> {
                    feedbackData.postValue("success")
                }
                response.code() == 400 -> {
                    val result = Utils().getNewAccessToken()
//                    if (result == "success") feedback(feedbackBody)
//                    else feedbackData.postValue("Some error has occurred!\nPlease try again")
                    feedback(feedbackBody)
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
                            val response = ServiceBuilder.getInstance().uploadNote(uploadNoteBody)
                            when {
                                response.isSuccessful -> {
                                    uploadPDF.postValue("success")
                                }
                                response.code() == 400 -> {
                                    val result = Utils().getNewAccessToken()
//                                    if (result == "success") uploadNote(uri, uploadPartialNoteBody)
//                                    else uploadPDF.postValue("Some error has occurred!\nPlease try again")
                                    uploadNote(uri, uploadPartialNoteBody)
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
                            val response = ServiceBuilder.getInstance().uploadLecture(
                                UploadLectureBody(description, title, it.toString())
                            )
                            when {
                                response.isSuccessful -> {
                                    uploadLectureData.postValue("success")
                                }
                                response.code() == 400 -> {
                                    val result = Utils().getNewAccessToken()
//                                    if (result == "success") uploadLecture(title, description, uri)
//                                    else uploadLectureData.postValue("Some error has occurred!\nPlease try again")
                                    uploadLecture(title, description, uri)
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