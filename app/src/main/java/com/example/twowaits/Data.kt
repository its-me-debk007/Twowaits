package com.example.twowaits

import android.app.Application
import android.net.Uri
import android.os.CountDownTimer
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.apiCalls.dashboardApiCalls.quizApiCalls.GetQuizDataResponse
import kotlinx.coroutines.flow.first
import java.io.File

class Data : Application() {
    companion object {
        lateinit var REFRESH_TOKEN: String
        var ACCESS_TOKEN: String? = null
        lateinit var EMAIL: String
        lateinit var USER: String
        var CURRENT_QUESTION = 0
        var FIRST_TIME = true
        lateinit var QUIZ_DATA: GetQuizDataResponse
        var QUIZ_RESULT_ID = 0
        var CHOSEN_OPTION: MutableMap<Int, Int> = mutableMapOf()
        lateinit var TITLE_OF_QUIZ: String
        var isSearchBarOpen = false
        lateinit var PDF_URI: Uri
        lateinit var VIDEO_URI: Uri
        lateinit var DOWNLOADED_NOTE: File
        lateinit var DOWNLOADED_LECTURE: File

        var dataStore: DataStore<Preferences>? = null
        suspend fun saveData(key: String, value: String) {
            val dataStoreKey = preferencesKey<String>(key)
            dataStore?.edit {
                it[dataStoreKey] = value
            }
        }

        suspend fun readData(key: String): String? {
            val dataStoreKey = preferencesKey<String>(key)
            val preferences = dataStore?.data?.first()
            return preferences?.get(dataStoreKey)
        }

        fun properTimeFormat(
            monthNumber: String,
            day: String,
            hours: String,
            minutes: String
        ): String {
            val monthName = when (monthNumber) {
                "01" -> "Jan"
                "02" -> "Feb"
                "03" -> "Mar"
                "04" -> "Apr"
                "05" -> "May"
                "06" -> "June"
                "07" -> "July"
                "08" -> "Aug"
                "09" -> "Sept"
                "10" -> "Oct"
                "11" -> "Nov"
                else -> "Dec"
            }
            val hour: Int
            val meridian: String
            when {
                (hours.toInt() + 5 in 13..24) -> {
                    hour = hours.toInt() + 5 - 12
                    meridian = "pm"
                }
                (hours.toInt() + 5 in 25..28) -> {
                    hour = hours.toInt() + 5 - 24
                    meridian = "am"
                }
                else -> {
                    hour = hours.toInt() + 5 - 24
                    meridian = "am"
                }
            }
            return "$hour:$minutes $meridian, $day $monthName"
        }

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

        var TIME_LIMIT = 0
        val isSearchBarActiveLiveData = MutableLiveData<Boolean>()
        var Q_SEARCHED: String? = null
    }

    fun getNewAccessToken(): String {
        val result: String
        val response = RetrofitClient.getInstance().getNewAccessToken(REFRESH_TOKEN).execute()
        if (response.isSuccessful) {
            ACCESS_TOKEN = response.body()?.access
            result = "success"
        } else {
            result = "failure"
        }
        return result
    }
}