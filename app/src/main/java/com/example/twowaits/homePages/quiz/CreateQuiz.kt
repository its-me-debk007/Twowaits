package com.example.twowaits.homePages.quiz

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.twowaits.CompanionObjects
import com.example.twowaits.R
import com.example.twowaits.databinding.CreateQuizBinding
import com.example.twowaits.viewmodels.CreateQuizViewModel

class CreateQuiz : Fragment() {
    private var _binding: CreateQuizBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CreateQuizBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this)[CreateQuizViewModel::class.java]

        binding.CreateAndAddQuestions.setOnClickListener {
            when {
                binding.Title.text!!.trim().isEmpty() -> {
                    binding.TitleOfQuiz.helperText = "Please enter the title of the Quiz"
                    return@setOnClickListener
                }
                binding.NoOfQuestions.text!!.trim().isEmpty() -> {
                    binding.TitleOfQuiz.helperText = ""
                    binding.NoOfQuestionsOfQuiz.helperText =
                        "Please enter the no. of questions for the Quiz"
                    return@setOnClickListener
                }
                binding.Duration.text!!.trim().isEmpty() -> {
                    binding.TitleOfQuiz.helperText = ""
                    binding.NoOfQuestionsOfQuiz.helperText = ""
                    binding.DurationOfQuiz.helperText = "Please enter the duration of the Quiz"
                    return@setOnClickListener
                }
            }
            binding.TitleOfQuiz.helperText = ""
            binding.NoOfQuestionsOfQuiz.helperText = ""
            binding.DurationOfQuiz.helperText = ""

            val title = binding.Title.text?.trim().toString()
            val description = "Just a quiz"
            val noOfQuestion = binding.NoOfQuestions.text?.trim().toString()
            val timeLimit = binding.Duration.text?.trim().toString()
            Log.d("Details", "$title $description ${noOfQuestion.toInt()} ${timeLimit.toInt()}")
            val authToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNjQyNDE4Mzc2LCJpYXQiOjE2NDIzMzE5NzYsImp0aSI6ImIzOTlmZTFjZGM2ZjQxMGY5Yzk1ZGVhMzYxOTg5NGVlIiwidXNlcl9pZCI6Nn0.JDrAQVl0A5lcTBy4Np3z4Icr3lHaUr3jZ_AbccUdL24".trim()
            viewModel.createQuiz( title, description, noOfQuestion.toInt(), timeLimit.toInt())
            viewModel.createQuizLiveData.observe(viewLifecycleOwner, {
                CompanionObjects.QUIZ_ID = it.quiz_id
                findNavController().navigate(R.id.action_createQuiz_to_addQuestions)
            })
            viewModel.errorLiveData.observe(viewLifecycleOwner, {
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