package com.example.twowaits.ui.fragment.navDrawer

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.twowaits.R
import com.example.twowaits.databinding.FragmentFeedbackBinding
import com.example.twowaits.viewModel.HomePageViewModel

class FeedbackFragment : Fragment(R.layout.fragment_feedback) {
    private lateinit var binding: FragmentFeedbackBinding
    private val viewModel by lazy { ViewModelProvider(this)[HomePageViewModel::class.java] }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFeedbackBinding.bind(view)
        binding.submitBtn.setOnClickListener {
            binding.feedbackLayout.helperText = ""
            if (binding.feedback.text.toString().trim().isEmpty()) {
                binding.feedbackLayout.helperText = "Please add you feedback"
                return@setOnClickListener
            }

            val choose = when {
                binding.option1.isChecked -> 1
                binding.option2.isChecked -> 2
                binding.option3.isChecked -> 3
                binding.option4.isChecked -> 4
                else -> 5
            }
            viewModel.feedback(binding.feedback.text.toString().trim(), choose)
            binding.submitBtn.isEnabled = false
            binding.submitBtn.text = null
            binding.progressBar.visibility = View.VISIBLE
            viewModel.feedbackLiveData.observe(viewLifecycleOwner) {
                if (it.data == "success") {
                    Toast.makeText(
                        context, "Thanks for sharing your feedback!",
                        Toast.LENGTH_SHORT
                    ).show()
                    activity?.finish()
                } else {
                    binding.submitBtn.isEnabled = true
                    binding.submitBtn.text = "Submit"
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}