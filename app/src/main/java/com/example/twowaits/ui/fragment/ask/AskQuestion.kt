package com.example.twowaits.ui.fragment.ask

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.twowaits.R
import com.example.twowaits.databinding.AskQuestionBinding
import com.example.twowaits.model.AskQuestionBody
import com.example.twowaits.util.hideKeyboard
import com.example.twowaits.viewModel.questionsAnswersViewModel.QuestionsAnswersViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class AskQuestion : Fragment(R.layout.ask_question) {
    private lateinit var binding: AskQuestionBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AskQuestionBinding.bind(view)
        val viewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]

        binding.askBtn.setOnClickListener {
            if (binding.question.text.toString().trim().isEmpty()) {
                binding.questionLayout.helperText = "Enter a question"
                return@setOnClickListener
            }
            binding.questionLayout.helperText = ""
            requireView().hideKeyboard(activity)
            viewModel.askQuestion(AskQuestionBody(binding.question.text.toString()))
            binding.progressBar.visibility = View.VISIBLE
            binding.askBtn.isEnabled = false
            binding.askBtn.text = ""
            viewModel.askQuestionLiveData.observe(viewLifecycleOwner) {
                Toast.makeText(context, "Question asked successfully", Toast.LENGTH_SHORT).show()
                activity?.finish()
            }
            viewModel.errorAskQuestionLiveData.observe(viewLifecycleOwner) {
                binding.askBtn.isEnabled = true
                binding.askBtn.text = "Ask"
                binding.progressBar.visibility = View.INVISIBLE
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}