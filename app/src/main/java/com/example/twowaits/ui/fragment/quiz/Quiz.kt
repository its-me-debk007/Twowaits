package com.example.twowaits.ui.fragment.quiz

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.twowaits.R
import com.example.twowaits.databinding.QuizBinding
import com.example.twowaits.model.AttemptQuizBody
import com.example.twowaits.network.dashboardApiCalls.quizApiCalls.OptionXX
import com.example.twowaits.network.dashboardApiCalls.quizApiCalls.RegisterResponseBody
import com.example.twowaits.sealedClass.Response
import com.example.twowaits.util.*
import com.example.twowaits.viewModel.quizViewModel.QuizViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class Quiz : Fragment(R.layout.quiz) {
    private lateinit var binding: QuizBinding
    private val viewModel by lazy { ViewModelProvider(this)[QuizViewModel::class.java] }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = QuizBinding.bind(view)

        var chosenOptionId = 0
        val quizId = activity?.intent?.getIntExtra("Quiz ID", -1)
        val attemptQuizBody = AttemptQuizBody(quizId!!)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = "Quiz"

        if (FIRST_TIME) {
            FIRST_TIME = false
            val builder = MaterialAlertDialogBuilder(requireContext()).apply {
                background = ColorDrawable(Color.TRANSPARENT)
                setView(layoutInflater.inflate(R.layout.dialog_please_wait, null))
                setCancelable(false)
            }
            val dialog = builder.show()

            viewModel.getQuizData(attemptQuizBody)
            viewModel.quizLiveData.observe(viewLifecycleOwner) {
                if (it is Response.Success) {
                    QUIZ_DATA = it.data!!
                    viewModel.attemptQuiz(attemptQuizBody)
                    viewModel.attemptQuizLiveData.observe(viewLifecycleOwner) { response ->
                        if (response is Response.Success) {
                            it.data.time_limit.startTimer()
                            Utils.QUIZ_RESULT_ID = response.data!!.quiz_result_id

                            binding.Title.text = QUIZ_DATA.title
                            binding.QuestionNo.text = "Q${Utils.CURRENT_QUESTION + 1}."
                            binding.Question.text =
                                it.data.question[Utils.CURRENT_QUESTION].question_text
                            dialog.dismiss()

                            for (i in QUIZ_DATA.question[Utils.CURRENT_QUESTION].option.indices) {
                                val optionText =
                                    QUIZ_DATA.question[Utils.CURRENT_QUESTION].option[i].option
                                when (i) {
                                    0 -> {
                                        binding.option1.text = optionText
                                        binding.option1.visibility = View.VISIBLE
                                    }
                                    1 -> {
                                        binding.option2.text = optionText
                                        binding.option2.visibility = View.VISIBLE
                                    }
                                    2 -> {
                                        binding.option3.text = optionText
                                        binding.option3.visibility = View.VISIBLE
                                    }
                                    3 -> {
                                        binding.option4.text = optionText
                                        binding.option4.visibility = View.VISIBLE
                                    }
                                }
                            }

                            binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
                                when (checkedId) {
                                    R.id.option1 -> {
                                        chosenOptionId =
                                            QUIZ_DATA.question[Utils.CURRENT_QUESTION].option[0].option_id
                                        Utils.CHOSEN_OPTION[Utils.CURRENT_QUESTION] = 1
                                    }
                                    R.id.option2 -> {
                                        chosenOptionId =
                                            QUIZ_DATA.question[Utils.CURRENT_QUESTION].option[1].option_id
                                        Utils.CHOSEN_OPTION[Utils.CURRENT_QUESTION] = 2
                                    }
                                    R.id.option3 -> {
                                        chosenOptionId =
                                            QUIZ_DATA.question[Utils.CURRENT_QUESTION].option[2].option_id
                                        Utils.CHOSEN_OPTION[Utils.CURRENT_QUESTION] = 3
                                    }
                                    R.id.option4 -> {
                                        chosenOptionId =
                                            QUIZ_DATA.question[Utils.CURRENT_QUESTION].option[3].option_id
                                        Utils.CHOSEN_OPTION[Utils.CURRENT_QUESTION] = 4
                                    }
                                }
                            }
                        } else {
                            if (response.errorMessage == "Quiz already attempted") {
                                dialog.dismiss()
                                FIRST_TIME = true
                                time = 0
                                val action = QuizDirections.actionQuizToQuizResult(quizId)
                                findNavController().navigate(action)

                            } else {
                                Toast.makeText(context, response.errorMessage, Toast.LENGTH_SHORT)
                                    .show()
                                dialog.dismiss()
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, it.errorMessage, Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
        } else {
            binding.Title.text = QUIZ_DATA.title
            binding.QuestionNo.text = "Q${Utils.CURRENT_QUESTION + 1}"
            binding.Question.text =
                QUIZ_DATA.question[Utils.CURRENT_QUESTION].question_text

            for (i in QUIZ_DATA.question[Utils.CURRENT_QUESTION].option.indices) {
                val optionText =
                    QUIZ_DATA.question[Utils.CURRENT_QUESTION].option[i].option
                when (i) {
                    0 -> {
                        binding.option1.text = optionText
                        binding.option1.visibility = View.VISIBLE
                    }
                    1 -> {
                        binding.option2.text = optionText
                        binding.option2.visibility = View.VISIBLE
                    }
                    2 -> {
                        binding.option3.text = optionText
                        binding.option3.visibility = View.VISIBLE
                    }
                    3 -> {
                        binding.option4.text = optionText
                        binding.option4.visibility = View.VISIBLE
                    }
                }
            }

            if (Utils.CURRENT_QUESTION == QUIZ_DATA.question.size - 1)
                binding.NextBtn.text = "Complete"
            else
                binding.NextBtn.text = "Next"
            binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.option1 -> {
                        chosenOptionId =
                            QUIZ_DATA.question[Utils.CURRENT_QUESTION].option[0].option_id
                        Utils.CHOSEN_OPTION[Utils.CURRENT_QUESTION] = 1
                    }
                    R.id.option2 -> {
                        chosenOptionId =
                            QUIZ_DATA.question[Utils.CURRENT_QUESTION].option[1].option_id
                        Utils.CHOSEN_OPTION[Utils.CURRENT_QUESTION] = 2
                    }
                    R.id.option3 -> {
                        chosenOptionId =
                            QUIZ_DATA.question[Utils.CURRENT_QUESTION].option[2].option_id
                        Utils.CHOSEN_OPTION[Utils.CURRENT_QUESTION] = 3
                    }
                    R.id.option4 -> {
                        chosenOptionId =
                            QUIZ_DATA.question[Utils.CURRENT_QUESTION].option[3].option_id
                        Utils.CHOSEN_OPTION[Utils.CURRENT_QUESTION] = 4
                    }
                }
            }
        }

        timeLeftLiveData.observe(viewLifecycleOwner) {
            val limit =
                if (QUIZ_DATA.time_limit == 1) "${QUIZ_DATA.time_limit} min" else "${QUIZ_DATA.time_limit} min(s)"
            val min = if (it / 60 == 1) "${it / 60} min" else "${it / 60} min(s)"
            val sec = if (it % 60 == 1) "${it % 60} second" else "${it % 60} seconds"

            binding.TimeLeft.text = "You have spent $min $sec out of $limit"
        }
        timeFinishedLiveData.observe(viewLifecycleOwner) {
            if (chosenOptionId != 0) {
                val registerResponseBody = RegisterResponseBody(
                    Utils.QUIZ_RESULT_ID,
                    QUIZ_DATA.question[Utils.CURRENT_QUESTION].quiz_question_id,
                    listOf(OptionXX(chosenOptionId))
                )
                viewModel.registerResponse(registerResponseBody)
                viewModel.registerResponseLiveData.observe(viewLifecycleOwner) {
                    FIRST_TIME = true
                    Utils.CURRENT_QUESTION = 0
                    Utils.CHOSEN_OPTION.clear()
                    binding.NextBtn.isEnabled = true
                    binding.PreviousBtn.isEnabled = true
                    time = 0
                    val action = QuizDirections.actionQuizToQuizResult(quizId)
                    findNavController().navigate(action)
                }
                viewModel.errorRegisterResponseLiveData.observe(viewLifecycleOwner) {
                    Toast.makeText(
                        context,
                        "$it\nSorry! We were unable to evaluate this question",
                        Toast.LENGTH_SHORT
                    ).show()
                    FIRST_TIME = true
                    Utils.CURRENT_QUESTION = 0
                    Utils.CHOSEN_OPTION.clear()
                    binding.NextBtn.isEnabled = true
                    binding.PreviousBtn.isEnabled = true
                    time = 0
                    val action = QuizDirections.actionQuizToQuizResult(quizId)
                    findNavController().navigate(action)
                }
            } else {
                FIRST_TIME = true
                Utils.CURRENT_QUESTION = 0
                Utils.CHOSEN_OPTION.clear()
                binding.NextBtn.isEnabled = true
                binding.PreviousBtn.isEnabled = true
                time = 0
                val action = QuizDirections.actionQuizToQuizResult(quizId)
                findNavController().navigate(action)
            }
        }

        binding.NextBtn.setOnClickListener {
            binding.NextBtn.isEnabled = false
            binding.PreviousBtn.isEnabled = false

            if (Utils.CURRENT_QUESTION < QUIZ_DATA.question.size - 1) {
                if (chosenOptionId != 0) {
                    val registerResponseBody = RegisterResponseBody(
                        Utils.QUIZ_RESULT_ID,
                        QUIZ_DATA.question[Utils.CURRENT_QUESTION].quiz_question_id,
                        listOf(OptionXX(chosenOptionId))
                    )
                    viewModel.registerResponse(registerResponseBody)
                    viewModel.registerResponseLiveData.observe(viewLifecycleOwner) {
                        Utils.CURRENT_QUESTION++
                        binding.NextBtn.isEnabled = true
                        binding.PreviousBtn.isEnabled = true
                        findNavController().navigate(R.id.action_quiz_self)
                    }
                    viewModel.errorRegisterResponseLiveData.observe(viewLifecycleOwner) {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        binding.NextBtn.isEnabled = true
                        binding.PreviousBtn.isEnabled = true
                    }
                } else {
                    Utils.CURRENT_QUESTION++
                    binding.NextBtn.isEnabled = true
                    binding.PreviousBtn.isEnabled = true
                    findNavController().navigate(R.id.action_quiz_self)
                }
            } else {
                if (chosenOptionId != 0) {
                    val registerResponseBody = RegisterResponseBody(
                        Utils.QUIZ_RESULT_ID,
                        QUIZ_DATA.question[Utils.CURRENT_QUESTION].quiz_question_id,
                        listOf(OptionXX(chosenOptionId))
                    )
                    viewModel.registerResponse(registerResponseBody)
                    viewModel.registerResponseLiveData.observe(viewLifecycleOwner) {
                        FIRST_TIME = true
                        Utils.CURRENT_QUESTION = 0
                        Utils.CHOSEN_OPTION.clear()
                        binding.NextBtn.isEnabled = true
                        binding.PreviousBtn.isEnabled = true
                        timerCountDownTimer.cancel()
                        time = 0
                        val action = QuizDirections.actionQuizToQuizResult(quizId)
                        findNavController().navigate(action)
                    }
                    viewModel.errorRegisterResponseLiveData.observe(viewLifecycleOwner) {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        binding.NextBtn.isEnabled = true
                        binding.PreviousBtn.isEnabled = true
                    }
                } else {
                    FIRST_TIME = true
                    Utils.CURRENT_QUESTION = 0
                    Utils.CHOSEN_OPTION.clear()
                    binding.NextBtn.isEnabled = true
                    binding.PreviousBtn.isEnabled = true
                    timerCountDownTimer.cancel()
                    time = 0
                    val action = QuizDirections.actionQuizToQuizResult(quizId)
                    findNavController().navigate(action)
                }
            }
        }

        if (Utils.CURRENT_QUESTION == 0)
            binding.PreviousBtn.visibility = View.GONE
        else
            binding.PreviousBtn.visibility = View.VISIBLE

        binding.PreviousBtn.setOnClickListener {
            binding.NextBtn.isEnabled = false
            binding.PreviousBtn.isEnabled = false

            if (Utils.CURRENT_QUESTION > 0) {
                if (chosenOptionId != 0) {
                    val registerResponseBody = RegisterResponseBody(
                        Utils.QUIZ_RESULT_ID,
                        QUIZ_DATA.question[Utils.CURRENT_QUESTION].quiz_question_id,
                        listOf(OptionXX(chosenOptionId))
                    )
                    viewModel.registerResponse(registerResponseBody)
                    viewModel.registerResponseLiveData.observe(viewLifecycleOwner) {
                        Utils.CURRENT_QUESTION--
                        binding.NextBtn.isEnabled = true
                        binding.PreviousBtn.isEnabled = true
                        findNavController().navigate(R.id.action_quiz_self)
                    }
                    viewModel.errorRegisterResponseLiveData.observe(viewLifecycleOwner) {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        binding.NextBtn.isEnabled = true
                        binding.PreviousBtn.isEnabled = true
                    }
                } else {
                    Utils.CURRENT_QUESTION--
                    binding.NextBtn.isEnabled = true
                    binding.PreviousBtn.isEnabled = true
                    findNavController().navigate(R.id.action_quiz_self)
                }
            }
        }
        binding.Clear.setOnClickListener {
            binding.radioGroup.clearCheck()
            Utils.CHOSEN_OPTION.remove(Utils.CURRENT_QUESTION)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Toast.makeText(
                    context,
                    "Finish the quiz first!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}