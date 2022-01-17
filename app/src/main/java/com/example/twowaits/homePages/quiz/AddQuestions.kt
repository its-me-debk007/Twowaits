package com.example.twowaits.homePages.quiz

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.twowaits.CompanionObjects
import com.example.twowaits.R
import com.example.twowaits.databinding.AddQuestionsBinding
import com.example.twowaits.recyclerAdapters.OptionsRecyclerAdapter
import com.example.twowaits.recyclerAdapters.YourQuestionsRecyclerAdapter
import com.example.twowaits.viewmodels.AddQuestionsViewModel

class AddQuestions : Fragment() {
    private var _binding: AddQuestionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddQuestionsBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this)[AddQuestionsViewModel::class.java]
        val data = mutableListOf<OptionsDataClass>()
        val dropdownItems = mutableListOf<String>()
        val optionsList = mutableListOf<OptionsForRetrofit>()

        binding.OptionsRecyclerView.adapter = OptionsRecyclerAdapter()
        binding.OptionsRecyclerView.layoutManager = LinearLayoutManager(container?.context)
//        OptionsRecyclerAdapter().submitList(data)
        binding.AddBtn.setOnClickListener {
            viewModel.optionCount++
//            optionsSerialNo.add("(${viewModel.optionCount})")
//            options.add(binding.Option.text?.trim().toString())
//            when (viewModel.optionCount){
//                1 -> {
//                    binding.Option1.text = "(A) ${binding.Option.text?.trim().toString()}"
//                    binding.Option1.visibility = View.VISIBLE
//                    dropdownItems.add("(A) ${binding.Option.text?.trim().toString()}")
//                    optionsList.add(OptionsForRetrofit(binding.Option.text?.trim().toString()))
//                    Log.d("dropdown", dropdownItems[dropdownItems.size - 1])
//                }
//                2 -> {
//                    binding.Option2.text = "(B) ${binding.Option.text?.trim().toString()}"
//                    binding.Option2.visibility = View.VISIBLE
//                    dropdownItems.add("(B) ${binding.Option.text?.trim().toString()}")
//                    optionsList.add(OptionsForRetrofit(binding.Option.text?.trim().toString()))
//                }
//                3 -> {
//                    binding.Option3.text = "(C) ${binding.Option.text?.trim().toString()}"
//                    binding.Option3.visibility = View.VISIBLE
//                    dropdownItems.add("(C) ${binding.Option.text?.trim().toString()}")
//                    optionsList.add(OptionsForRetrofit(binding.Option.text?.trim().toString()))
//                }
//                4 -> {
//                    binding.Option4.text = "(D) ${binding.Option.text?.trim().toString()}"
//                    binding.Option4.visibility = View.VISIBLE
//                    dropdownItems.add("(D) ${binding.Option.text?.trim().toString()}")
//                    optionsList.add(OptionsForRetrofit(binding.Option.text?.trim().toString()))
//                }
//            }
            binding.Option.text?.clear()

            data.add(OptionsDataClass("(${viewModel.optionCount})", "${binding.Option.text?.trim()}"))
            Log.d("TAG", data[0].option)
            OptionsRecyclerAdapter().submitList(data)
        }
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, dropdownItems)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        binding.CreateAndAddQuestions.setOnClickListener {
            OptionsForRetrofit("")
            viewModel.createQuestion(CompanionObjects.ACCESS_TOKEN, CompanionObjects.QUIZ_ID, binding.QuestionForQuiz.text.toString(), optionsList)
            viewModel.addQuestionsLiveData.observe(viewLifecycleOwner, {
                Toast.makeText(context, "Added Q", Toast.LENGTH_SHORT).show()
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