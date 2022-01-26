package com.example.twowaits.repository.authRepositories

import androidx.lifecycle.MutableLiveData
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.apiCalls.authApiCalls.VerifyOtpResponse
import com.example.twowaits.authPages.VerifyOtpBody
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@DelicateCoroutinesApi
class VerifyOtpRepository {
    val errorMutableLiveData = MutableLiveData<String>()

//    fun verifyOtp(email: String, otp: String){
//        RetrofitClient.getInstance().verifyOtp(email, otp).enqueue(object :
//            Callback<VerifyOtpResponse?> {
//            override fun onResponse(
//                call: Call<VerifyOtpResponse?>,
//                response: Response<VerifyOtpResponse?>
//            ) {
//                when {
//                    response.isSuccessful -> {
//                        errorMutableLiveData.postValue("success")
//                    }
//                    response.code() == 406 -> {
//                        errorMutableLiveData.postValue("OTP expired! Please tap on Resend OTP button to get a new OTP")
//                    }
//                    response.code() == 400 -> {
//                        errorMutableLiveData.postValue("Wrong OTP!")
//                    }
//                }
//            }
//            override fun onFailure(call: Call<VerifyOtpResponse?>, t: Throwable) {
//                errorMutableLiveData.postValue(t.message)
//            }
//        })
    fun verifyOtp(verifyOtpBody: VerifyOtpBody) {
    GlobalScope.launch {
        val response = RetrofitClient.getInstance().verifyOtp(verifyOtpBody)
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