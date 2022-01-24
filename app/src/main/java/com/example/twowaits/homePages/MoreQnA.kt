package com.example.twowaits.homePages

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.twowaits.CompanionObjects
import com.example.twowaits.R
import com.example.twowaits.databinding.MoreQNABinding
import com.example.twowaits.databinding.ProfileBinding
import com.example.twowaits.homePages.questionsAnswers.LikeAnswerBody
import com.example.twowaits.recyclerAdapters.ItemClicked
import com.example.twowaits.recyclerAdapters.QuestionsAnswersRecyclerAdapter
import com.example.twowaits.viewmodels.HomePageViewModel
import com.example.twowaits.viewmodels.questionsAnswersViewModel.QuestionsAnswersViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class MoreQnA: Fragment(), ItemClicked {
    private var _binding: MoreQNABinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MoreQNABinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this)[HomePageViewModel::class.java]

        viewModel.getQnA()
        viewModel.getQnALiveData.observe(viewLifecycleOwner, {
            binding.moreQnA.adapter = QuestionsAnswersRecyclerAdapter(it.size, it, this)
            binding.moreQnA.layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean = false
            }
        })
        viewModel.errorGetQnALiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        binding.swipeToRefresh.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_moreQnA2_self)
            }, 440)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onQuestionClicked(question: String) {

    }

    override fun likeBtnClicked(question_id: Int) {
        val viewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]
        viewModel.likeAnswer(LikeAnswerBody(question_id))
//        viewModel.likeAnswerLiveData.observe(viewLifecycleOwner, {

//        })
        viewModel.errorLikeAnswerLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun commentBtnClicked(question_id: Int) {

    }

    override fun bookmarkBtnClicked(question_id: Int) {

    }
}