package com.example.twowaits.repository.homeRepository

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.ui.fragment.navDrawer.ChangePasswordBody
import com.example.twowaits.ui.fragment.navDrawer.Feedbackbody
import com.example.twowaits.model.auth.SendOtpResponse
import com.example.twowaits.model.home.UploadLectureBody
import com.example.twowaits.model.home.UploadNoteBody
import com.example.twowaits.model.home.UploadNotePartialBody
import com.example.twowaits.network.ServiceBuilder
import com.example.twowaits.network.dashboardApiCalls.RecentLecturesResponse
import com.example.twowaits.network.dashboardApiCalls.RecentNotesResponse
import com.example.twowaits.network.dashboardApiCalls.RecentQuizzesResponse
import com.example.twowaits.sealedClass.Response
import com.example.twowaits.utils.Utils
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
    val uploadPDF = MutableLiveData<String>()
    val feedbackData = MutableLiveData<String>()
    val uploadLectureData = MutableLiveData<String>()
    val recentQuizLiveData = MutableLiveData<Response<List<RecentQuizzesResponse>>>()
    val recentNoteLiveData = MutableLiveData<Response<List<RecentNotesResponse>>>()
    val recentLectureLiveData = MutableLiveData<Response<List<RecentLecturesResponse>>>()
    val changePasswordLiveData = MutableLiveData<Response<SendOtpResponse>>()

    fun recentQuizzes(): MutableLiveData<Response<List<RecentQuizzesResponse>>> {
        ServiceBuilder.getInstance().recentQuizzes()
            .enqueue(object : Callback<List<RecentQuizzesResponse>> {
                override fun onResponse(
                    call: Call<List<RecentQuizzesResponse>>,
                    response: retrofit2.Response<List<RecentQuizzesResponse>>
                ) {
                    when {
                        response.isSuccessful ->
                            recentQuizLiveData.postValue(Response.Success(response.body()))

                        response.code() == 400 -> {
                            Utils().getNewAccessToken()
                            recentQuizzes()
                        }

                        else -> recentQuizLiveData.postValue(Response.Error(response.message() + "\nPlease try again"))
                    }
                }

                override fun onFailure(call: Call<List<RecentQuizzesResponse>>, t: Throwable) {
                    recentQuizLiveData.postValue(Response.Error(t.message + "\nPlease try again"))
                }
            })
        return recentQuizLiveData
    }

    fun recentNotes(): MutableLiveData<Response<List<RecentNotesResponse>>> {
        ServiceBuilder.getInstance().recentNotes()
            .enqueue(object : Callback<List<RecentNotesResponse>> {
                override fun onResponse(
                    call: Call<List<RecentNotesResponse>>,
                    response: retrofit2.Response<List<RecentNotesResponse>>
                ) {
                    when {
                        response.isSuccessful ->
                            recentNoteLiveData.postValue(Response.Success(response.body()))

                        response.code() == 400 -> {
                            Utils().getNewAccessToken()
                            recentNotes()
                        }

                        else -> recentNoteLiveData.postValue(Response.Error(response.message() + "\nPlease try again"))
                    }
                }

                override fun onFailure(call: Call<List<RecentNotesResponse>>, t: Throwable) {
                    recentNoteLiveData.postValue(Response.Error(t.message + "\nPlease try again"))
                }
            })
        return recentNoteLiveData
    }

    fun recentLectures(): MutableLiveData<Response<List<RecentLecturesResponse>>> {
        ServiceBuilder.getInstance().recentLectures()
            .enqueue(object : Callback<List<RecentLecturesResponse>> {
                override fun onResponse(
                    call: Call<List<RecentLecturesResponse>>,
                    response: retrofit2.Response<List<RecentLecturesResponse>>
                ) {
                    when {
                        response.isSuccessful ->
                            recentLectureLiveData.postValue(Response.Success(response.body()))

                        response.code() == 400 -> {
                            Utils().getNewAccessToken()
                            recentLectures()
                        }

                        else -> recentLectureLiveData.postValue(Response.Error(response.message() + "\nPlease try again"))
                    }
                }

                override fun onFailure(call: Call<List<RecentLecturesResponse>>, t: Throwable) {
                    recentLectureLiveData.postValue(Response.Error(t.message + "\nPlease try again"))
                }
            })
        return recentLectureLiveData
    }

    fun changePassword(changePasswordBody: ChangePasswordBody): MutableLiveData<Response<SendOtpResponse>> {
        ServiceBuilder.getInstance().changePassword(changePasswordBody)
            .enqueue(object : Callback<SendOtpResponse> {
                override fun onResponse(
                    call: Call<SendOtpResponse>,
                    response: retrofit2.Response<SendOtpResponse>
                ) {
                    when {
                        response.isSuccessful ->
                            changePasswordLiveData.postValue(Response.Success(response.body()))

                        response.code() == 400 -> {
                            Utils().getNewAccessToken()
                            changePassword(changePasswordBody)
                        }

                        else -> changePasswordLiveData.postValue(Response.Error(response.message() + "\nPlease try again"))
                    }
                }

                override fun onFailure(call: Call<SendOtpResponse>, t: Throwable) {
                    changePasswordLiveData.postValue(Response.Error(t.message + "\nPlease try again"))
                }
            })
        return changePasswordLiveData
    }

    suspend fun feedback(feedbackBody: Feedbackbody) {
        val response = ServiceBuilder.getInstance().feedback(feedbackBody)
        when {
            response.isSuccessful -> feedbackData.postValue("success")
            response.code() == 400 -> {
                Utils().getNewAccessToken()
                feedback(feedbackBody)
            }
            else ->
                feedbackData.postValue("${response.message()}\nPlease try again")
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
                                    Utils().getNewAccessToken()
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
                                    Utils().getNewAccessToken()
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