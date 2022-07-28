package com.example.twowaits.util

import android.content.Context
import android.widget.Toast
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import java.text.SimpleDateFormat
import java.util.*

var ACCESS_TOKEN: String? = null
var REFRESH_TOKEN: String? = null
lateinit var USER: String

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

fun downloadImg(context: Context, imgUrl: String, dirPath: String, imgName: String) {
    PRDownloader.initialize(context)

    PRDownloader.download(imgUrl, dirPath, imgName).build()
        .start(object : OnDownloadListener {
            override fun onDownloadComplete() {
                Toast.makeText(context, "Downloaded successfully", Toast.LENGTH_SHORT).show()
            }

            override fun onError(p0: com.downloader.Error?) {}
        })
}