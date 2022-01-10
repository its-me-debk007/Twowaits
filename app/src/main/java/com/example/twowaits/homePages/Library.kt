package com.example.twowaits.homePages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.twowaits.R
import com.example.twowaits.databinding.LibraryBinding
import com.example.twowaits.recyclerAdapters.BookmarkedNotesRecyclerAdapter
import com.example.twowaits.recyclerAdapters.WishlistRecyclerAdapter
import com.example.twowaits.recyclerAdapters.YourQuestionsRecyclerAdapter

class Library : Fragment() {
    private var _binding: LibraryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LibraryBinding.inflate(inflater, container, false)

//        binding.BookmarkedQuestionsRecyclerView.adapter = YourQuestionsRecyclerAdapter()
//        binding.BookmarkedQuestionsRecyclerView.layoutManager = LinearLayoutManager(container?.context)

        binding.BookmarkedNotesRecyclerView.adapter = BookmarkedNotesRecyclerAdapter(4)
        binding.BookmarkedNotesRecyclerView.layoutManager = LinearLayoutManager(container?.context, LinearLayoutManager.HORIZONTAL, false)

        binding.WishlistRecyclerView.adapter = WishlistRecyclerAdapter(4)
        binding.WishlistRecyclerView.layoutManager = LinearLayoutManager(container?.context, LinearLayoutManager.HORIZONTAL, false)

        binding.SubHeading1.setOnClickListener {
            findNavController().navigate(R.id.action_library_to_bookmarkedQuestions)
        }

        binding.MoreBookmarkedQuestions.setOnClickListener {
            findNavController().navigate(R.id.action_library_to_bookmarkedQuestions)
        }

        binding.SubHeading2.setOnClickListener {
            findNavController().navigate(R.id.action_library_to_bookmarkedNotes)
        }

        binding.MoreBookmarkedNotes.setOnClickListener {
            findNavController().navigate(R.id.action_library_to_bookmarkedNotes)
        }

        binding.SubHeading3.setOnClickListener {
            findNavController().navigate(R.id.action_library_to_wishlist)
        }

        binding.MoreWishlistContents.setOnClickListener {
            findNavController().navigate(R.id.action_library_to_wishlist)
        }

        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}