package com.example.twowaits.viewModel.quizViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twowaits.network.dashboardApiCalls.quizApiCalls.*
import com.example.twowaits.model.AttemptQuizBody
import com.example.twowaits.repository.homeRepository.quizRepositories.AttemptQuizRepository
import com.example.twowaits.sealedClass.Response
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class QuizViewModel: ViewModel() {
    val repository = AttemptQuizRepository()


    lateinit var registerResponseLiveData: LiveData<RegisterOptionsResponse>
    lateinit var errorRegisterResponseLiveData: LiveData<String>
    lateinit var viewScoreLiveData: LiveData<ViewScoreResponse>
    lateinit var errorViewScoreLiveData: LiveData<String>
    lateinit var detailedQuizScoreData: MutableLiveData<DetailedQuizResultResponse>
    lateinit var errorDetailedQuizScoreData: MutableLiveData<String>

    lateinit var quizLiveData : MutableLiveData<Response<GetQuizDataResponse>>
    fun getQuizData(attemptQuizBody: AttemptQuizBody) = viewModelScope.launch {
        quizLiveData = repository.getQuizData(attemptQuizBody)
    }

    lateinit var attemptQuizLiveData : MutableLiveData<Response<AttemptQuizResponse>>
    fun attemptQuiz(attemptQuizBody: AttemptQuizBody) = viewModelScope.launch {
        attemptQuizLiveData = repository.attemptQuiz(attemptQuizBody)
    }

    fun registerResponse(registerResponseBody: RegisterResponseBody){
        val repository = AttemptQuizRepository()
        repository.registerResponse(registerResponseBody)
        registerResponseLiveData = repository.registerResponseLiveData
        errorRegisterResponseLiveData = repository.errorRegisterResponseLiveData
    }

    fun viewScore(attemptQuizBody: AttemptQuizBody){
        val repository = AttemptQuizRepository()
        repository.viewScore(attemptQuizBody)
        viewScoreLiveData = repository.viewScoreLiveData
        errorViewScoreLiveData = repository.errorViewScoreLiveData
    }

    fun detailedQuizResult(id: Int) {
        val repository = AttemptQuizRepository()
        repository.detailedQuizResult(id)
        detailedQuizScoreData = repository.detailedQuizScoreData
        errorDetailedQuizScoreData = repository.errorDetailedQuizScoreData
    }
}