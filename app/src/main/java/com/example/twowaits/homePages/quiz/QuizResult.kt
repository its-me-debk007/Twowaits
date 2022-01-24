package com.example.twowaits.homePages.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.twowaits.CompanionObjects
import com.example.twowaits.R
import com.example.twowaits.databinding.QuizResultBinding
import com.example.twowaits.viewmodels.quizViewModels.QuizViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class QuizResult : Fragment() {
    private var _binding: QuizResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = QuizResultBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this)[QuizViewModel::class.java]

        viewModel.viewScore(AttemptQuizBody(CompanionObjects.QUIZ_ID))
        viewModel.viewScoreLiveData.observe(viewLifecycleOwner, {
            CompanionObjects.CURRENT_QUESTION = 0
            binding.attemptedQuestions.text = it.attempted.toString()
            binding.correctQuestions.text = it.correct.toString()
            binding.wrongQuestions.text = it.wrong.toString()
            binding.totalScore.text = it.total_score
            binding.noOfQuestions.text = it.total_questions.toString()
            binding.title.text = it.title
        })
        viewModel.errorViewScoreLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_quizResult_to_homePage)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}