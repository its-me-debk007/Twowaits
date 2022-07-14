package com.example.twowaits.ui.fragment.downloads

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.twowaits.NoteLectureActivity
import com.example.twowaits.R
import com.example.twowaits.databinding.DownloadedLecturesBinding
import com.example.twowaits.recyclerAdapters.DownloadedLectureClicked
import com.example.twowaits.recyclerAdapters.DownloadedLecturesRecyclerAdapter
import com.example.twowaits.utils.Utils
import java.io.File

class DownloadedLectures : Fragment(R.layout.downloaded_lectures), DownloadedLectureClicked {
    private lateinit var binding: DownloadedLecturesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DownloadedLecturesBinding.bind(view)
        val file = File(context?.filesDir, "Downloaded Lectures/")
        if (!file.exists()) {
            binding.DownloadedLecturesRecyclerView.visibility = View.GONE
            binding.emptyAnimation.visibility = View.VISIBLE
            binding.text.visibility = View.VISIBLE
        } else {
            val fileNames = mutableListOf<String>()
            file.listFiles().forEach {
                fileNames.add(it.name)
            }
            binding.DownloadedLecturesRecyclerView.adapter =
                DownloadedLecturesRecyclerAdapter(fileNames, this)
            binding.DownloadedLecturesRecyclerView.isNestedScrollingEnabled = false
        }
    }

    override fun onDownloadedLectureClicked(downloadedLectureName: String) {
        Utils.DOWNLOADED_LECTURE =
            File(context?.filesDir, "Downloaded Lectures/$downloadedLectureName")
        val intent = Intent(context, NoteLectureActivity::class.java).apply {
            putExtra("PREVIOUS PAGE", "DOWNLOADS")
            putExtra("PAGE TYPE", "LECTURE")
        }
        startActivity(intent)
    }
}