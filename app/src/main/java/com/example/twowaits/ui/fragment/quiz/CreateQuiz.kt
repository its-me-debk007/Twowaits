package com.example.twowaits.ui.fragment.quiz

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.twowaits.R
import com.example.twowaits.databinding.CreateQuizBinding
import com.example.twowaits.model.CreateQuizBody
import com.example.twowaits.viewModel.quizViewModel.CreateQuizViewModel

class CreateQuiz : Fragment(R.layout.create_quiz) {
    private lateinit var binding: CreateQuizBinding
    val viewModel by lazy { ViewModelProvider(this)[CreateQuizViewModel::class.java] }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CreateQuizBinding.bind(view)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = "Create a Quiz"

        binding.CreateAndAddQuestions.setOnClickListener {
            binding.TitleOfQuiz.helperText = ""
            binding.NoOfQuestionsOfQuiz.helperText = ""
            binding.DurationOfQuiz.helperText = ""
            when {
                binding.Title.text!!.trim().isEmpty() -> {
                    binding.TitleOfQuiz.helperText = "Please enter the title of the Quiz"
                    return@setOnClickListener
                }
                binding.noOfQuestions.text!!.trim().isEmpty() -> {
                    binding.NoOfQuestionsOfQuiz.helperText =
                        "Please enter the no. of questions for the Quiz"
                    return@setOnClickListener
                }
                binding.Duration.text!!.trim().isEmpty() -> {
                    binding.DurationOfQuiz.helperText = "Please enter the duration of the Quiz"
                    return@setOnClickListener
                }
            }
            if (binding.noOfQuestions.text.toString().toInt() > 50) {
                binding.NoOfQuestionsOfQuiz.helperText = "You can add upto 50 Questions only"
                return@setOnClickListener
            }

            val title = binding.Title.text?.trim().toString()
            val description = null
            val noOfQuestion = binding.noOfQuestions.text?.trim().toString().toInt()
            val timeLimit = binding.Duration.text?.trim().toString().toInt()
            val createQuizBody = CreateQuizBody(title, description, noOfQuestion, timeLimit)

            viewModel.createQuiz(createQuizBody)
            viewModel.createQuizLiveData.observe(viewLifecycleOwner) {
                val action = CreateQuizDirections.actionCreateQuiz2ToAddQuestions2(
                    noOfQuestion, it.quiz_id
                )
                findNavController().navigate(action)
            }
            viewModel.errorLiveData.observe(viewLifecycleOwner) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
        binding.AddNoOfQuestions.setOnClickListener {
            if (binding.noOfQuestions.text.isNullOrEmpty()) binding.noOfQuestions.setText("1")
            else {
                var noOfQuestions = binding.noOfQuestions.text.toString().toInt()
                binding.noOfQuestions.setText((++noOfQuestions).toString())
            }
        }
        binding.SubtractNoOfQuestions.setOnClickListener {
            if (!binding.noOfQuestions.text.isNullOrEmpty() &&
                binding.noOfQuestions.text.toString().toInt() > 1
            ) {
                var noOfQuestions = binding.noOfQuestions.text.toString().toInt()
                binding.noOfQuestions.setText((--noOfQuestions).toString())
            }
        }
        binding.AddDuration.setOnClickListener {
            if (binding.Duration.text.isNullOrEmpty()) binding.Duration.setText("1")
            else {
                var duration = binding.Duration.text.toString().toInt()
                binding.Duration.setText((++duration).toString())
            }
        }
        binding.SubtractDuration.setOnClickListener {
            if (!binding.Duration.text.isNullOrEmpty() &&
                binding.Duration.text.toString().toInt() > 1
            ) {
                var duration = binding.Duration.text.toString().toInt()
                binding.Duration.setText((--duration).toString())
            }
        }
    }
}