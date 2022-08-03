package com.example.twowaits.ui.fragment.downloads

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.twowaits.ui.activity.NoteLectureActivity
import com.example.twowaits.R
import com.example.twowaits.databinding.DownloadedNotesBinding
import com.example.twowaits.recyclerAdapters.DownloadedNoteClicked
import com.example.twowaits.recyclerAdapters.DownloadedNotesRecyclerAdapter
import com.example.twowaits.util.Utils
import java.io.File

class DownloadedNotes : Fragment(R.layout.downloaded_notes), DownloadedNoteClicked {
    private lateinit var binding: DownloadedNotesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DownloadedNotesBinding.bind(view)
        val file = File(context?.filesDir, "Downloaded Notes/")
        file.mkdirs()
        if (!file.exists() || file.listFiles().isEmpty()) {
            binding.recyclerView.visibility = View.GONE
            binding.emptyAnimation.visibility = View.VISIBLE
            binding.text.visibility = View.VISIBLE
        } else {
            val fileNames = mutableListOf<String>()
            file.listFiles().forEach {
                fileNames.add(it.name)
            }
            binding.recyclerView.adapter = DownloadedNotesRecyclerAdapter(fileNames, this)
        }
    }

    override fun onDownloadedNoteClicked(downloadedNoteName: String) {
        Utils.DOWNLOADED_NOTE = File(context?.filesDir, "Downloaded Notes/$downloadedNoteName")
        Intent(context, NoteLectureActivity::class.java).apply {
            putExtra("PREVIOUS PAGE", "DOWNLOADS")
            putExtra("PAGE TYPE", "NOTE")
            startActivity(this)
        }
    }
}