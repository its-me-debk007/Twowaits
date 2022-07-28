package com.example.twowaits.viewModel.questionsAnswersViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twowaits.homePages.questionsAnswers.*
import com.example.twowaits.model.BookmarkNoteBody
import com.example.twowaits.network.dashboardApiCalls.AddToWishlistBody
import com.example.twowaits.network.dashboardApiCalls.QnAResponseItem
import com.example.twowaits.network.dashboardApiCalls.RecentLecturesResponse
import com.example.twowaits.network.dashboardApiCalls.RecentNotesResponse
import com.example.twowaits.network.dashboardApiCalls.questionsAnswersApiCalls.AskQuestionResponse
import com.example.twowaits.network.dashboardApiCalls.questionsAnswersApiCalls.BookmarkQuestionResponse
import com.example.twowaits.network.dashboardApiCalls.questionsAnswersApiCalls.LikeAnswerResponse
import com.example.twowaits.repository.homeRepository.questionsAnswersRepositories.QnARepository
import com.example.twowaits.sealedClass.Response
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class QuestionsAnswersViewModel : ViewModel() {
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

    fun getQnA() {
        qnaRepository.getYourQnA()
        q_n_aLiveData = qnaRepository.q_n_aLiveData
        errorLiveData = qnaRepository.errorLiveData
    }

    fun askQuestion(askQuestionBody: AskQuestionBody) {
        qnaRepository.askQuestion(askQuestionBody)
        askQuestionLiveData = qnaRepository.askQuestionLiveData
        errorAskQuestionLiveData = qnaRepository.errorAskQuestionLiveData
    }

    fun likeAnswer(likeAnswerBody: LikeAnswerBody) {
        qnaRepository.likeAnswer(likeAnswerBody)
        likeAnswerLiveData = qnaRepository.likeAnswerLiveData
        errorLikeAnswerLiveData = qnaRepository.errorLikeAnswerLiveData
    }

    fun bookmarkQuestion(bookmarkQuestionBody: BookmarkQuestionBody) {
        qnaRepository.bookmarkQuestion(bookmarkQuestionBody)
        bookmarkQuestionLiveData = qnaRepository.bookmarkQuestionLiveData
        errorBookmarkQuestionLiveData = qnaRepository.errorBookmarkQuestionLiveData
    }

    fun bookmarkNote(bookmarkNoteBody: BookmarkNoteBody) {
        qnaRepository.bookmarkNote(bookmarkNoteBody)
        bookmarkNoteData = qnaRepository.bookmarkNoteData
    }

    fun addToWishlist(addToWishlistBody: AddToWishlistBody) {
        qnaRepository.addToWishlist(addToWishlistBody)
        addToWishlistData = qnaRepository.addToWishlistData
    }

    fun getYourBookmarkedQ() {
        qnaRepository.getYourBookmarkedQ()
        getBookmarkedQLiveData = qnaRepository.getBookmarkedQLiveData
        errorGetBookmarkedQLiveData = qnaRepository.errorGetBookmarkedQLiveData
    }

    fun getBookmarkedNotes() {
        qnaRepository.getBookmarkedNotes()
        bookmarkedNotesData = qnaRepository.bookmarkedNotesData
        errorBookmarkedNotesData = qnaRepository.errorBookmarkedNotesData
    }

    lateinit var wishlistLiveData: MutableLiveData<Response<List<RecentLecturesResponse>>>
    fun getWishlist() = viewModelScope.launch {
        wishlistLiveData = qnaRepository.getWishlist()
    }

    lateinit var createAnswerData: MutableLiveData<Response<String>>
    fun createAnswer(createAnswerBody: CreateAnswerBody) = viewModelScope.launch {
        createAnswerData = qnaRepository.createAnswer(createAnswerBody)
    }

    lateinit var createCommentData: MutableLiveData<Response<String>>
    fun createComment(createCommentBody: CreateCommentBody) = viewModelScope.launch {
        createCommentData = qnaRepository.createComment(createCommentBody)
    }
}