package com.example.twowaits.utils

import android.content.Context
import android.net.Uri
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.example.twowaits.network.ServiceBuilder
import com.example.twowaits.network.dashboardApiCalls.GetNewAccessTokenResponse
import com.example.twowaits.network.dashboardApiCalls.quizApiCalls.GetQuizDataResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {
        var ACCESS_TOKEN: String? = null
        var REFRESH_TOKEN: String? = null
        lateinit var USER: String

        var CURRENT_QUESTION = 0
        var FIRST_TIME = true
        lateinit var QUIZ_DATA: GetQuizDataResponse
        var QUIZ_RESULT_ID = 0
        var CHOSEN_OPTION: MutableMap<Int, Int> = mutableMapOf()
        lateinit var TITLE_OF_QUIZ: String
        lateinit var PDF_URI: String
        lateinit var VIDEO_URI: String
        lateinit var DOWNLOADED_NOTE: File
        lateinit var DOWNLOADED_LECTURE: File
        lateinit var timerCountDownTimer: CountDownTimer
        private val timeLeftData = MutableLiveData<Int>()
        val timeLeftLiveData: LiveData<Int> = timeLeftData

        private val timeFinishedData = MutableLiveData<Boolean>()
        val timeFinishedLiveData: LiveData<Boolean> = timeFinishedData

        var time = 0
        fun startTimer(time_limit: Int) {
            timerCountDownTimer = object : CountDownTimer((time_limit * 60 * 1000).toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    time++
                    timeLeftData.postValue(time)
                }

                override fun onFinish() {
                    timeFinishedData.postValue(false)
                }
            }.start()
        }
    }

    fun formatTime(date: String): String {
        val str = date.substring(0, 19)
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.UK)
        val time = sdf.parse(str).toString()

        val day = time.substring(8, 10)
        val month = time.substring(4, 7)
        val tmp = time.substring(11, 13).toInt()
        val meridian: String
        val hours = if (tmp > 12) {
            meridian = "pm"
            tmp - 12
        } else {
            meridian = "am"
            tmp
        }
        val minutes = time.substring(14, 16)

        return "$hours:$minutes $meridian, $day $month"
    }

    var result = ""
    fun getNewAccessToken(): String {
        val call = ServiceBuilder.getInstance().getNewAccessToken(REFRESH_TOKEN!!)
        call.enqueue(object : Callback<GetNewAccessTokenResponse> {
                override fun onResponse(
                    call: Call<GetNewAccessTokenResponse>,
                    response: Response<GetNewAccessTokenResponse>
                ) {
                    if (response.isSuccessful) {
                        ACCESS_TOKEN = response.body()?.access
                        result = "success"
                    }
                    else result = "failure"
                }

                override fun onFailure(call: Call<GetNewAccessTokenResponse>, t: Throwable) {
                    result = "failure"
                }

            })

        return result
    }

    fun showKeyboard(view: View, activity: FragmentActivity?) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    fun hideKeyboard(view: View, activity: FragmentActivity?) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun downloadImg(context: Context, imgUrl: String, dirPath: String, imgName: String){
        PRDownloader.initialize(context)

        PRDownloader.download(imgUrl, dirPath, imgName)
            .build()
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    Toast.makeText(context, "Downloaded successfully", Toast.LENGTH_SHORT).show()
                }

                override fun onError(p0: com.downloader.Error?) {}
            })
    }
}