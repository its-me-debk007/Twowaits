package com.example.twowaits.homePages

import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.twowaits.Data
import com.example.twowaits.R
import com.example.twowaits.databinding.DownloadedLecturesBinding
import com.example.twowaits.recyclerAdapters.DownloadedLectureClicked
import com.example.twowaits.recyclerAdapters.DownloadedLecturesRecyclerAdapter
import com.example.twowaits.recyclerAdapters.DownloadedNotesRecyclerAdapter
import java.io.File

class DownloadedLectures: Fragment(), DownloadedLectureClicked {
    private var _binding: DownloadedLecturesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DownloadedLecturesBinding.inflate(inflater, container, false)

        val downloadedFilesList = mutableListOf<DownloadedNotesDataClass>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)/* write for else condition too*/ {
            val downloadsFolder = File(
                "${Environment.getStorageDirectory()}/emulated/0/Download/Educool Downloads",
                "Lectures")
            if (downloadsFolder.exists()) {
                val downloadedLecturesList = downloadsFolder.listFiles()
                for (file in downloadedLecturesList) {
                    downloadedFilesList.add(
                        DownloadedNotesDataClass(
                            file.nameWithoutExtension, "description",
                            "author"
                        )
                    )
                }
            }
        }
        binding.DownloadedLecturesRecyclerView.adapter = DownloadedLecturesRecyclerAdapter(downloadedFilesList, this)
        binding.DownloadedLecturesRecyclerView.layoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically(): Boolean = false
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onDownloadedLectureClicked(downloadedLectureName: String) {
        val file: File
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            file = File("${Environment.getStorageDirectory()}/emulated/0/Download/Educool Downloads/Lectures/${downloadedLectureName}")
            Data.DOWNLOADED_LECTURE = file
        }// Write for else condition too
        Data.PREV_PAGE_FOR_PLAYER = "DOWNLOADS"
        findNavController().navigate(R.id.action_downloads_to_videoPlayer2)
    }
}