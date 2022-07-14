package com.example.twowaits.homePages

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.twowaits.ui.fragment.home.library.BookmarkedNotes
import com.example.twowaits.ui.fragment.home.library.BookmarkedQuestions
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class LibraryViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BookmarkedQuestions()
            else -> BookmarkedNotes()
        }
    }
}