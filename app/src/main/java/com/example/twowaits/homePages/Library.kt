package com.example.twowaits.homePages

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.twowaits.R
import com.example.twowaits.databinding.LibraryBinding
import com.example.twowaits.recyclerAdapters.BookmarkedNotesRecyclerAdapter
import com.example.twowaits.recyclerAdapters.WishlistRecyclerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class Library : Fragment() {
    private var _binding: LibraryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LibraryBinding.inflate(inflater, container, false)

        binding.swipeToRefresh.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_library_self)
            }, 440)
        }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_library_to_homePage)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}