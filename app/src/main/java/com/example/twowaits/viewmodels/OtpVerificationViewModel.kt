package com.example.twowaits.viewmodels

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.twowaits.repository.BaseRepository

class OtpVerificationViewModel(private val repository: BaseRepository): ViewModel() {
    private lateinit var timerCountDownTimer: CountDownTimer
    var timerOnStatusLiveData = MutableLiveData<Boolean>(true)
    val timerLiveData = MutableLiveData<Long>()
    var timerOnStatus = true

    fun startTimer() {
        timerCountDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val timeLeft = millisUntilFinished / 1000
                timerLiveData.postValue(timeLeft)
            }
            override fun onFinish() {
                timerOnStatusLiveData.postValue(false)
                timerOnStatus = false
            }
        }.start()
    }
    fun cancelTimer(){
        timerCountDownTimer.cancel()
    }
    init {
        startTimer()
    }
}