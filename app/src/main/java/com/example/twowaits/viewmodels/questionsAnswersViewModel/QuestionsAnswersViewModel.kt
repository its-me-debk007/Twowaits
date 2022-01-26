package com.example.twowaits.viewmodels.questionsAnswersViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.twowaits.apiCalls.dashboardApiCalls.QnAResponseItem
import com.example.twowaits.apiCalls.dashboardApiCalls.questionsAnswersApiCalls.AskQuestionResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.questionsAnswersApiCalls.BookmarkQuestionResponse
import com.example.twowaits.apiCalls.dashboardApiCalls.questionsAnswersApiCalls.LikeAnswerResponse
import com.example.twowaits.homePages.BookmarkNoteBody
import com.example.twowaits.homePages.questionsAnswers.*
import com.example.twowaits.repository.dashboardRepositories.questionsAnswersRepositories.QnARepository
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class QuestionsAnswersViewModel: ViewModel() {
    lateinit var q_n_aLiveData: LiveData<List<QnAResponseItem>>
    lateinit var errorLiveData: LiveData<String>
    lateinit var askQuestionLiveData: LiveData<AskQuestionResponse>
    lateinit var errorAskQuestionLiveData: LiveData<String>
    lateinit var likeAnswerLiveData: LiveData<LikeAnswerResponse>
    lateinit var errorLikeAnswerLiveData: LiveData<String>
    lateinit var bookmarkQuestionLiveData: LiveData<BookmarkQuestionResponse>
    lateinit var errorBookmarkQuestionLiveData: LiveData<String>
    lateinit var getBookmarkedQLiveData: LiveData<List<QnAResponseItem>>
    lateinit var errorGetBookmarkedQLiveData: LiveData<String>
    lateinit var createAnswerData: MutableLiveData<String>
    lateinit var createCommentData: MutableLiveData<String>
    lateinit var bookmarkNoteData: MutableLiveData<String>
    var isClicked = false

    fun getQnA() {
        val repository = QnARepository()
        repository.getYourQnA()
        q_n_aLiveData = repository.q_n_aLiveData
        errorLiveData = repository.errorLiveData
    }

    fun askQuestion(askQuestionBody: AskQuestionBody){
        val repository = QnARepository()
        repository.askQuestion(askQuestionBody)
        askQuestionLiveData = repository.askQuestionLiveData
        errorAskQuestionLiveData = repository.errorAskQuestionLiveData
    }

    fun likeAnswer(likeAnswerBody: LikeAnswerBody){
        val repository = QnARepository()
        repository.likeAnswer(likeAnswerBody)
        likeAnswerLiveData = repository.likeAnswerLiveData
        errorLikeAnswerLiveData = repository.errorLikeAnswerLiveData
    }

    fun bookmarkQuestion(bookmarkQuestionBody: BookmarkQuestionBody){
        val repository = QnARepository()
        repository.bookmarkQuestion(bookmarkQuestionBody)
        bookmarkQuestionLiveData = repository.bookmarkQuestionLiveData
        errorBookmarkQuestionLiveData = repository.errorBookmarkQuestionLiveData
    }

    fun bookmarkNote(bookmarkNoteBody: BookmarkNoteBody){
        val repository = QnARepository()
        repository.bookmarkNote(bookmarkNoteBody)
        bookmarkNoteData = repository.bookmarkNoteData
    }

    fun getYourBookmarkedQ(){
        val repository = QnARepository()
        repository.getYourBookmarkedQ()
        getBookmarkedQLiveData = repository.getBookmarkedQLiveData
        errorGetBookmarkedQLiveData = repository.errorGetBookmarkedQLiveData
    }

    fun createAnswer(createAnswerBody: CreateAnswerBody){
        val repository = QnARepository()
        repository.createAnswer(createAnswerBody)
        createAnswerData = repository.createAnswerData
    }

    fun createComment(createCommentBody: CreateCommentBody){
        val repository = QnARepository()
        repository.createComment(createCommentBody)
        createCommentData = repository.createCommentData
    }
}