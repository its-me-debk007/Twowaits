package com.example.twowaits.utils

import android.content.Context
import android.os.CountDownTimer
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar

fun String.isValidEmail() = android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPassword(): String? = when {
        this.length < 8 -> "Must contain at least 8 characters"

        !this.matches(".*[A-Z].*".toRegex()) -> "Must contain at least 1 uppercase letter"

        !this.matches(".*[a-z].*".toRegex()) -> "Must contain at least 1 lowercase letter"

        !this.matches(".*[\$#%@&*/+_=?^!].*".toRegex()) -> "Must contain at least 1 special character"

        !this.matches(".*[0-9].*".toRegex()) -> "Must contain at least 1 numeric digit"

        this.contains("123") -> "Must not contain 123"

        else -> null
    }

fun View.snackBar(msg: String) {
    Snackbar.make(this, msg, Snackbar.LENGTH_SHORT).apply {
        setAction("OK") { dismiss() }
        animationMode = Snackbar.ANIMATION_MODE_SLIDE
        show()
    }
}

fun View.hideKeyboard(activity: FragmentActivity?) {
    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

lateinit var timerCountDownTimer: CountDownTimer
private val timeLeftData = MutableLiveData<Int>()
val timeLeftLiveData: LiveData<Int> = timeLeftData
private val timeFinishedData = MutableLiveData<Boolean>()
val timeFinishedLiveData: LiveData<Boolean> = timeFinishedData
var time = 0
fun Int.startTimer() {
    timerCountDownTimer = object : CountDownTimer((this * 60 * 1000).toLong(), 1000) {
        override fun onTick(millisUntilFinished: Long) {
            time++
            timeLeftData.postValue(time)
        }

        override fun onFinish() {
            timeFinishedData.postValue(false)
        }
    }.start()
}