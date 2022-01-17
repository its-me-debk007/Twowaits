package com.example.twowaits.homePages.quiz

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.twowaits.CompanionObjects
import com.example.twowaits.R
import com.example.twowaits.databinding.AddQuestionsBinding
import com.example.twowaits.viewmodels.AddQuestionsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class AddQuestions : Fragment() {
    private var _binding: AddQuestionsBinding? = null
    private val binding get() = _binding!!
    private val dropdownItems = mutableListOf<String>()

    override fun onResume() {
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, dropdownItems)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddQuestionsBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this)[AddQuestionsViewModel::class.java]
        val data = mutableListOf<OptionsDataClass>()
        val options = mutableListOf<Option>()

        if (CompanionObjects.QUESTIONS_LEFT == 1)
            binding.CreateAndAddQuestions.text = "Finish"

//        binding.OptionsRecyclerView.adapter = OptionsRecyclerAdapter()
//        binding.OptionsRecyclerView.layoutManager = LinearLayoutManager(container?.context)
//        OptionsRecyclerAdapter().submitList(data)
        binding.AddBtn.setOnClickListener {
            if (binding.Option.text.isNullOrEmpty()){
                binding.OptionsForQuestions.helperText = "Please enter an option"
                return@setOnClickListener
            }
            binding.OptionsForQuestions.helperText = ""
            viewModel.optionCount++
//            optionsSerialNo.add("(${viewModel.optionCount})")
//            options.add(binding.Option.text?.trim().toString())
            when (viewModel.optionCount){
                1 -> {
                    binding.Option1.text = "(A) ${binding.Option.text?.trim().toString()}"
                    binding.Option1.visibility = View.VISIBLE
                    dropdownItems.add("(A) ${binding.Option.text?.trim().toString()}")
                    options.add(Option(binding.Option.text?.trim().toString()))
                }
                2 -> {
                    binding.Option2.text = "(B) ${binding.Option.text?.trim().toString()}"
                    binding.Option2.visibility = View.VISIBLE
                    dropdownItems.add("(B) ${binding.Option.text?.trim().toString()}")
                    options.add(Option(binding.Option.text?.trim().toString()))
                }
                3 -> {
                    binding.Option3.text = "(C) ${binding.Option.text?.trim().toString()}"
                    binding.Option3.visibility = View.VISIBLE
                    dropdownItems.add("(C) ${binding.Option.text?.trim().toString()}")
                    options.add(Option(binding.Option.text?.trim().toString()))
                }
                4 -> {
                    binding.Option4.text = "(D) ${binding.Option.text?.trim().toString()}"
                    binding.Option4.visibility = View.VISIBLE
                    dropdownItems.add("(D) ${binding.Option.text?.trim().toString()}")
                    options.add(Option(binding.Option.text?.trim().toString()))
                }
            }
            binding.Option.text?.clear()

//            data.add(OptionsDataClass("(${viewModel.optionCount})", "${binding.Option.text?.trim()}"))
//            Log.d("TAG", data[0].option)
//            OptionsRecyclerAdapter().submitList(data)
        }

        binding.CreateAndAddQuestions.setOnClickListener {
            when {
                dropdownItems.size < 2 -> {
                    Snackbar.make(it, "Please enter at least 2 options first", Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                binding.autoCompleteTextView.text.isNullOrEmpty() -> {
                    Snackbar.make(it, "Please choose an answer", Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            val createQuestionBody = CreateQuestionBody(CompanionObjects.QUIZ_ID, binding.QuestionForQuiz.text.toString(), options)
            viewModel.createQuestion(createQuestionBody)
            viewModel.addQuestionsLiveData.observe(viewLifecycleOwner, { response ->

                val questionId = response.question.last().quiz_question_id
                val correctOptions = mutableListOf<CorrectOption>()
                val correctOptionText = binding.autoCompleteTextView.text.toString().substring(4)

                response.question.last().option.forEach { optionIterator ->
                    if (optionIterator.option == correctOptionText)
                        correctOptions.add(CorrectOption(optionIterator.option_id))
                }
                val addCorrectOptionBody = AddCorrectOptionBody(questionId, correctOptions)
                Log.d("TAG", "${questionId}\n${correctOptions.size}\nCorrectOptionText is $correctOptionText")
                viewModel.addCorrectOption(addCorrectOptionBody)

                viewModel.addCorrectOptionLiveData.observe(viewLifecycleOwner, {
                    Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show()

                    CompanionObjects.QUESTIONS_LEFT--
                    if (CompanionObjects.QUESTIONS_LEFT == 0){
                        Toast.makeText(context, "Finished", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_addQuestions_to_createQuiz)
                    }
                    else
                        findNavController().navigate(R.id.action_addQuestions_to_addQuestions)
//                    binding.QuestionForQuiz.text?.clear()
//                    binding.Option.text?.clear()
//                    binding.Option1.visibility = View.GONE
//                    binding.Option2.visibility = View.GONE
//                    binding.Option3.visibility = View.GONE
//                    binding.Option4.visibility = View.GONE
//                    binding.autoCompleteTextView.setText("Enter the correct answer of question")
//                    viewModel.optionCount = 0
//                    dropdownItems.clear()
//                    options.clear()
                })
                viewModel.errorAddCorrectOptionLiveData.observe(viewLifecycleOwner, { errorMessage ->
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                })
            })

            viewModel.errorAddQuestionsLiveData.observe(viewLifecycleOwner, { errorMessage ->
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            })
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}