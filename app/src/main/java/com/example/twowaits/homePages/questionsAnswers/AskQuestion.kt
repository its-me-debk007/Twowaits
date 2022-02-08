package com.example.twowaits.homePages.questionsAnswers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.twowaits.R
import com.example.twowaits.databinding.AskQuestionBinding
import com.example.twowaits.viewmodels.questionsAnswersViewModel.QuestionsAnswersViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class AskQuestion: Fragment() {
    private var _binding: AskQuestionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AskQuestionBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]

        binding.askBtn.setOnClickListener {
            if (binding.question.text.toString().trim().isEmpty()){
                binding.questionLayout.helperText = "Please enter a question first"
            }
            binding.questionLayout.helperText = ""
            viewModel.askQuestion(AskQuestionBody(binding.question.text.toString()))
            binding.ProgressBar.visibility = View.VISIBLE
            viewModel.askQuestionLiveData.observe(viewLifecycleOwner, {
                binding.question.text?.clear()
                binding.ProgressBar.visibility = View.INVISIBLE
                findNavController().navigate(R.id.action_askQuestion_to_explore)
            })
            viewModel.errorAskQuestionLiveData.observe(viewLifecycleOwner, {
                binding.ProgressBar.visibility = View.INVISIBLE
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            })
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}