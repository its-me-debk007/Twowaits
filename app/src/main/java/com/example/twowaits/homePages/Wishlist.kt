package com.example.twowaits.homePages

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.twowaits.Data
import com.example.twowaits.R
import com.example.twowaits.apiCalls.dashboardApiCalls.AddToWishlistBody
import com.example.twowaits.databinding.WishlistBinding
import com.example.twowaits.recyclerAdapters.WishlistLectureClicked
import com.example.twowaits.recyclerAdapters.WishlistRecyclerAdapter
import com.example.twowaits.viewmodels.questionsAnswersViewModel.QuestionsAnswersViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class Wishlist : Fragment(), WishlistLectureClicked {
    private var _binding: WishlistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WishlistBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]

        viewModel.getWishlist()
        viewModel.getWishlistData.observe(viewLifecycleOwner) {
            binding.WishlistRecyclerView.adapter = WishlistRecyclerAdapter(it.toMutableList(), this)
            binding.WishlistRecyclerView.layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean = false
            }
        }
        viewModel.errorGetWishlistData.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onLectureClicked(videoUri: Uri, lectureName: String) {
        Data.VIDEO_URI = videoUri
        Data.PREV_PAGE_FOR_PLAYER = "PROFILE"
        Data.LECTURE_NAME = lectureName
        findNavController().navigate(R.id.action_profile_to_videoPlayer2)
    }

    override fun onWishlistBtnClicked(lecture_id: Int) {
        val viewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]
        viewModel.addToWishlist(AddToWishlistBody(lecture_id))
        viewModel.addToWishlistData.observe(viewLifecycleOwner) {
            if (it != "success")
                Toast.makeText(
                    context,
                    it,
                    Toast.LENGTH_SHORT
                ).show()
        }
    }
}