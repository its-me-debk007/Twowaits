package com.example.twowaits.homePages

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.twowaits.Data
import com.example.twowaits.NoteLectureActivity
import com.example.twowaits.R
import com.example.twowaits.databinding.DownloadedNotesBinding
import com.example.twowaits.recyclerAdapters.DownloadedNoteClicked
import com.example.twowaits.recyclerAdapters.DownloadedNotesRecyclerAdapter
import java.io.File

class DownloadedNotes : Fragment(R.layout.downloaded_notes), DownloadedNoteClicked {
    private lateinit var binding: DownloadedNotesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DownloadedNotesBinding.bind(view)
        val downloadedFilesList = findDownloadedNotes()
        if (downloadedFilesList.isEmpty()) {
            binding.DownloadedNotesRecyclerView.visibility = View.GONE
            binding.emptyAnimation.visibility = View.VISIBLE
            binding.text.visibility = View.VISIBLE
        } else {
            binding.DownloadedNotesRecyclerView.adapter =
                DownloadedNotesRecyclerAdapter(downloadedFilesList, this)
            binding.DownloadedNotesRecyclerView.isNestedScrollingEnabled = false
        }
    }

    private fun findDownloadedNotes(): MutableList<DownloadedNotesDataClass> {
        val downloadedFilesList = mutableListOf<DownloadedNotesDataClass>()
        val downloadsFolder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            File(
                "${Environment.getStorageDirectory()}/emulated/0/Download/Educool Downloads/",
                "Notes"
            )
        else
            File(
                "${Environment.getExternalStorageDirectory()}/Download/Educool Downloads/",
                "Notes"
            )
        if (downloadsFolder.exists()) {
            val downloadedNotesList = downloadsFolder.listFiles()
            Log.e("eeee", downloadedNotesList.size.toString())
            for (file in downloadedNotesList) {
                downloadedFilesList.add(
                    DownloadedNotesDataClass(
                        file.nameWithoutExtension, "description", "author"
                    )
                )
            }
        }
        return downloadedFilesList
    }

    override fun onDownloadedNoteClicked(downloadedNoteName: String) {
        val file = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) File(
            "${Environment.getStorageDirectory()}/emulated/0/Download/Educool Downloads/Notes/${downloadedNoteName}"
        )
        else File("${Environment.getExternalStorageDirectory()}/Download/Educool Downloads/Notes/${downloadedNoteName}")
        Data.DOWNLOADED_NOTE = file
        val intent = Intent(context, NoteLectureActivity::class.java)
        intent.putExtra("PREVIOUS PAGE", "DOWNLOADS")
        intent.putExtra("PAGE TYPE", "NOTE")
        startActivity(intent)
    }
}