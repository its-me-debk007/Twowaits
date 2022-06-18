package com.example.twowaits.ui.fragments.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.twowaits.R
import com.example.twowaits.databinding.VerifyEmailBinding
import com.example.twowaits.repositories.authRepositories.SendOtpRepository

class ForgotPasswordFragment : Fragment(R.layout.verify_email) {
    private lateinit var binding: VerifyEmailBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = VerifyEmailBinding.bind(view)
        binding.verifyButton.setOnClickListener {
            val repository = SendOtpRepository()
            val userEmail = binding.emailForVerifying.text.toString().trim()
            if (!LoginFragment().isValidEmail(userEmail)) {
                binding.emailForVerifying.error = "Please enter a valid email"
                return@setOnClickListener
            }
            repository.sendOtp(userEmail)
            binding.verifyButton.isEnabled = false
            binding.ProgressBar.visibility = View.VISIBLE
            repository.errorMutableLiveData.observe(viewLifecycleOwner) {
                if (it == "success") {
                    binding.verifyButton.isEnabled = true
                    binding.ProgressBar.visibility = View.INVISIBLE
                    binding.emailForVerifying.text?.clear()
                    val action = ForgotPasswordFragmentDirections.actionVerifyEmailToOtpVerification(
                        userEmail, "", "VerifyEmail")
                    findNavController().navigate(action)
                } else {
                    Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                    binding.verifyButton.isEnabled = true
                    binding.ProgressBar.visibility = View.INVISIBLE
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_verifyEmail_to_login)
            }
        })
    }
}