package com.example.twowaits.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twowaits.sealedClasses.Response
import com.example.twowaits.models.auth.SendOtpResponse
import com.example.twowaits.network.dashboardApiCalls.QnAResponseItem
import com.example.twowaits.network.dashboardApiCalls.RecentLecturesResponse
import com.example.twowaits.network.dashboardApiCalls.RecentNotesResponse
import com.example.twowaits.network.dashboardApiCalls.RecentQuizzesResponse
import com.example.twowaits.homePages.UploadNotePartialBody
import com.example.twowaits.homePages.navdrawerPages.ChangePasswordBody
import com.example.twowaits.homePages.navdrawerPages.Feedbackbody
import com.example.twowaits.repositories.homeRepositories.HomePageRepository
import com.example.twowaits.repositories.homeRepositories.questionsAnswersRepositories.QnARepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

class HomePageViewModel : ViewModel() {
    lateinit var recentLecturesLiveData: LiveData<Response<List<RecentLecturesResponse>>>
    lateinit var recentNotesLiveData: LiveData<Response<List<RecentNotesResponse>>>
    lateinit var recentQuizzesLiveData: LiveData<Response<List<RecentQuizzesResponse>>>
    lateinit var getQnALiveData: LiveData<Response<List<QnAResponseItem>>>

    lateinit var changePasswordLiveData: LiveData<SendOtpResponse>
    lateinit var errorChangePasswordData: LiveData<String>
    lateinit var uploadPDF: MutableLiveData<String>
    lateinit var getSearchQnAData: MutableLiveData<List<QnAResponseItem>>
    lateinit var errorGetSearchQnAData: MutableLiveData<String>
    lateinit var feedbackData: MutableLiveData<String>

    lateinit var uploadLectureData: MutableLiveData<String>

    fun getQnA() = viewModelScope.launch{
        getQnALiveData = QnARepository().getQnA()
    }

    fun recentQuizzes() = viewModelScope.launch{
        recentQuizzesLiveData= HomePageRepository().recentQuizzes()
    }

    fun recentNotes() = viewModelScope.launch {
        recentNotesLiveData = HomePageRepository().recentNotes()
    }

    fun recentLectures() = viewModelScope.launch {
        recentLecturesLiveData = HomePageRepository().recentLectures()
    }

    fun changePassword(changePasswordBody: ChangePasswordBody) {
        val repository = HomePageRepository()
        repository.changePassword(changePasswordBody)
        changePasswordLiveData = repository.changePasswordLiveData
        errorChangePasswordData = repository.errorChangePasswordData
    }

    fun feedback(feedbackBody: Feedbackbody) {
        val repository = HomePageRepository()
        repository.feedback(feedbackBody)
        feedbackData = repository.feedbackData
    }

    fun uploadNote(uri: Uri, uploadPartialNoteBody: UploadNotePartialBody) {
        val repository = HomePageRepository()
        repository.uploadNote(uri, uploadPartialNoteBody)
        uploadPDF = repository.uploadPDF
    }

    fun uploadLecture(title: String, description: String, uri: Uri) {
        val repository = HomePageRepository()
        repository.uploadLecture(title, description, uri)
        uploadLectureData = repository.uploadLectureData
    }

    fun searchQnA(search: String) {
        val repository = QnARepository()
        repository.searchQnA(search)
        getSearchQnAData = repository.getSearchQnAData
        errorGetSearchQnAData = repository.errorGetSearchQnAData
    }
}