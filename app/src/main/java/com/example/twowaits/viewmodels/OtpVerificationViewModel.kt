package com.example.twowaits.viewmodels

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.twowaits.apiCalls.API
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.repository.BaseRepository
import com.example.twowaits.repository.SendOtpRepository
import com.example.twowaits.repository.VerifyOtpRepository

class OtpVerificationViewModel(): ViewModel() {
    private lateinit var timerCountDownTimer: CountDownTimer
    var timerOnStatusLiveData = MutableLiveData<Boolean>(true)
    val timerLiveData = MutableLiveData<Long>()
    var timerOnStatus = true
    lateinit var verifyOtpLiveData: LiveData<String>

    private val api = RetrofitClient.getInstance().create(API::class.java)
    private val repository = SendOtpRepository(api)
    private val repository2 = VerifyOtpRepository(api)

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
    fun sendOtp(email: String){
//        viewModelScope.launch(Dispatchers.IO) {
        repository.sendOtp(email)
//        }
    }
    fun verifyOtp(email: String, otp: String){
//        viewModelScope.launch(Dispatchers.IO) {
        repository2.verifyOtp(email, otp)
//        }
        verifyOtpLiveData = repository2.errorMutableLiveData
    }
    init {
        startTimer()
    }
}