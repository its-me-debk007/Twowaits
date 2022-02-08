package com.example.twowaits.homePages

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
import com.example.twowaits.R
import com.example.twowaits.databinding.BookmarkedQuestionsBinding
import com.example.twowaits.databinding.DownloadedNotesBinding
import com.example.twowaits.recyclerAdapters.DownloadedNoteClicked
import com.example.twowaits.recyclerAdapters.DownloadedNotesRecyclerAdapter
import java.io.File

class DownloadedNotes: Fragment(), DownloadedNoteClicked {
    private var _binding: DownloadedNotesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DownloadedNotesBinding.inflate(inflater, container, false)
        val downloadedFilesList = mutableListOf<DownloadedNotesDataClass>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)/* write for else condition too*/ {
            val downloadsFolder = File(
                "${Environment.getStorageDirectory()}/emulated/0/Download/Educool Downloads",
                "Notes")
            if (downloadsFolder.exists()) {
                val downloadedNotesList = downloadsFolder.listFiles()
                for (file in downloadedNotesList) {
                    downloadedFilesList.add(
                        DownloadedNotesDataClass(
                            file.nameWithoutExtension, "description",
                            "author"
                        )
                    )
                }
            }
        }
        binding.DownloadedNotesRecyclerView.adapter = DownloadedNotesRecyclerAdapter(downloadedFilesList, this)
        binding.DownloadedNotesRecyclerView.layoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically(): Boolean = false
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onDownloadedNoteClicked(downloadedNoteName: String) {
        val file: File
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            file = File("${Environment.getStorageDirectory()}/emulated/0/Download/Educool Downloads/Notes/${downloadedNoteName}")
            Data.DOWNLOADED_NOTE = file
        }// Write for else condition too
        Data.PREVIOUS_PAGE = "DOWNLOADS"
        findNavController().navigate(R.id.action_downloads_to_PDFViewer)
    }
}