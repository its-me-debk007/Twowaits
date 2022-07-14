package com.example.twowaits.homePages.quiz

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.twowaits.R
import com.example.twowaits.databinding.AddQuestionsBinding
import com.example.twowaits.viewmodel.quizViewModel.AddQuestionsViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class AddQuestions : Fragment(R.layout.add_questions) {
    private lateinit var binding: AddQuestionsBinding
    private val dropdownItems = mutableListOf<String>()

    override fun onResume() {
        super.onResume()
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, dropdownItems)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AddQuestionsBinding.bind(view)
        val viewModel = ViewModelProvider(this)[AddQuestionsViewModel::class.java]
        val options = mutableListOf<Option>()
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = "Add Questions"
        var questionsLeft = AddQuestionsArgs.fromBundle(requireArguments()).noOfQuestions
        val quizId = AddQuestionsArgs.fromBundle(requireArguments()).quizId

        if (questionsLeft == 1)
            binding.CreateAndAddQuestions.text = "Finish"

        binding.AddBtn.setOnClickListener {
            if (binding.Option.text.isNullOrEmpty()) {
                binding.OptionsForQuestions.helperText = "Please enter an option"
                return@setOnClickListener
            }
            binding.OptionsForQuestions.helperText = ""
            viewModel.optionCount++

            when (viewModel.optionCount) {
                1 -> {
                    binding.Option1.text = "(A) ${binding.Option.text?.trim().toString()}"
                    binding.Option1.visibility = View.VISIBLE
                    dropdownItems.add("(A) ${binding.Option.text?.trim().toString()}")
                    options.add(Option(binding.Option.text?.trim().toString()))
                }
                2 -> {
                    var flag = false
                    for (itr in options) {
                        if (binding.Option.text?.trim().toString() == itr.option) {
                            flag = true
                            break
                        }
                    }
                    if (flag) {
                        Toast.makeText(
                            context,
                            "No two options must be same",
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.optionCount--
                    } else {
                        binding.Option2.text = "(B) ${binding.Option.text?.trim().toString()}"
                        binding.Option2.visibility = View.VISIBLE
                        dropdownItems.add("(B) ${binding.Option.text?.trim().toString()}")
                        options.add(Option(binding.Option.text?.trim().toString()))
                    }
                }
                3 -> {
                    var flag = false
                    for (itr in options) {
                        if (binding.Option.text?.trim().toString() == itr.option) {
                            flag = true
                            break
                        }
                    }
                    if (flag) {
                        Toast.makeText(
                            context,
                            "No two options must be same",
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.optionCount--
                    } else {
                        binding.Option3.text = "(C) ${binding.Option.text?.trim().toString()}"
                        binding.Option3.visibility = View.VISIBLE
                        dropdownItems.add("(C) ${binding.Option.text?.trim().toString()}")
                        options.add(Option(binding.Option.text?.trim().toString()))
                    }
                }
                4 -> {
                    var flag = false
                    for (itr in options) {
                        if (binding.Option.text?.trim().toString() == itr.option) {
                            flag = true
                            break
                        }
                    }
                    if (flag) {
                        Toast.makeText(
                            context,
                            "No two options must be same",
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.optionCount--
                    } else {
                        binding.Option4.text = "(D) ${binding.Option.text?.trim().toString()}"
                        binding.Option4.visibility = View.VISIBLE
                        dropdownItems.add("(D) ${binding.Option.text?.trim().toString()}")
                        options.add(Option(binding.Option.text?.trim().toString()))
                    }
                }
            }
            binding.Option.text?.clear()
        }

        binding.CreateAndAddQuestions.setOnClickListener {
            when {
                dropdownItems.size < 2 -> {
                    Toast.makeText(
                        context,
                        "Please enter at least 2 options first",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                binding.autoCompleteTextView.text.isNullOrEmpty() -> {
                    Toast.makeText(context, "Please choose an answer", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            val createQuestionBody = CreateQuestionBody(
                quizId,
                binding.QuestionForQuiz.text.toString(),
                options
            )
            viewModel.createQuestion(createQuestionBody)
            viewModel.addQuestionsLiveData.observe(viewLifecycleOwner) { response ->

                val questionId = response.question.last().quiz_question_id
                val correctOptions = mutableListOf<CorrectOption>()
                val correctOptionText = binding.autoCompleteTextView.text.toString().substring(4)

                response.question.last().option.forEach { optionIterator ->
                    if (optionIterator.option == correctOptionText)
                        correctOptions.add(CorrectOption(optionIterator.option_id))
                }
                val addCorrectOptionBody = AddCorrectOptionBody(questionId, correctOptions)
                viewModel.addCorrectOption(addCorrectOptionBody)

                viewModel.addCorrectOptionLiveData.observe(viewLifecycleOwner) {
                    questionsLeft--
                    if (questionsLeft == 0) {
                        Toast.makeText(
                            context,
                            "You have successfully created your quiz",
                            Toast.LENGTH_SHORT
                        ).show()
                        activity?.finish()
                    } else {
                        val action =
                            AddQuestionsDirections.actionAddQuestions2Self(questionsLeft, quizId)
                        findNavController().navigate(action)
                    }
                }
                viewModel.errorAddCorrectOptionLiveData.observe(
                    viewLifecycleOwner
                ) { errorMessage ->
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            viewModel.errorAddQuestionsLiveData.observe(viewLifecycleOwner) { errorMessage ->
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Toast.makeText(
                    context,
                    "Complete creating your quiz first",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}