package com.example.twowaits.homePages.questionsAnswers

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = "Ask a Question"

        binding.askBtn.setOnClickListener {
            if (binding.question.text.toString().trim().isEmpty()) {
                binding.questionLayout.helperText = "Please enter a question first"
            }
            binding.questionLayout.helperText = ""
            viewModel.askQuestion(AskQuestionBody(binding.question.text.toString()))
            binding.ProgressBar.visibility = View.VISIBLE
            viewModel.askQuestionLiveData.observe(viewLifecycleOwner) {
                binding.question.text?.clear()
                binding.ProgressBar.visibility = View.INVISIBLE
                findNavController().navigate(R.id.action_askQuestion_to_explore)
            }
            viewModel.errorAskQuestionLiveData.observe(viewLifecycleOwner) {
                binding.ProgressBar.visibility = View.INVISIBLE
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val actionBar = (activity as AppCompatActivity).supportActionBar
                actionBar?.title = "EduCool"
                findNavController().navigate(R.id.action_askQuestion_to_explore)
            }
        })
    }
}