package com.example.twowaits.homePages.quiz

import android.app.Dialog
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
import com.example.twowaits.databinding.PleaseWaitDialog2Binding
import com.example.twowaits.databinding.QuizBinding
import com.example.twowaits.network.dashboardApiCalls.quizApiCalls.OptionXX
import com.example.twowaits.network.dashboardApiCalls.quizApiCalls.RegisterResponseBody
import com.example.twowaits.util.*
import com.example.twowaits.viewModel.quizViewModel.QuizViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class Quiz : Fragment(R.layout.quiz) {
    private lateinit var binding: QuizBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = QuizBinding.bind(view)
        val viewModel = ViewModelProvider(this)[QuizViewModel::class.java]
        var chosenOptionId = 0
        var timeLimit = 0
        val quizId = activity?.intent?.getIntExtra("Quiz ID", -1)
        val attemptQuizBody = AttemptQuizBody(quizId!!)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = "Quiz"

        if (Utils.CHOSEN_OPTION[Utils.CURRENT_QUESTION] != null) {
            when (Utils.CHOSEN_OPTION[Utils.CURRENT_QUESTION]) {
                1 -> binding.option1.isChecked = true
                2 -> binding.option2.isChecked = true
                3 -> binding.option3.isChecked = true
                4 -> binding.option4.isChecked = true
            }
        }

        if (Utils.FIRST_TIME) {
            Utils.FIRST_TIME = false
            val dialog = Dialog(requireContext())
            dialog.setContentView(PleaseWaitDialog2Binding.inflate(layoutInflater).root)
            dialog.setCancelable(false)
            dialog.show()
            if (dialog.window != null)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))

            viewModel.getQuizData(attemptQuizBody)
            viewModel.getQuizLiveData.observe(viewLifecycleOwner) {
                Utils.QUIZ_DATA = it
                timeLimit = it.time_limit
                viewModel.attemptQuiz(attemptQuizBody)
                viewModel.attemptQuizLiveData.observe(viewLifecycleOwner) { response ->
                    it.time_limit.startTimer()
                    Utils.QUIZ_RESULT_ID = response.quiz_result_id
                    Utils.TITLE_OF_QUIZ = it.title
                    binding.Title.text = Utils.TITLE_OF_QUIZ
                    binding.QuestionNo.text = "Q${Utils.CURRENT_QUESTION + 1}."
                    binding.Question.text =
                        Utils.QUIZ_DATA.question[Utils.CURRENT_QUESTION].question_text
                    dialog.hide()

                    for (i in Utils.QUIZ_DATA.question[Utils.CURRENT_QUESTION].option.indices) {
                        val optionText =
                            Utils.QUIZ_DATA.question[Utils.CURRENT_QUESTION].option[i].option
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
                                    Utils.QUIZ_DATA.question[Utils.CURRENT_QUESTION].option[0].option_id
                                Utils.CHOSEN_OPTION[Utils.CURRENT_QUESTION] = 1
                            }
                            R.id.option2 -> {
                                chosenOptionId =
                                    Utils.QUIZ_DATA.question[Utils.CURRENT_QUESTION].option[1].option_id
                                Utils.CHOSEN_OPTION[Utils.CURRENT_QUESTION] = 2
                            }
                            R.id.option3 -> {
                                chosenOptionId =
                                    Utils.QUIZ_DATA.question[Utils.CURRENT_QUESTION].option[2].option_id
                                Utils.CHOSEN_OPTION[Utils.CURRENT_QUESTION] = 3
                            }
                            R.id.option4 -> {
                                chosenOptionId =
                                    Utils.QUIZ_DATA.question[Utils.CURRENT_QUESTION].option[3].option_id
                                Utils.CHOSEN_OPTION[Utils.CURRENT_QUESTION] = 4
                            }
                        }
                    }
                }
                viewModel.errorAttemptQuizLiveData.observe(viewLifecycleOwner) { errorMessage ->
                    if (errorMessage == "Quiz already attempted") {
                        dialog.hide()
                        Utils.FIRST_TIME = true
                        time = 0
                        val action = QuizDirections.actionQuizToQuizResult(quizId)
                        findNavController().navigate(action)

                    } else {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        dialog.hide()
                    }
                }
            }
            viewModel.errorGetQuizLiveData.observe(viewLifecycleOwner) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                dialog.hide()
            }

        } else {
            binding.Title.text = Utils.TITLE_OF_QUIZ
            binding.QuestionNo.text = "Q${Utils.CURRENT_QUESTION + 1}"
            binding.Question.text =
                Utils.QUIZ_DATA.question[Utils.CURRENT_QUESTION].question_text

            for (i in Utils.QUIZ_DATA.question[Utils.CURRENT_QUESTION].option.indices) {
                val optionText =
                    Utils.QUIZ_DATA.question[Utils.CURRENT_QUESTION].option[i].option
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

            if (Utils.CURRENT_QUESTION == Utils.QUIZ_DATA.question.size - 1)
                binding.NextBtn.text = "Complete"
            else
                binding.NextBtn.text = "Next"
            binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.option1 -> {
                        chosenOptionId =
                            Utils.QUIZ_DATA.question[Utils.CURRENT_QUESTION].option[0].option_id
                        Utils.CHOSEN_OPTION[Utils.CURRENT_QUESTION] = 1
                    }
                    R.id.option2 -> {
                        chosenOptionId =
                            Utils.QUIZ_DATA.question[Utils.CURRENT_QUESTION].option[1].option_id
                        Utils.CHOSEN_OPTION[Utils.CURRENT_QUESTION] = 2
                    }
                    R.id.option3 -> {
                        chosenOptionId =
                            Utils.QUIZ_DATA.question[Utils.CURRENT_QUESTION].option[2].option_id
                        Utils.CHOSEN_OPTION[Utils.CURRENT_QUESTION] = 3
                    }
                    R.id.option4 -> {
                        chosenOptionId =
                            Utils.QUIZ_DATA.question[Utils.CURRENT_QUESTION].option[3].option_id
                        Utils.CHOSEN_OPTION[Utils.CURRENT_QUESTION] = 4
                    }
                }
            }
        }

        timeLeftLiveData.observe(viewLifecycleOwner) {
            val limit =
                if (timeLimit == 1) "$timeLimit min" else "$timeLimit min(s)"
            val min = if (it / 60 == 1) "${it / 60} min" else "${it / 60} min(s)"
            val sec = if (it % 60 == 1) "${it % 60} second" else "${it % 60} seconds"

            binding.TimeLeft.text = "You have spent $min $sec out of $limit"
        }
        timeFinishedLiveData.observe(viewLifecycleOwner) {
            if (chosenOptionId != 0) {
                val registerResponseBody = RegisterResponseBody(
                    Utils.QUIZ_RESULT_ID,
                    Utils.QUIZ_DATA.question[Utils.CURRENT_QUESTION].quiz_question_id,
                    listOf(OptionXX(chosenOptionId))
                )
                viewModel.registerResponse(registerResponseBody)
                viewModel.registerResponseLiveData.observe(viewLifecycleOwner) {
                    Utils.FIRST_TIME = true
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
                    Utils.FIRST_TIME = true
                    Utils.CURRENT_QUESTION = 0
                    Utils.CHOSEN_OPTION.clear()
                    binding.NextBtn.isEnabled = true
                    binding.PreviousBtn.isEnabled = true
                    time = 0
                    val action = QuizDirections.actionQuizToQuizResult(quizId)
                    findNavController().navigate(action)
                }
            } else {
                Utils.FIRST_TIME = true
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

            if (Utils.CURRENT_QUESTION < Utils.QUIZ_DATA.question.size - 1) {
                if (chosenOptionId != 0) {
                    val registerResponseBody = RegisterResponseBody(
                        Utils.QUIZ_RESULT_ID,
                        Utils.QUIZ_DATA.question[Utils.CURRENT_QUESTION].quiz_question_id,
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
                        Utils.QUIZ_DATA.question[Utils.CURRENT_QUESTION].quiz_question_id,
                        listOf(OptionXX(chosenOptionId))
                    )
                    viewModel.registerResponse(registerResponseBody)
                    viewModel.registerResponseLiveData.observe(viewLifecycleOwner) {
                        Utils.FIRST_TIME = true
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
                    Utils.FIRST_TIME = true
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
                        Utils.QUIZ_DATA.question[Utils.CURRENT_QUESTION].quiz_question_id,
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
                    "You cannot exit the quiz without submitting! Use next and previous buttons to navigate between questions",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}