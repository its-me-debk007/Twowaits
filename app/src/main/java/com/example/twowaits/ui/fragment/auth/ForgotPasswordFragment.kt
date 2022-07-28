package com.example.twowaits.ui.fragment.auth

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.twowaits.R
import com.example.twowaits.databinding.VerifyEmailBinding
import com.example.twowaits.repository.authRepository.SendOtpRepository
import com.example.twowaits.util.isValidEmail
import com.example.twowaits.util.snackBar

class ForgotPasswordFragment : Fragment(R.layout.verify_email) {
    private lateinit var binding: VerifyEmailBinding
    val repository by lazy { SendOtpRepository() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = VerifyEmailBinding.bind(view)
        binding.verifyButton.setOnClickListener {

            val userEmail = binding.emailForVerifying.text.toString().trim()
            if (!userEmail.isValidEmail()) {
                binding.emailForVerifying.error = "Please enter a valid email"
                return@setOnClickListener
            }
            binding.verifyButton.isEnabled = false
            binding.ProgressBar.visibility = View.VISIBLE
            repository.sendOtp(userEmail).observe(viewLifecycleOwner) {
                if (it.data == "success") {
                    binding.verifyButton.isEnabled = true
                    binding.ProgressBar.visibility = View.INVISIBLE
                    binding.emailForVerifying.text?.clear()
                    val action =
                        ForgotPasswordFragmentDirections.actionVerifyEmailToOtpVerification(
                            userEmail, "", "VerifyEmail"
                        )
                    findNavController().navigate(action)
                } else {
                    binding.verifyButton.snackBar(it.errorMessage!!)
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