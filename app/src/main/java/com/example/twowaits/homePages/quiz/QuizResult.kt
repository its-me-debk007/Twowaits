package com.example.twowaits.homePages.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.twowaits.CompanionObjects
import com.example.twowaits.R
import com.example.twowaits.databinding.QuizResultBinding
import com.example.twowaits.viewmodels.QuizViewModel
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

        viewModel.viewScore(AttemptQuizBody(45))
        viewModel.viewScoreLiveData.observe(viewLifecycleOwner, {
            CompanionObjects.CURRENT_QUESTION = 0
            binding.Attempted.text = "Attempted: ${it.attempted}"
            binding.Correct.text = "Correct: ${it.correct}"
            binding.Wrong.text = "Wrong: ${it.wrong}"
            binding.TotalScore.text = "Total Score: ${it.total_score}"
        })
        viewModel.errorViewScoreLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}