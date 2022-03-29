package com.example.twowaits.homePages

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.twowaits.Data
import com.example.twowaits.NoteLectureActivity
import com.example.twowaits.R
import com.example.twowaits.databinding.DownloadedLecturesBinding
import com.example.twowaits.recyclerAdapters.DownloadedLectureClicked
import com.example.twowaits.recyclerAdapters.DownloadedLecturesRecyclerAdapter
import com.example.twowaits.recyclerAdapters.DownloadedNotesRecyclerAdapter
import java.io.File

class DownloadedLectures: Fragment(R.layout.downloaded_lectures), DownloadedLectureClicked {
    private lateinit var binding: DownloadedLecturesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DownloadedLecturesBinding.bind(view)
        val downloadedFilesList = findDownloadedLectures()
        if (downloadedFilesList.isEmpty()) {
            binding.DownloadedLecturesRecyclerView.visibility = View.GONE
            binding.emptyAnimation.visibility = View.VISIBLE
            binding.text.visibility = View.VISIBLE
        } else {
            binding.DownloadedLecturesRecyclerView.adapter =
                DownloadedLecturesRecyclerAdapter(downloadedFilesList, this)
            binding.DownloadedLecturesRecyclerView.isNestedScrollingEnabled = false
        }
    }

    private fun findDownloadedLectures(): MutableList<DownloadedNotesDataClass> {
        val downloadedFilesList = mutableListOf<DownloadedNotesDataClass>()
        val downloadsFolder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            File("${Environment.getStorageDirectory()}/emulated/0/Download/Educool Downloads/", "Lectures")
        else File("${Environment.DIRECTORY_DOWNLOADS}/Educool Downloads/", "Lectures")
//        else File("${Environment.DIRECTORY_DOWNLOADS}/Educool Downloads/", "Lectures")
        Log.e("eeee", downloadsFolder.absolutePath)
        if (downloadsFolder.exists()) {
            val downloadedLecturesList = downloadsFolder.listFiles()
            for (file in downloadedLecturesList) {
                downloadedFilesList.add(DownloadedNotesDataClass(
                    file.nameWithoutExtension, "description",
                    "author"))
            }
        }
        return downloadedFilesList
    }

    override fun onDownloadedLectureClicked(downloadedLectureName: String) {
        val file = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) File("${Environment.getStorageDirectory()}/emulated/0/Download/Educool Downloads/Lectures/${downloadedLectureName}")
            else File("${Environment.getExternalStorageDirectory()}/Download/Educool Downloads/Lectures/${downloadedLectureName}")

        Data.DOWNLOADED_LECTURE = file
        val intent = Intent(context, NoteLectureActivity::class.java)
        intent.putExtra("PREVIOUS PAGE", "DOWNLOADS")
        intent.putExtra("PAGE TYPE", "LECTURE")
        startActivity(intent)
    }
}