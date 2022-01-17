package com.example.twowaits.homePages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.twowaits.databinding.DownloadedLecturesBinding
import com.example.twowaits.recyclerAdapters.DownloadedLecturesRecyclerAdapter

class DownloadedLectures: Fragment() {
    private var _binding: DownloadedLecturesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DownloadedLecturesBinding.inflate(inflater, container, false)

        binding.DownloadedLecturesRecyclerView.adapter = DownloadedLecturesRecyclerAdapter()
        binding.DownloadedLecturesRecyclerView.layoutManager = LinearLayoutManager(container?.context)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}