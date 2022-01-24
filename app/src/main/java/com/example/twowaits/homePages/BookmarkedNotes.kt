package com.example.twowaits.homePages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.twowaits.databinding.BookmarkedNotesBinding
import com.example.twowaits.recyclerAdapters.BookmarkedNotesRecyclerAdapter
import com.example.twowaits.recyclerAdapters.WishlistRecyclerAdapter

class BookmarkedNotes: Fragment() {
    private var _binding: BookmarkedNotesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BookmarkedNotesBinding.inflate(inflater, container, false)

        binding.BookmarkedNotesRecyclerView.adapter = BookmarkedNotesRecyclerAdapter(1)
        binding.BookmarkedNotesRecyclerView.layoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically(): Boolean = false
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}