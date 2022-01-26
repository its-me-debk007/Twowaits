package com.example.twowaits.homePages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.twowaits.databinding.BookmarkedQuestionsBinding
import com.example.twowaits.databinding.DownloadedNotesBinding
import com.example.twowaits.recyclerAdapters.DownloadedNotesRecyclerAdapter

class DownloadedNotes: Fragment() {
    private var _binding: DownloadedNotesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DownloadedNotesBinding.inflate(inflater, container, false)

        binding.DownloadedNotesRecyclerView.adapter = DownloadedNotesRecyclerAdapter()
        binding.DownloadedNotesRecyclerView.layoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically(): Boolean = false
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}