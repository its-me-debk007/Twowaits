package com.example.twowaits.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.twowaits.apiCalls.authApiCalls.SendOtpResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.QnAResponseItem
import com.example.twowaits.apiCalls.dashboardApiCalls.RecentLecturesResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.RecentNotesResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.RecentQuizzesResponse
import com.example.twowaits.homePages.UploadNoteBody
import com.example.twowaits.homePages.UploadNotePartialBody
import com.example.twowaits.homePages.navdrawerPages.ChangePasswordBody
import com.example.twowaits.homePages.navdrawerPages.Feedbackbody
import com.example.twowaits.repository.dashboardRepositories.HomePageRepository
import com.example.twowaits.repository.dashboardRepositories.ProfileRepository
import com.example.twowaits.repository.dashboardRepositories.questionsAnswersRepositories.QnARepository
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class HomePageViewModel: ViewModel() {
    lateinit var getQnALiveData: LiveData<List<QnAResponseItem>>
    lateinit var errorGetQnALiveData: LiveData<String>
    lateinit var recentQuizzesLiveData: LiveData<List<RecentQuizzesResponse>>
    lateinit var errorRecentQuizzesLiveData: LiveData<String>
    lateinit var recentNotesLiveData: LiveData<List<RecentNotesResponse>>
    lateinit var errorRecentNotesLiveData: LiveData<String>
    lateinit var changePasswordLiveData: LiveData<SendOtpResponse>
    lateinit var errorChangePasswordData: LiveData<String>
    lateinit var uploadPDF: MutableLiveData<String>
    lateinit var getSearchQnAData: MutableLiveData<List<QnAResponseItem>>
    lateinit var errorGetSearchQnAData: MutableLiveData<String>
    lateinit var feedbackData: MutableLiveData<String>
    lateinit var recentLecturesLiveData: LiveData<List<RecentLecturesResponse>>
    lateinit var errorRecentLecturesLiveData: LiveData<String>
    lateinit var uploadLectureData: MutableLiveData<String>
    var isClicked = false

    fun getQnA() {
        val repository = QnARepository()
        repository.getQnA()
        getQnALiveData = repository.getQnALiveData
        errorGetQnALiveData = repository.errorGetQnALiveData
    }

    fun recentQuizzes(){
        val repository = HomePageRepository()
        repository.recentQuizzes()
        recentQuizzesLiveData = repository.recentQuizzesLiveData
        errorRecentQuizzesLiveData = repository.errorRecentQuizzesLiveData
    }

    fun recentNotes(){
        val repository = HomePageRepository()
        repository.recentNotes()
        recentNotesLiveData = repository.recentNotesLiveData
        errorRecentNotesLiveData = repository.errorRecentNotesLiveData
    }

    fun recentLectures(){
        val repository = HomePageRepository()
        repository.recentLectures()
        recentLecturesLiveData = repository.recentLecturesLiveData
        errorRecentLecturesLiveData = repository.errorRecentLecturesLiveData
    }

    fun changePassword(changePasswordBody: ChangePasswordBody){
        val repository = HomePageRepository()
        repository.changePassword(changePasswordBody)
        changePasswordLiveData = repository.changePasswordLiveData
        errorChangePasswordData = repository.errorChangePasswordData
    }

    fun feedback(feedbackbody: Feedbackbody){
        val repository = HomePageRepository()
        repository.feedback(feedbackbody)
        feedbackData = repository.feedbackData
    }

    fun uploadNote(uri: Uri, uploadPartialNoteBody: UploadNotePartialBody){
        val repository = HomePageRepository()
        repository.uploadNote(uri, uploadPartialNoteBody)
        uploadPDF = repository.uploadPDF
    }

    fun uploadLecture(title: String, description: String, uri: Uri){
        val repository = HomePageRepository()
        repository.uploadLecture(title, description, uri)
        uploadLectureData = repository.uploadLectureData
    }

    fun searchQnA(search: String){
        val repository = QnARepository()
        repository.searchQnA(search)
        getSearchQnAData = repository.getSearchQnAData
        errorGetSearchQnAData = repository.errorGetSearchQnAData
    }
}