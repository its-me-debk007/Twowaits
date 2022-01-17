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
import com.google.android.material.tabs.TabLayoutMediator

class Library : Fragment() {
    private var _binding: LibraryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LibraryBinding.inflate(inflater, container, false)

        val viewPagerAdapter = LibraryViewPagerAdapter(childFragmentManager, lifecycle)
        binding.LibraryViewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.TabLayout, binding.LibraryViewPager){tab, position ->
            when (position){
                0 -> tab.text = "Q & A’s"
                1 -> tab.text = "Notes"
            }
        }.attach()

        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}