package com.example.twowaits.homePages

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.twowaits.Data
import com.example.twowaits.R
import com.example.twowaits.apiCalls.dashboardApiCalls.AddToWishlistBody
import com.example.twowaits.databinding.WishlistBinding
import com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters.LecturesClicked
import com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters.RecentLecturesRecyclerAdapter
import com.example.twowaits.viewmodels.questionsAnswersViewModel.QuestionsAnswersViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class Wishlist : Fragment(R.layout.wishlist), LecturesClicked {
    private lateinit var binding: WishlistBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = WishlistBinding.bind(view)
        val viewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]
        viewModel.getWishlist()
        viewModel.getWishlistData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) noItems()
            binding.WishlistRecyclerView.adapter = RecentLecturesRecyclerAdapter(
                "WISHLIST",
                it.size, it.toMutableList(), this
            )
            binding.WishlistRecyclerView.isNestedScrollingEnabled = false
        }
        viewModel.errorGetWishlistData.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onLectureClicked(videoUri: Uri, lectureName: String) {
        Data.VIDEO_URI = videoUri
        Data.PREV_PAGE_FOR_PLAYER = "PROFILE"
        Data.LECTURE_NAME = lectureName
        findNavController().navigate(R.id.action_profile_to_videoPlayer2)
    }

    override fun onWishlistBtnClicked(lectureId: Int) {
        val viewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]
        viewModel.addToWishlist(AddToWishlistBody(lectureId))
        viewModel.addToWishlistData.observe(viewLifecycleOwner) {
            if (it != "success")
                Toast.makeText(
                    context,
                    it,
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    override fun noItems() {
        binding.WishlistRecyclerView.visibility = View.GONE
        binding.emptyAnimation.visibility = View.VISIBLE
        binding.text.visibility = View.VISIBLE
    }
}