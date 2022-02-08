package com.example.twowaits.homePages.navdrawerPages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.twowaits.R
import com.example.twowaits.databinding.FeedbackBinding
import com.example.twowaits.viewmodels.HomePageViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class Feedback : Fragment() {
    private var _binding: FeedbackBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FeedbackBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this)[HomePageViewModel::class.java]

        binding.submitBtn.setOnClickListener {
            when {
                !(binding.option1.isChecked || binding.option2.isChecked || binding.option3.isChecked || binding.option4.isChecked || binding.option5.isChecked) ->
                {
                    Toast.makeText(context, "Please select a rating", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                binding.feedback.text.toString().trim().isEmpty() ->{
                    binding.feedbackLayout . helperText = "Please add you feedback"
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
                    binding.submitBtn.isEnabled = true
                    binding.ProgressBar.visibility = View.GONE
                    findNavController().navigate(R.id.action_feedback2_to_homePage)
                    val bottomNavigationView =
                        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                    bottomNavigationView?.visibility = View.VISIBLE
                } else {
                    binding.submitBtn.isEnabled = true
                    binding.ProgressBar.visibility = View.GONE
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_feedback2_to_homePage)
                val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                bottomNavigationView?.visibility = View.VISIBLE
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}