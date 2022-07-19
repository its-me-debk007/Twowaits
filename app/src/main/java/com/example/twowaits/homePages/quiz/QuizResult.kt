package com.example.twowaits.homePages.quiz

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.twowaits.utils.Utils
import com.example.twowaits.R
import com.example.twowaits.databinding.QuizResultBinding
import com.example.twowaits.recyclerAdapters.DetailedQuizResultRecyclerAdapter
import com.example.twowaits.viewModel.quizViewModel.QuizViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class QuizResult : Fragment(R.layout.quiz_result) {
    private lateinit var binding: QuizResultBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = QuizResultBinding.bind(view)
        val viewModel = ViewModelProvider(this)[QuizViewModel::class.java]
        val bottomNavigationView =
            activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.visibility = View.GONE
        val drawerLayout = activity?.findViewById<DrawerLayout>(R.id.drawerLayout)
        drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        val quizId = QuizResultArgs.fromBundle(requireArguments()).quizId
        viewModel.viewScore(AttemptQuizBody(quizId))
        viewModel.viewScoreLiveData.observe(viewLifecycleOwner) {
            Utils.CURRENT_QUESTION = 0
            binding.attemptedQuestions.text = it.attempted.toString()
            binding.correctQuestions.text = it.correct.toString()
            binding.wrongQuestions.text = it.wrong.toString()
            binding.totalScore.text = it.total_score
            binding.noOfQuestions.text = it.total_questions.toString()
            binding.title.text = it.title
        }
        viewModel.errorViewScoreLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.detailedQuizResult(quizId)
        viewModel.detailedQuizScoreData.observe(viewLifecycleOwner) {
            binding.detailedResultRecyclerView.apply {
                isNestedScrollingEnabled = false
                adapter = DetailedQuizResultRecyclerAdapter(it.question)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        })
    }
}