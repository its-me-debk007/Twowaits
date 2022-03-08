package com.example.twowaits.homePages

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.twowaits.R
import com.example.twowaits.databinding.LibraryBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class Library : Fragment(R.layout.library) {
    private lateinit var binding: LibraryBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LibraryBinding.bind(view)
        binding.swipeToRefresh.setColorSchemeColors(Color.parseColor("#804D37"))
        binding.swipeToRefresh.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_library_self)
            }, 440)
        }
        binding.LibraryViewPager.adapter = LibraryViewPagerAdapter(childFragmentManager, lifecycle)
        TabLayoutMediator(binding.TabLayout, binding.LibraryViewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Q & A’s"
                1 -> tab.text = "Notes"
            }
        }.attach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_library_to_homePage)
            }
        })
    }
}