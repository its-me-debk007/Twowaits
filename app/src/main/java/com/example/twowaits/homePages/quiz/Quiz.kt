package com.example.twowaits.homePages.quiz

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
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
import com.example.twowaits.apiCalls.dashboardApiCalls.quizApiCalls.OptionXX
import com.example.twowaits.apiCalls.dashboardApiCalls.quizApiCalls.RegisterResponseBody
import com.example.twowaits.databinding.PleaseWaitDialog2Binding
import com.example.twowaits.databinding.QuizBinding
import com.example.twowaits.viewmodels.quizViewModels.QuizViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class Quiz : Fragment() {
    private var _binding: QuizBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = QuizBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this)[QuizViewModel::class.java]
        var chosenOptionId = 0
        val attemptQuizBody = AttemptQuizBody(CompanionObjects.QUIZ_ID)

        if (CompanionObjects.CHOSEN_OPTION[CompanionObjects.CURRENT_QUESTION] != null) {
            when (CompanionObjects.CHOSEN_OPTION[CompanionObjects.CURRENT_QUESTION]) {
                1 -> binding.option1.isChecked = true
                2 -> binding.option2.isChecked = true
                3 -> binding.option3.isChecked = true
                4 -> binding.option4.isChecked = true
            }
        }

        if (CompanionObjects.FIRST_TIME) {
            CompanionObjects.FIRST_TIME = false

            val dialog = Dialog(requireContext())
            dialog.setContentView(PleaseWaitDialog2Binding.inflate(layoutInflater).root)
            dialog.setCancelable(false)
            dialog.show()
            if (dialog.window != null)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))

            viewModel.getQuizData(attemptQuizBody)
            viewModel.getQuizLiveData.observe(viewLifecycleOwner, {
                CompanionObjects.QUIZ_DATA = it
                CompanionObjects.TIME_LIMIT = it.time_limit
                viewModel.attemptQuiz(attemptQuizBody)
                viewModel.attemptQuizLiveData.observe(viewLifecycleOwner, { response ->
//                    Toast.makeText(context, "Quiz has started", Toast.LENGTH_SHORT).show()
                    CompanionObjects.startTimer(it.time_limit)
                    CompanionObjects.QUIZ_RESULT_ID = response.quiz_result_id
                    CompanionObjects.TITLE_OF_QUIZ = it.title
                    binding.Title.text = CompanionObjects.TITLE_OF_QUIZ
                    binding.QuestionNo.text = "Q${CompanionObjects.CURRENT_QUESTION + 1}."
                    binding.Question.text =
                        CompanionObjects.QUIZ_DATA.question[CompanionObjects.CURRENT_QUESTION].question_text
                    dialog.hide()

                    for (i in CompanionObjects.QUIZ_DATA.question[CompanionObjects.CURRENT_QUESTION].option.indices) {
                        val optionText =
                            CompanionObjects.QUIZ_DATA.question[CompanionObjects.CURRENT_QUESTION].option[i].option
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
                                    CompanionObjects.QUIZ_DATA.question[CompanionObjects.CURRENT_QUESTION].option[0].option_id
                                CompanionObjects.CHOSEN_OPTION[CompanionObjects.CURRENT_QUESTION] =
                                    1
                            }
                            R.id.option2 -> {
                                chosenOptionId =
                                    CompanionObjects.QUIZ_DATA.question[CompanionObjects.CURRENT_QUESTION].option[1].option_id
                                CompanionObjects.CHOSEN_OPTION[CompanionObjects.CURRENT_QUESTION] =
                                    2
                            }
                            R.id.option3 -> {
                                chosenOptionId =
                                    CompanionObjects.QUIZ_DATA.question[CompanionObjects.CURRENT_QUESTION].option[2].option_id
                                CompanionObjects.CHOSEN_OPTION[CompanionObjects.CURRENT_QUESTION] =
                                    3
                            }
                            R.id.option4 -> {
                                chosenOptionId =
                                    CompanionObjects.QUIZ_DATA.question[CompanionObjects.CURRENT_QUESTION].option[3].option_id
                                CompanionObjects.CHOSEN_OPTION[CompanionObjects.CURRENT_QUESTION] =
                                    4
                            }
                        }
                    }
                })
                viewModel.errorAttemptQuizLiveData.observe(viewLifecycleOwner, { errorMessage ->
                    if (errorMessage == "Quiz already attempted") {
                        dialog.hide()
                        findNavController().navigate(R.id.action_quiz_to_quizResult)
                    } else {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        dialog.hide()
                    }
                })
            })
            viewModel.errorGetQuizLiveData.observe(viewLifecycleOwner, {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                dialog.hide()
            })

        } else {
            binding.Title.text = CompanionObjects.TITLE_OF_QUIZ
            binding.QuestionNo.text = "Q${CompanionObjects.CURRENT_QUESTION + 1}"
            binding.Question.text =
                CompanionObjects.QUIZ_DATA.question[CompanionObjects.CURRENT_QUESTION].question_text

            for (i in CompanionObjects.QUIZ_DATA.question[CompanionObjects.CURRENT_QUESTION].option.indices) {
                val optionText =
                    CompanionObjects.QUIZ_DATA.question[CompanionObjects.CURRENT_QUESTION].option[i].option
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

            if (CompanionObjects.CURRENT_QUESTION == CompanionObjects.QUIZ_DATA.question.size - 1)
                binding.NextBtn.text = "Complete"
            else
                binding.NextBtn.text = "Next"
            binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.option1 -> {
                        chosenOptionId =
                            CompanionObjects.QUIZ_DATA.question[CompanionObjects.CURRENT_QUESTION].option[0].option_id
                        CompanionObjects.CHOSEN_OPTION[CompanionObjects.CURRENT_QUESTION] = 1
                    }
                    R.id.option2 -> {
                        chosenOptionId =
                            CompanionObjects.QUIZ_DATA.question[CompanionObjects.CURRENT_QUESTION].option[1].option_id
                        CompanionObjects.CHOSEN_OPTION[CompanionObjects.CURRENT_QUESTION] = 2
                    }
                    R.id.option3 -> {
                        chosenOptionId =
                            CompanionObjects.QUIZ_DATA.question[CompanionObjects.CURRENT_QUESTION].option[2].option_id
                        CompanionObjects.CHOSEN_OPTION[CompanionObjects.CURRENT_QUESTION] = 3
                    }
                    R.id.option4 -> {
                        chosenOptionId =
                            CompanionObjects.QUIZ_DATA.question[CompanionObjects.CURRENT_QUESTION].option[3].option_id
                        CompanionObjects.CHOSEN_OPTION[CompanionObjects.CURRENT_QUESTION] = 4
                    }
                }
            }
        }

        CompanionObjects.timeLeftLiveData.observe(viewLifecycleOwner, {
            val timeLimit =
                if (CompanionObjects.TIME_LIMIT == 1) "${CompanionObjects.TIME_LIMIT} min" else "${CompanionObjects.TIME_LIMIT} mins"
            val min = if (it / 60 == 1) "${it / 60} min" else "${it / 60} mins"
            val sec = if (it % 60 == 1) "${it % 60} second" else "${it % 60} seconds"

            binding.TimeLeft.text = "You have spent $min $sec out of ${timeLimit}"
        })
        CompanionObjects.timeFinishedLiveData.observe(viewLifecycleOwner, {
            if (chosenOptionId != 0) {
                val registerResponseBody = RegisterResponseBody(
                    CompanionObjects.QUIZ_RESULT_ID,
                    CompanionObjects.QUIZ_DATA.question[CompanionObjects.CURRENT_QUESTION].quiz_question_id,
                    listOf(OptionXX(chosenOptionId))
                )
                viewModel.registerResponse(registerResponseBody)
                viewModel.registerResponseLiveData.observe(viewLifecycleOwner, {
                    CompanionObjects.FIRST_TIME = false
                    CompanionObjects.CURRENT_QUESTION = 0
                    CompanionObjects.CHOSEN_OPTION.clear()
                    binding.NextBtn.isEnabled = true
                    binding.PreviousBtn.isEnabled = true
                    findNavController().navigate(R.id.action_quiz_to_quizResult)
                })
                viewModel.errorRegisterResponseLiveData.observe(viewLifecycleOwner, {
                    Toast.makeText(
                        context,
                        it + "\nSorry! We were unable to evaluate this question",
                        Toast.LENGTH_SHORT
                    ).show()
                    CompanionObjects.FIRST_TIME = false
                    CompanionObjects.CURRENT_QUESTION = 0
                    CompanionObjects.CHOSEN_OPTION.clear()
                    binding.NextBtn.isEnabled = true
                    binding.PreviousBtn.isEnabled = true
                    findNavController().navigate(R.id.action_quiz_to_quizResult)
                })
            } else {
                CompanionObjects.FIRST_TIME = false
                CompanionObjects.CURRENT_QUESTION = 0
                CompanionObjects.CHOSEN_OPTION.clear()
                binding.NextBtn.isEnabled = true
                binding.PreviousBtn.isEnabled = true
                findNavController().navigate(R.id.action_quiz_to_quizResult)
            }
        })

        binding.NextBtn.setOnClickListener {
            binding.NextBtn.isEnabled = false
            binding.PreviousBtn.isEnabled = false

            if (CompanionObjects.CURRENT_QUESTION < CompanionObjects.QUIZ_DATA.question.size - 1) {
                if (chosenOptionId != 0) {
                    val registerResponseBody = RegisterResponseBody(
                        CompanionObjects.QUIZ_RESULT_ID,
                        CompanionObjects.QUIZ_DATA.question[CompanionObjects.CURRENT_QUESTION].quiz_question_id,
                        listOf(OptionXX(chosenOptionId))
                    )
                    viewModel.registerResponse(registerResponseBody)
                    viewModel.registerResponseLiveData.observe(viewLifecycleOwner, {
                        CompanionObjects.CURRENT_QUESTION++
                        binding.NextBtn.isEnabled = true
                        binding.PreviousBtn.isEnabled = true
                        findNavController().navigate(R.id.action_quiz_self)
                    })
                    viewModel.errorRegisterResponseLiveData.observe(viewLifecycleOwner, {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        binding.NextBtn.isEnabled = true
                        binding.PreviousBtn.isEnabled = true
                    })
                } else {
                    CompanionObjects.CURRENT_QUESTION++
                    binding.NextBtn.isEnabled = true
                    binding.PreviousBtn.isEnabled = true
                    findNavController().navigate(R.id.action_quiz_self)
                }
            } else {
                if (chosenOptionId != 0) {
                    val registerResponseBody = RegisterResponseBody(
                        CompanionObjects.QUIZ_RESULT_ID,
                        CompanionObjects.QUIZ_DATA.question[CompanionObjects.CURRENT_QUESTION].quiz_question_id,
                        listOf(OptionXX(chosenOptionId))
                    )
                    viewModel.registerResponse(registerResponseBody)
                    viewModel.registerResponseLiveData.observe(viewLifecycleOwner, {
                        CompanionObjects.FIRST_TIME = false
                        CompanionObjects.CURRENT_QUESTION = 0
                        CompanionObjects.CHOSEN_OPTION.clear()
                        binding.NextBtn.isEnabled = true
                        binding.PreviousBtn.isEnabled = true
                        CompanionObjects.timerCountDownTimer.cancel()
                        findNavController().navigate(R.id.action_quiz_to_quizResult)
                    })
                    viewModel.errorRegisterResponseLiveData.observe(viewLifecycleOwner, {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        binding.NextBtn.isEnabled = true
                        binding.PreviousBtn.isEnabled = true
                    })
                } else {
                    CompanionObjects.FIRST_TIME = false
                    CompanionObjects.CURRENT_QUESTION = 0
                    CompanionObjects.CHOSEN_OPTION.clear()
                    binding.NextBtn.isEnabled = true
                    binding.PreviousBtn.isEnabled = true
                    CompanionObjects.timerCountDownTimer.cancel()
                    findNavController().navigate(R.id.action_quiz_to_quizResult)
                }
            }
        }

        if (CompanionObjects.CURRENT_QUESTION == 0)
            binding.PreviousBtn.visibility = View.GONE
        else
            binding.PreviousBtn.visibility = View.VISIBLE

        binding.PreviousBtn.setOnClickListener {
            binding.NextBtn.isEnabled = false
            binding.PreviousBtn.isEnabled = false

            if (CompanionObjects.CURRENT_QUESTION > 0) {
                if (chosenOptionId != 0) {
                    val registerResponseBody = RegisterResponseBody(
                        CompanionObjects.QUIZ_RESULT_ID,
                        CompanionObjects.QUIZ_DATA.question[CompanionObjects.CURRENT_QUESTION].quiz_question_id,
                        listOf(OptionXX(chosenOptionId))
                    )
                    viewModel.registerResponse(registerResponseBody)
                    viewModel.registerResponseLiveData.observe(viewLifecycleOwner, {
                        CompanionObjects.CURRENT_QUESTION--
                        binding.NextBtn.isEnabled = true
                        binding.PreviousBtn.isEnabled = true
                        findNavController().navigate(R.id.action_quiz_self)
                    })
                    viewModel.errorRegisterResponseLiveData.observe(viewLifecycleOwner, {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        binding.NextBtn.isEnabled = true
                        binding.PreviousBtn.isEnabled = true
                    })
                } else {
                    CompanionObjects.CURRENT_QUESTION--
                    binding.NextBtn.isEnabled = true
                    binding.PreviousBtn.isEnabled = true
                    findNavController().navigate(R.id.action_quiz_self)
                }
            }
        }

        binding.Clear.setOnClickListener {
            binding.radioGroup.clearCheck()
            CompanionObjects.CHOSEN_OPTION.remove(CompanionObjects.CURRENT_QUESTION)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}