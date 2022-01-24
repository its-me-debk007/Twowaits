package com.example.twowaits.homePages.questionsAnswers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.twowaits.databinding.CreateQuestionBinding
import com.example.twowaits.viewmodels.questionsAnswersViewModel.QuestionsAnswersViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class AskQuestion: Fragment() {
    private var _binding: CreateQuestionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CreateQuestionBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]

        binding.createQuestionBtn.setOnClickListener {
            Toast.makeText(context, "Please wait", Toast.LENGTH_SHORT).show()
            viewModel.askQuestion(AskQuestionBody(binding.question.text.toString()))
            viewModel.askQuestionLiveData.observe(viewLifecycleOwner, {
                Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show()
            })
            viewModel.errorAskQuestionLiveData.observe(viewLifecycleOwner, {
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