package com.example.twowaits

import android.os.CountDownTimer
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.apiCalls.dashboardApiCalls.quizApiCalls.GetQuizDataResponse
import kotlinx.coroutines.flow.first

class CompanionObjects {
    companion object{
        lateinit var EMAIL: String
        lateinit var PREVIOUS_PAGE: String
        var ACCESS_TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoicmVmcmVzaCIsImV4cCI6MTY0NDg1MTgwOCwiaWF0IjoxNjQyMjU5ODA4LCJqdGkiOiJhYmZkYzY1ZWI2NTQ0MzYzOWEzNDMzMTQwODk2Y2FlNyIsInVzZXJfaWQiOjN9.suLx_Q6jp5viw9MP2PwEM2w5Mq2so7k24fpy2MNMLHM"
        lateinit var REFRESH_TOKEN: String
        var QUIZ_ID = 0
        var QUESTIONS_LEFT = -1
        var CURRENT_QUESTION = 0
        var FIRST_TIME = true
        lateinit var QUIZ_DATA: GetQuizDataResponse
        var QUIZ_RESULT_ID = 0
        var CHOSEN_OPTION: MutableMap<Int, Int> = mutableMapOf<Int, Int>()

        var dataStore: DataStore<Preferences>? = null
        suspend fun saveLoginStatus(key: String, value: String){
            val dataStoreKey = preferencesKey<String>(key)
            dataStore?.edit {
                it[dataStoreKey] = value
            }
        }
        suspend fun readLoginStatus(key:String): String?{
            val dataStoreKey = preferencesKey<String>(key)
            val preferences = dataStore?.data?.first()
            return preferences?.get(dataStoreKey)
        }
        suspend fun saveAccessToken(key: String, value: String){
            val dataStoreKey = preferencesKey<String>(key)
            dataStore?.edit {
                it[dataStoreKey] = value
            }
        }
        suspend fun readAccessToken(key:String): String?{
            val dataStoreKey = preferencesKey<String>(key)
            val preferences = dataStore?.data?.first()
            return preferences?.get(dataStoreKey)
        }
        suspend fun saveRefreshToken(key: String, value: String){
            val dataStoreKey = preferencesKey<String>(key)
            dataStore?.edit {
                it[dataStoreKey] = value
            }
        }
        suspend fun readRefreshToken(key:String): String?{
            val dataStoreKey = preferencesKey<String>(key)
            val preferences = dataStore?.data?.first()
            return preferences?.get(dataStoreKey)
        }

        fun properTimeFormat(monthNumber: String, day:String, hours: String, minutes: String): String {
            val monthName = when(monthNumber){
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
            when{
                (hours.toInt() + 5 in 13..24) -> {
                    hour = hours.toInt() + 5 - 12
                    meridian = "pm"
                }
                (hours.toInt() + 5 in 25..28)-> {
                    hour = hours.toInt() + 5 - 24
                    meridian = "am"
                }
                else -> {
                    hour = hours.toInt() + 5 - 24
                    meridian = "am"
                }
            }
            return "Answered at $hour:$minutes $meridian, $day $monthName"
        }

        lateinit var timerCountDownTimer: CountDownTimer

        private val timeLeftData = MutableLiveData<Long>()
        val timeLeftLiveData: LiveData<Long> = timeLeftData

        private val timeFinishedData = MutableLiveData<Boolean>()
        val timeFinishedLiveData: LiveData<Boolean> = timeFinishedData

        fun startTimer(time_limit: Int ) {
            timerCountDownTimer = object : CountDownTimer((time_limit * 60 * 1000).toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val timeLeft = millisUntilFinished / 1000
                    timeLeftData.postValue(timeLeft)
                }
                override fun onFinish() {
                    timeFinishedData.postValue(false)
                }
            }.start()
        }
    }
}