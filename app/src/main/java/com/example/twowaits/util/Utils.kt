package com.example.twowaits.util

import android.app.Application
import android.util.Log
import com.example.twowaits.network.ServiceBuilder
import com.example.twowaits.network.dashboardApiCalls.quizApiCalls.GetQuizDataResponse
import com.example.twowaits.ui.activity.home.HomeActivity
import kotlinx.coroutines.runBlocking
import java.io.File

class Utils : Application() {
    companion object {
        var CURRENT_QUESTION = 0
        var QUIZ_RESULT_ID = 0
        var CHOSEN_OPTION: MutableMap<Int, Int> = mutableMapOf()
        lateinit var PDF_URI: String
        lateinit var VIDEO_URI: String
        lateinit var DOWNLOADED_NOTE: File
        lateinit var DOWNLOADED_LECTURE: File
    }

    fun getNewAccessToken() {
        runBlocking {
            try {
                val response = ServiceBuilder.getInstance()
                    .generateNewToken(REFRESH_TOKEN!!)
                ACCESS_TOKEN = response.body()?.access
                Datastore(HomeActivity()).saveAccessToken(ACCESS_TOKEN!!)
            } catch (e: Exception) {
                Log.e("NEW_TOKEN_ERROR", e.message.toString())
            }
        }
    }
}