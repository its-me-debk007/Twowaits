package com.example.twowaits.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twowaits.ui.fragment.navDrawer.ChangePasswordBody
import com.example.twowaits.ui.fragment.navDrawer.Feedbackbody
import com.example.twowaits.model.auth.SendOtpResponse
import com.example.twowaits.model.home.UploadNotePartialBody
import com.example.twowaits.network.dashboardApiCalls.QnAResponseItem
import com.example.twowaits.network.dashboardApiCalls.RecentLecturesResponse
import com.example.twowaits.network.dashboardApiCalls.RecentNotesResponse
import com.example.twowaits.network.dashboardApiCalls.RecentQuizzesResponse
import com.example.twowaits.repository.homeRepository.HomePageRepository
import com.example.twowaits.repository.homeRepository.questionsAnswersRepositories.QnARepository
import com.example.twowaits.sealedClass.Response
import kotlinx.coroutines.launch

class HomePageViewModel : ViewModel() {
    val homePageRepository = HomePageRepository()
    val qnARepository = QnARepository()
    lateinit var recentLecturesLiveData: LiveData<Response<List<RecentLecturesResponse>>>
    lateinit var recentNotesLiveData: LiveData<Response<List<RecentNotesResponse>>>
    lateinit var recentQuizzesLiveData: LiveData<Response<List<RecentQuizzesResponse>>>
    lateinit var getQnALiveData: LiveData<Response<List<QnAResponseItem>>>
    lateinit var changePasswordLiveData: LiveData<Response<SendOtpResponse>>

    lateinit var uploadPDF: MutableLiveData<String>
    lateinit var getSearchQnAData: MutableLiveData<List<QnAResponseItem>>
    lateinit var errorGetSearchQnAData: MutableLiveData<String>
    lateinit var feedbackData: MutableLiveData<String>

    lateinit var uploadLectureData: MutableLiveData<String>

    fun getQnA() = viewModelScope.launch {
        getQnALiveData = QnARepository().getQnA()
    }

    fun recentQuizzes() = viewModelScope.launch {
        recentQuizzesLiveData = homePageRepository.recentQuizzes()
    }

    fun recentNotes() = viewModelScope.launch {
        recentNotesLiveData = homePageRepository.recentNotes()
    }

    fun recentLectures() = viewModelScope.launch {
        recentLecturesLiveData = homePageRepository.recentLectures()
    }

    fun changePassword(changePasswordBody: ChangePasswordBody) = viewModelScope.launch {
        changePasswordLiveData = homePageRepository.changePassword(changePasswordBody)
    }

    fun feedback(feedbackBody: Feedbackbody) {
        viewModelScope.launch {
            homePageRepository.feedback(feedbackBody)
            feedbackData = homePageRepository.feedbackData
        }
    }

    fun uploadNote(uri: Uri, uploadPartialNoteBody: UploadNotePartialBody) {
        homePageRepository.uploadNote(uri, uploadPartialNoteBody)
        uploadPDF = homePageRepository.uploadPDF
    }

    fun uploadLecture(title: String, description: String, uri: Uri) {
        homePageRepository.uploadLecture(title, description, uri)
        uploadLectureData = homePageRepository.uploadLectureData
    }

    fun searchQnA(search: String) {
        qnARepository.searchQnA(search)
        getSearchQnAData = qnARepository.getSearchQnAData
        errorGetSearchQnAData = qnARepository.errorGetSearchQnAData
    }
}