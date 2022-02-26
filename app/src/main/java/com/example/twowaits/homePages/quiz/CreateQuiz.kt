package com.example.twowaits.homePages.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.twowaits.Data
import com.example.twowaits.R
import com.example.twowaits.databinding.CreateQuizBinding
import com.example.twowaits.viewmodels.quizViewModels.CreateQuizViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class CreateQuiz : Fragment() {
    private var _binding: CreateQuizBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CreateQuizBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this)[CreateQuizViewModel::class.java]
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.visibility = View.GONE
        val drawerLayout = activity?.findViewById<DrawerLayout>(R.id.drawerLayout)
        drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        binding.CreateAndAddQuestions.setOnClickListener {
            binding.TitleOfQuiz.helperText = ""
            binding.NoOfQuestionsOfQuiz.helperText = ""
            binding.DurationOfQuiz.helperText = ""
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
            if (binding.NoOfQuestions.text.toString().toInt() > 50) {
                binding.NoOfQuestionsOfQuiz.helperText = "You can add upto 50 Questions only"
                return@setOnClickListener
            }

            val title = binding.Title.text?.trim().toString()
            val description = null
            val noOfQuestion = binding.NoOfQuestions.text?.trim().toString().toInt()
            val timeLimit = binding.Duration.text?.trim().toString().toInt()
            val createQuizBody = CreateQuizBody(title, description, noOfQuestion, timeLimit)

            viewModel.createQuiz(createQuizBody)
            viewModel.createQuizLiveData.observe(viewLifecycleOwner) {
                Data.QUIZ_ID = it.quiz_id
                Data.QUESTIONS_LEFT = noOfQuestion
                findNavController().navigate(R.id.action_createQuiz_to_addQuestions)
            }
            viewModel.errorLiveData.observe(viewLifecycleOwner) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
        binding.AddNoOfQuestions.setOnClickListener {
            if (binding.NoOfQuestions.text.isNullOrEmpty()) binding.NoOfQuestions.setText("1")
            else {
                var noOfQuestions = binding.NoOfQuestions.text.toString().toInt()
                binding.NoOfQuestions.setText((++noOfQuestions).toString())
            }
        }
        binding.SubtractNoOfQuestions.setOnClickListener {
            if (!binding.NoOfQuestions.text.isNullOrEmpty() && binding.NoOfQuestions.text.toString().toInt() > 0) {
                var noOfQuestions = binding.NoOfQuestions.text.toString().toInt()
                binding.NoOfQuestions.setText((--noOfQuestions).toString())
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
            if (!binding.Duration.text.isNullOrEmpty() && binding.Duration.text.toString().toInt() > 0) {
                var duration = binding.Duration.text.toString().toInt()
                binding.Duration.setText((--duration).toString())
            }
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                bottomNavigationView?.visibility = View.VISIBLE
                val drawerLayout = activity?.findViewById<DrawerLayout>(R.id.drawerLayout)
                drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}