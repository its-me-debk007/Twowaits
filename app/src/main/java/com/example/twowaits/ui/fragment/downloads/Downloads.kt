package com.example.twowaits.ui.fragment.downloads

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.twowaits.R
import com.example.twowaits.databinding.FragmentDownloadsBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.platform.MaterialFadeThrough

class Downloads : Fragment(R.layout.fragment_downloads) {
    private lateinit var binding: FragmentDownloadsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDownloadsBinding.bind(view)
        binding.apply {
            binding.swipeToRefresh.setColorSchemeColors(Color.parseColor("#804D37"))
            swipeToRefresh.setOnRefreshListener {
                swipeToRefresh.postDelayed({
                    findNavController().navigate(R.id.action_downloads_self)
                }, 440)
            }

            viewPager2.adapter = DownloadsViewPagerAdapter(childFragmentManager, lifecycle)
            TabLayoutMediator(TabLayout, viewPager2) { tab, position ->
                if (position == 0) tab.text = "Notes"
                else tab.text = "Lectures"
            }.attach()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialFadeThrough()
        enterTransition = MaterialFadeThrough()
        reenterTransition = MaterialFadeThrough()
        returnTransition = MaterialFadeThrough()

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_downloads_to_homePage)
            }
        })
    }
}

class DownloadsViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DownloadedNotes()
            else -> DownloadedLectures()
        }
    }
}

