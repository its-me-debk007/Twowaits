package com.example.twowaits.homePages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.twowaits.databinding.BookmarkedQuestionsBinding
import com.example.twowaits.homePages.questionsAnswers.BookmarkQuestionBody
import com.example.twowaits.homePages.questionsAnswers.LikeAnswerBody
import com.example.twowaits.recyclerAdapters.ItemClicked
import com.example.twowaits.recyclerAdapters.QuestionsAnswersRecyclerAdapter
import com.example.twowaits.viewmodels.YourQuestionsViewModel
import com.example.twowaits.viewmodels.questionsAnswersViewModel.QuestionsAnswersViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class BookmarkedQuestions: Fragment(), ItemClicked {
    private var _binding: BookmarkedQuestionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = BookmarkedQuestionsBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]

        viewModel.getYourBookmarkedQ()
        viewModel.getBookmarkedQLiveData.observe(viewLifecycleOwner, {
            binding.BookmarkedQuestionsRecyclerView.adapter = QuestionsAnswersRecyclerAdapter( it.size, it, this)
            binding.BookmarkedQuestionsRecyclerView.layoutManager = object: LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean = false
            }
        })
        viewModel.errorGetBookmarkedQLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

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
        val viewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]
        viewModel.bookmarkQuestion(BookmarkQuestionBody(question_id))
//        viewModel.bookmarkQuestionLiveData.observe(viewLifecycleOwner, {

//        })
        viewModel.errorBookmarkQuestionLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }
}