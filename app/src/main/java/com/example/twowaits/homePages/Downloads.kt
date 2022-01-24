package com.example.twowaits.homePages

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.twowaits.R
import com.example.twowaits.databinding.DownloadsBinding
import com.google.android.material.tabs.TabLayoutMediator

class Downloads : Fragment() {
    private var _binding: DownloadsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DownloadsBinding.inflate(inflater, container, false)

        binding.swipeToRefresh.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_downloads_self)
            }, 440)
        }

//        binding.DownloadedNotesRecyclerView.adapter = DownloadedNotesRecyclerAdapter()
//        binding.DownloadedNotesRecyclerView.layoutManager = LinearLayoutManager(container?.context)

//        binding.DownloadedLecturesRecyclerView.adapter = DownloadedLecturesRecyclerAdapter()
//        binding.DownloadedLecturesRecyclerView.layoutManager = LinearLayoutManager(container?.context)

        val viewPagerAdapter = DownloadsViewPagerAdapter(childFragmentManager, lifecycle)
        binding.viewPager2.adapter = viewPagerAdapter
        TabLayoutMediator(binding.TabLayout, binding.viewPager2){tab, position ->
            when (position){
                0 -> tab.text = "Notes"
                1 -> tab.text = "Lectures"
            }
        }.attach()

        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}