package com.example.twowaits.homePages.questionsAnswers

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.twowaits.R
import com.example.twowaits.databinding.AskQuestionBinding
import com.example.twowaits.viewmodels.questionsAnswersViewModel.QuestionsAnswersViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class AskQuestion : Fragment(R.layout.ask_question) {
    private lateinit var binding: AskQuestionBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AskQuestionBinding.bind(view)
        val viewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]

        binding.askBtn.setOnClickListener {
            if (binding.question.text.toString().trim().isEmpty()) {
                binding.questionLayout.helperText = "Please enter a question first"
                return@setOnClickListener
            }
            binding.questionLayout.helperText = ""
            viewModel.askQuestion(AskQuestionBody(binding.question.text.toString()))
            binding.ProgressBar.visibility = View.VISIBLE
            viewModel.askQuestionLiveData.observe(viewLifecycleOwner) {
                Toast.makeText(context, "The Question has been asked!", Toast.LENGTH_SHORT).show()
                activity?.finish()
            }
            viewModel.errorAskQuestionLiveData.observe(viewLifecycleOwner) {
                binding.ProgressBar.visibility = View.INVISIBLE
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}