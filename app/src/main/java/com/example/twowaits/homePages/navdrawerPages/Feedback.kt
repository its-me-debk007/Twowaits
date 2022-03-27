package com.example.twowaits.homePages.navdrawerPages

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.twowaits.R
import com.example.twowaits.databinding.FeedbackBinding
import com.example.twowaits.viewmodels.HomePageViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class Feedback : Fragment(R.layout.feedback) {
    private lateinit var binding: FeedbackBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FeedbackBinding.bind(view)
        val viewModel = ViewModelProvider(this)[HomePageViewModel::class.java]

        binding.submitBtn.setOnClickListener {
            when {
                !(binding.option1.isChecked || binding.option2.isChecked || binding.option3.isChecked || binding.option4.isChecked || binding.option5.isChecked) -> {
                    Toast.makeText(context, "Please select a rating", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                binding.feedback.text.toString().trim().isEmpty() -> {
                    binding.feedbackLayout.helperText = "Please add you feedback"
                    return@setOnClickListener
                }
            }
            binding.feedbackLayout.helperText = ""
            val choose = when {
                binding.option1.isChecked -> 1
                binding.option2.isChecked -> 2
                binding.option3.isChecked -> 3
                binding.option4.isChecked -> 4
                binding.option5.isChecked -> 5
                else -> -1
            }
            viewModel.feedback(Feedbackbody(binding.feedback.text.toString().trim(), choose))
            binding.submitBtn.isEnabled = false
            binding.ProgressBar.visibility = View.VISIBLE
            viewModel.feedbackData.observe(viewLifecycleOwner) {
                if (it == "success") {
                    Toast.makeText(context, "Thanks for sharing your feedback!", Toast.LENGTH_SHORT)
                        .show()
                    activity?.finish()
                } else {
                    binding.submitBtn.isEnabled = true
                    binding.ProgressBar.visibility = View.GONE
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}