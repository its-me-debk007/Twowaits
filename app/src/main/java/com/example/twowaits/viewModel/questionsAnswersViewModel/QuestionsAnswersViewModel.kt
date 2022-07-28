package com.example.twowaits.viewModel.questionsAnswersViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twowaits.network.dashboardApiCalls.*
import com.example.twowaits.network.dashboardApiCalls.questionsAnswersApiCalls.AskQuestionResponse
import com.example.twowaits.network.dashboardApiCalls.questionsAnswersApiCalls.BookmarkQuestionResponse
import com.example.twowaits.network.dashboardApiCalls.questionsAnswersApiCalls.LikeAnswerResponse
import com.example.twowaits.model.BookmarkNoteBody
import com.example.twowaits.homePages.questionsAnswers.*
import com.example.twowaits.repository.homeRepository.questionsAnswersRepositories.QnARepository
import com.example.twowaits.sealedClass.Response
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class QuestionsAnswersViewModel: ViewModel() {
    private val qnaRepository = QnARepository()

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
    lateinit var bookmarkNoteData: MutableLiveData<String>
    lateinit var bookmarkedNotesData: MutableLiveData<List<RecentNotesResponse>>
    lateinit var errorBookmarkedNotesData: MutableLiveData<String>
    lateinit var addToWishlistData: MutableLiveData<String>
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

    fun addToWishlist(addToWishlistBody: AddToWishlistBody){
        val repository = QnARepository()
        repository.addToWishlist(addToWishlistBody)
        addToWishlistData = repository.addToWishlistData
    }

    fun getYourBookmarkedQ(){
        val repository = QnARepository()
        repository.getYourBookmarkedQ()
        getBookmarkedQLiveData = repository.getBookmarkedQLiveData
        errorGetBookmarkedQLiveData = repository.errorGetBookmarkedQLiveData
    }

    fun getBookmarkedNotes(){
        val repository = QnARepository()
        repository.getBookmarkedNotes()
        bookmarkedNotesData = repository.bookmarkedNotesData
        errorBookmarkedNotesData = repository.errorBookmarkedNotesData
    }

    lateinit var wishlistLiveData: MutableLiveData<Response<List<RecentLecturesResponse>>>
    fun getWishlist() = viewModelScope.launch {
        wishlistLiveData = qnaRepository.getWishlist()
    }

    lateinit var createAnswerData: MutableLiveData<Response<String>>
    fun createAnswer(createAnswerBody: CreateAnswerBody) = viewModelScope.launch {
        val repository = QnARepository()
        createAnswerData = repository.createAnswer(createAnswerBody)
    }

    lateinit var createCommentData: MutableLiveData<Response<String>>
    fun createComment(createCommentBody: CreateCommentBody) = viewModelScope.launch {
        val repository = QnARepository()
        createCommentData = repository.createComment(createCommentBody)
    }
}