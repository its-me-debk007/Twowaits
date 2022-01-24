package com.example.twowaits.homePages

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import com.example.twowaits.databinding.HomePageBinding
import com.example.twowaits.homePages.questionsAnswers.BookmarkQuestionBody
import com.example.twowaits.homePages.questionsAnswers.LikeAnswerBody
import com.example.twowaits.recyclerAdapters.ItemClicked
import com.example.twowaits.recyclerAdapters.QuestionsAnswersRecyclerAdapter
import com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters.*
import com.example.twowaits.viewmodels.HomePageViewModel
import com.example.twowaits.viewmodels.questionsAnswersViewModel.QuestionsAnswersViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class HomePage : Fragment(), ItemClicked, QuizClicked, NotesClicked {
    private var _binding: HomePageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomePageBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this)[HomePageViewModel::class.java]

        binding.TopLecturesRecyclerView.adapter = RecentLecturesRecyclerAdapter(4)
        binding.TopLecturesRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        viewModel.recentNotes()
        viewModel.recentNotesLiveData.observe(viewLifecycleOwner, {
            val size = if (it.size < 4) it.size else 4
            binding.recentNotesRecyclerView.adapter = RecentNotesRecyclerAdapter(size, it, this)
            binding.recentNotesRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        })
        viewModel.errorRecentNotesLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.recentQuizzes()
        viewModel.recentQuizzesLiveData.observe(viewLifecycleOwner, {
            val size = if (it.size < 4) it.size else 4
            binding.QuizzesRecyclerView.adapter = QuizzesRecyclerAdapter(size, it, this)
            binding.QuizzesRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        })
        viewModel.errorRecentQuizzesLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.getQnA()
        viewModel.getQnALiveData.observe(viewLifecycleOwner, {
            binding.QnARecyclerView.adapter = QuestionsAnswersRecyclerAdapter(4, it, this)
            binding.QnARecyclerView.layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean = false
            }
        })
        viewModel.errorGetQnALiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        binding.QnA.setOnClickListener {
            findNavController().navigate(R.id.action_homePage_to_moreQnA2)
        }
        binding.arrow.setOnClickListener {
            findNavController().navigate(R.id.action_homePage_to_moreQnA2)
        }

        binding.StudentSuggestionRecyclerView.adapter = StudentsSuggestionRecyclerAdapter()
        binding.StudentSuggestionRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.swipeToRefresh.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                CompanionObjects.isSearchBarActiveLiveData.postValue(false)
                findNavController().navigate(R.id.action_homePage_self)
            }, 440)
        }
        binding.SearchBar.setOnEditorActionListener { v, actionId, event ->
            Log.d("PRESSED", "Yes")
            false
        }

        CompanionObjects.isSearchBarActiveLiveData.observe(viewLifecycleOwner, {
            if (it) {
                binding.textInputLayout4.visibility = View.VISIBLE
                binding.SearchBar.requestFocus()
//                showSoftKeyboard(binding.SearchBar)
            } else {
                binding.SearchBar.visibility = View.GONE
            }
        })

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

//    private fun showSoftKeyboard(view: View) {
//        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
//    }
//    private fun hideKeyboard(view: View) {
//        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(view.windowToken, 0)
//    }

    private fun exitConfirmation() {
        AlertDialog.Builder(context)
            .setTitle("Exit")
            .setMessage("Are you sure you want to exit?")
            .setIcon(R.drawable.exit_warning)
            .setPositiveButton("Yes") { _, _ ->
                activity?.finish()
            }
            .setNegativeButton("No") { _, _ ->
            }
            .create()
            .show()
    }

    override fun onQuestionClicked(question: String) {

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

    override fun likeBtnClicked(question_id: Int) {
        val viewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]
        viewModel.likeAnswer(LikeAnswerBody(question_id))
//        viewModel.likeAnswerLiveData.observe(viewLifecycleOwner, {

//        })
        viewModel.errorLikeAnswerLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onQuizClicked(quiz_id: Int) {
        findNavController().navigate(R.id.action_homePage_to_quiz)
        CompanionObjects.QUIZ_ID = quiz_id
    }

    override fun onNotesClicked(notes_id: Int) {
        findNavController().navigate(R.id.action_homePage_to_PDFViewer)

    }
}