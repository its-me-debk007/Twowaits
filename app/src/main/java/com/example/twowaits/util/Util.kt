package com.example.twowaits.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.example.twowaits.network.dashboardApiCalls.quizApiCalls.GetQuizDataResponse
import java.text.SimpleDateFormat
import java.util.*

var ACCESS_TOKEN: String? = null
var REFRESH_TOKEN: String? = null
lateinit var USER: String
lateinit var QUIZ_DATA : GetQuizDataResponse
var FIRST_TIME = true

fun formatTime(date: String): String {
    val str = date.substring(0, 19)
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.UK)
    val time = sdf.parse(str)!!.toString()

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

fun Activity.hideSystemUi(view: View) {
    WindowCompat.setDecorFitsSystemWindows(this.window, false)
    WindowInsetsControllerCompat(
        this.window,
        view
    ).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

fun downloadImg(context: Context, imgUrl: String, dirPath: String, imgName: String,
                listener: DownloaderInterface) {
    PRDownloader.initialize(context)

    PRDownloader.download(imgUrl, dirPath, imgName).build()
        .start(object : OnDownloadListener {
            override fun onDownloadComplete() {
                Toast.makeText(context, "Downloaded successfully", Toast.LENGTH_SHORT).show()
            }

            override fun onError(p0: com.downloader.Error?) {
                Toast.makeText(context, "Some error has occurred! Please try again", Toast.LENGTH_SHORT).show()
                listener.onDownloadError()
            }
        })
}

interface DownloaderInterface {
    fun onDownloadError()
}