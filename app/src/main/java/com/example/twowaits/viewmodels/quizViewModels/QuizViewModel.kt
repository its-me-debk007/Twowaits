package com.example.twowaits.viewmodels.quizViewModels

import android.os.CountDownTimer
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.twowaits.CompanionObjects
import com.example.twowaits.R
import com.example.twowaits.apiCalls.dashboardApiCalls.quizApiCalls.*
import com.example.twowaits.homePages.quiz.AttemptQuizBody
import com.example.twowaits.repository.dashboardRepositories.quizRepositories.AttemptQuizRepository
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class QuizViewModel: ViewModel() {
    lateinit var getQuizLiveData: LiveData<GetQuizDataResponse>
    lateinit var errorGetQuizLiveData: LiveData<String>

    lateinit var attemptQuizLiveData: LiveData<AttemptQuizResponse>
    lateinit var errorAttemptQuizLiveData: LiveData<String>

    lateinit var registerResponseLiveData: LiveData<RegisterOptionsResponse>
    lateinit var errorRegisterResponseLiveData: LiveData<String>

    lateinit var viewScoreLiveData: LiveData<ViewScoreResponse>
    lateinit var errorViewScoreLiveData: LiveData<String>

    fun getQuizData(attemptQuizBody: AttemptQuizBody){
        val repository = AttemptQuizRepository()
        repository.getQuizData(attemptQuizBody)
        getQuizLiveData = repository.getQuizLiveData
        errorGetQuizLiveData = repository.errorGetQuizLiveData
    }

    fun attemptQuiz(attemptQuizBody: AttemptQuizBody){
        val repository = AttemptQuizRepository()
        repository.attemptQuiz(attemptQuizBody)
        attemptQuizLiveData = repository.attemptQuizLiveData
        errorAttemptQuizLiveData = repository.errorAttemptQuizLiveData
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
}