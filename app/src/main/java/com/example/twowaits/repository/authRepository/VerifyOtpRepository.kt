package com.example.twowaits.repository.authRepository

import androidx.lifecycle.MutableLiveData
import com.example.twowaits.network.ServiceBuilder
import com.example.twowaits.ui.fragment.auth.VerifyOtpBody
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class VerifyOtpRepository {
    val errorMutableLiveData = MutableLiveData<String>()

    fun verifyOtp(verifyOtpBody: VerifyOtpBody) {
        GlobalScope.launch {
            val response = ServiceBuilder.getInstance().verifyOtp(verifyOtpBody)
            when {
                response.isSuccessful -> {
                    errorMutableLiveData.postValue("success")
                }
                response.code() == 400 -> errorMutableLiveData.postValue("OTP is incorrect\nPlease try again")
                else -> {
                    errorMutableLiveData.postValue("${response.message()}\nPlease Try again")
                }
            }
        }
    }
}