package com.example.twowaits.homePages.quiz

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
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
import com.example.twowaits.apiCalls.dashboardApiCalls.quizApiCalls.OptionXX
import com.example.twowaits.apiCalls.dashboardApiCalls.quizApiCalls.RegisterResponseBody
import com.example.twowaits.databinding.PleaseWaitDialog2Binding
import com.example.twowaits.databinding.QuizBinding
import com.example.twowaits.viewmodels.quizViewModels.QuizViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
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
        val bottomNavigationView =
            activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.visibility = View.GONE
        val drawerLayout = activity?.findViewById<DrawerLayout>(R.id.drawerLayout)
        drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        var chosenOptionId = 0
        val attemptQuizBody = AttemptQuizBody(Data.QUIZ_ID)

        if (Data.CHOSEN_OPTION[Data.CURRENT_QUESTION] != null) {
            when (Data.CHOSEN_OPTION[Data.CURRENT_QUESTION]) {
                1 -> binding.option1.isChecked = true
                2 -> binding.option2.isChecked = true
                3 -> binding.option3.isChecked = true
                4 -> binding.option4.isChecked = true
            }
        }

        if (Data.FIRST_TIME) {
            Data.FIRST_TIME = false

            val dialog = Dialog(requireContext())
            dialog.setContentView(PleaseWaitDialog2Binding.inflate(layoutInflater).root)
            dialog.setCancelable(false)
            dialog.show()
            if (dialog.window != null)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))

            viewModel.getQuizData(attemptQuizBody)
            viewModel.getQuizLiveData.observe(viewLifecycleOwner) {
                Data.QUIZ_DATA = it
                Data.TIME_LIMIT = it.time_limit
                viewModel.attemptQuiz(attemptQuizBody)
                viewModel.attemptQuizLiveData.observe(viewLifecycleOwner) { response ->
                    Data.startTimer(it.time_limit)
                    Data.QUIZ_RESULT_ID = response.quiz_result_id
                    Data.TITLE_OF_QUIZ = it.title
                    binding.Title.text = Data.TITLE_OF_QUIZ
                    binding.QuestionNo.text = "Q${Data.CURRENT_QUESTION + 1}."
                    binding.Question.text =
                        Data.QUIZ_DATA.question[Data.CURRENT_QUESTION].question_text
                    dialog.hide()

                    for (i in Data.QUIZ_DATA.question[Data.CURRENT_QUESTION].option.indices) {
                        val optionText =
                            Data.QUIZ_DATA.question[Data.CURRENT_QUESTION].option[i].option
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
                                    Data.QUIZ_DATA.question[Data.CURRENT_QUESTION].option[0].option_id
                                Data.CHOSEN_OPTION[Data.CURRENT_QUESTION] = 1
                            }
                            R.id.option2 -> {
                                chosenOptionId =
                                    Data.QUIZ_DATA.question[Data.CURRENT_QUESTION].option[1].option_id
                                Data.CHOSEN_OPTION[Data.CURRENT_QUESTION] = 2
                            }
                            R.id.option3 -> {
                                chosenOptionId =
                                    Data.QUIZ_DATA.question[Data.CURRENT_QUESTION].option[2].option_id
                                Data.CHOSEN_OPTION[Data.CURRENT_QUESTION] = 3
                            }
                            R.id.option4 -> {
                                chosenOptionId =
                                    Data.QUIZ_DATA.question[Data.CURRENT_QUESTION].option[3].option_id
                                Data.CHOSEN_OPTION[Data.CURRENT_QUESTION] = 4
                            }
                        }
                    }
                }
                viewModel.errorAttemptQuizLiveData.observe(viewLifecycleOwner) { errorMessage ->
                    if (errorMessage == "Quiz already attempted") {
                        dialog.hide()
                        Data.FIRST_TIME = true
                        Data.time = 0
                        findNavController().navigate(R.id.action_quiz_to_quizResult)
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
            binding.Title.text = Data.TITLE_OF_QUIZ
            binding.QuestionNo.text = "Q${Data.CURRENT_QUESTION + 1}"
            binding.Question.text =
                Data.QUIZ_DATA.question[Data.CURRENT_QUESTION].question_text

            for (i in Data.QUIZ_DATA.question[Data.CURRENT_QUESTION].option.indices) {
                val optionText =
                    Data.QUIZ_DATA.question[Data.CURRENT_QUESTION].option[i].option
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

            if (Data.CURRENT_QUESTION == Data.QUIZ_DATA.question.size - 1)
                binding.NextBtn.text = "Complete"
            else
                binding.NextBtn.text = "Next"
            binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.option1 -> {
                        chosenOptionId =
                            Data.QUIZ_DATA.question[Data.CURRENT_QUESTION].option[0].option_id
                        Data.CHOSEN_OPTION[Data.CURRENT_QUESTION] = 1
                    }
                    R.id.option2 -> {
                        chosenOptionId =
                            Data.QUIZ_DATA.question[Data.CURRENT_QUESTION].option[1].option_id
                        Data.CHOSEN_OPTION[Data.CURRENT_QUESTION] = 2
                    }
                    R.id.option3 -> {
                        chosenOptionId =
                            Data.QUIZ_DATA.question[Data.CURRENT_QUESTION].option[2].option_id
                        Data.CHOSEN_OPTION[Data.CURRENT_QUESTION] = 3
                    }
                    R.id.option4 -> {
                        chosenOptionId =
                            Data.QUIZ_DATA.question[Data.CURRENT_QUESTION].option[3].option_id
                        Data.CHOSEN_OPTION[Data.CURRENT_QUESTION] = 4
                    }
                }
            }
        }

        Data.timeLeftLiveData.observe(viewLifecycleOwner) {
            val timeLimit =
                if (Data.TIME_LIMIT == 1) "${Data.TIME_LIMIT} min" else "${Data.TIME_LIMIT} mins"
            val min = if (it / 60 == 1) "${it / 60} min" else "${it / 60} mins"
            val sec = if (it % 60 == 1) "${it % 60} second" else "${it % 60} seconds"

            binding.TimeLeft.text = "You have spent $min $sec out of ${timeLimit}"
        }
        Data.timeFinishedLiveData.observe(viewLifecycleOwner) {
            if (chosenOptionId != 0) {
                val registerResponseBody = RegisterResponseBody(
                    Data.QUIZ_RESULT_ID,
                    Data.QUIZ_DATA.question[Data.CURRENT_QUESTION].quiz_question_id,
                    listOf(OptionXX(chosenOptionId))
                )
                viewModel.registerResponse(registerResponseBody)
                viewModel.registerResponseLiveData.observe(viewLifecycleOwner) {
                    Data.FIRST_TIME = true
                    Data.CURRENT_QUESTION = 0
                    Data.CHOSEN_OPTION.clear()
                    binding.NextBtn.isEnabled = true
                    binding.PreviousBtn.isEnabled = true
                    Data.time = 0
                    findNavController().navigate(R.id.action_quiz_to_quizResult)
                }
                viewModel.errorRegisterResponseLiveData.observe(viewLifecycleOwner) {
                    Toast.makeText(
                        context,
                        it + "\nSorry! We were unable to evaluate this question",
                        Toast.LENGTH_SHORT
                    ).show()
                    Data.FIRST_TIME = true
                    Data.CURRENT_QUESTION = 0
                    Data.CHOSEN_OPTION.clear()
                    binding.NextBtn.isEnabled = true
                    binding.PreviousBtn.isEnabled = true
                    Data.time = 0
                    findNavController().navigate(R.id.action_quiz_to_quizResult)
                }
            } else {
                Data.FIRST_TIME = true
                Data.CURRENT_QUESTION = 0
                Data.CHOSEN_OPTION.clear()
                binding.NextBtn.isEnabled = true
                binding.PreviousBtn.isEnabled = true
                Data.time = 0
                findNavController().navigate(R.id.action_quiz_to_quizResult)
            }
        }

        binding.NextBtn.setOnClickListener {
            binding.NextBtn.isEnabled = false
            binding.PreviousBtn.isEnabled = false

            if (Data.CURRENT_QUESTION < Data.QUIZ_DATA.question.size - 1) {
                if (chosenOptionId != 0) {
                    val registerResponseBody = RegisterResponseBody(
                        Data.QUIZ_RESULT_ID,
                        Data.QUIZ_DATA.question[Data.CURRENT_QUESTION].quiz_question_id,
                        listOf(OptionXX(chosenOptionId))
                    )
                    viewModel.registerResponse(registerResponseBody)
                    viewModel.registerResponseLiveData.observe(viewLifecycleOwner) {
                        Data.CURRENT_QUESTION++
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
                    Data.CURRENT_QUESTION++
                    binding.NextBtn.isEnabled = true
                    binding.PreviousBtn.isEnabled = true
                    findNavController().navigate(R.id.action_quiz_self)
                }
            } else {
                if (chosenOptionId != 0) {
                    val registerResponseBody = RegisterResponseBody(
                        Data.QUIZ_RESULT_ID,
                        Data.QUIZ_DATA.question[Data.CURRENT_QUESTION].quiz_question_id,
                        listOf(OptionXX(chosenOptionId))
                    )
                    viewModel.registerResponse(registerResponseBody)
                    viewModel.registerResponseLiveData.observe(viewLifecycleOwner) {
                        Data.FIRST_TIME = true
                        Data.CURRENT_QUESTION = 0
                        Data.CHOSEN_OPTION.clear()
                        binding.NextBtn.isEnabled = true
                        binding.PreviousBtn.isEnabled = true
                        Data.timerCountDownTimer.cancel()
                        Data.time = 0
                        findNavController().navigate(R.id.action_quiz_to_quizResult)
                    }
                    viewModel.errorRegisterResponseLiveData.observe(viewLifecycleOwner) {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        binding.NextBtn.isEnabled = true
                        binding.PreviousBtn.isEnabled = true
                    }
                } else {
                    Data.FIRST_TIME = true
                    Data.CURRENT_QUESTION = 0
                    Data.CHOSEN_OPTION.clear()
                    binding.NextBtn.isEnabled = true
                    binding.PreviousBtn.isEnabled = true
                    Data.timerCountDownTimer.cancel()
                    Data.time = 0
                    findNavController().navigate(R.id.action_quiz_to_quizResult)
                }
            }
        }

        if (Data.CURRENT_QUESTION == 0)
            binding.PreviousBtn.visibility = View.GONE
        else
            binding.PreviousBtn.visibility = View.VISIBLE

        binding.PreviousBtn.setOnClickListener {
            binding.NextBtn.isEnabled = false
            binding.PreviousBtn.isEnabled = false

            if (Data.CURRENT_QUESTION > 0) {
                if (chosenOptionId != 0) {
                    val registerResponseBody = RegisterResponseBody(
                        Data.QUIZ_RESULT_ID,
                        Data.QUIZ_DATA.question[Data.CURRENT_QUESTION].quiz_question_id,
                        listOf(OptionXX(chosenOptionId))
                    )
                    viewModel.registerResponse(registerResponseBody)
                    viewModel.registerResponseLiveData.observe(viewLifecycleOwner) {
                        Data.CURRENT_QUESTION--
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
                    Data.CURRENT_QUESTION--
                    binding.NextBtn.isEnabled = true
                    binding.PreviousBtn.isEnabled = true
                    findNavController().navigate(R.id.action_quiz_self)
                }
            }
        }

        binding.Clear.setOnClickListener {
            binding.radioGroup.clearCheck()
            Data.CHOSEN_OPTION.remove(Data.CURRENT_QUESTION)
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Toast.makeText(
                    context,
                    "You cannot exit the quiz without submitting! Use next and previous buttons to navigate between questions",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}