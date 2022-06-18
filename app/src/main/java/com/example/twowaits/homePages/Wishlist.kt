package com.example.twowaits.homePages

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.twowaits.NoteLectureActivity
import com.example.twowaits.R
import com.example.twowaits.databinding.WishlistBinding
import com.example.twowaits.network.dashboardApiCalls.AddToWishlistBody
import com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters.LecturesClicked
import com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters.RecentLecturesRecyclerAdapter
import com.example.twowaits.utils.Utils
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

    override fun onLectureClicked(videoUri: String, lectureName: String) {
        Utils.VIDEO_URI = videoUri
        val intent = Intent(context, NoteLectureActivity::class.java)
        intent.apply {
            intent.putExtra("PREVIOUS PAGE", "PROFILE")
            intent.putExtra("PAGE TYPE", "LECTURE")
            intent.putExtra("LECTURE NAME", lectureName)
        }
        startActivity(intent)
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