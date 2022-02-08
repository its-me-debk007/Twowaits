package com.example.twowaits.authPages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.example.twowaits.Data
import com.example.twowaits.R
import com.example.twowaits.databinding.VerifyEmailBinding
import com.example.twowaits.repository.authRepositories.SendOtpRepository
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class VerifyEmail : Fragment() {
    private var _binding: VerifyEmailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VerifyEmailBinding.inflate(inflater, container, false)

        binding.verifyButton.setOnClickListener {
            val repository = SendOtpRepository()
            val userEmail = binding.emailForVerifying.text.toString().trim()
            if(!Login().isValidEmail(userEmail)){
                binding.emailForVerifying.error="Please enter a valid email"
                return@setOnClickListener
            }
            repository.sendOtp(userEmail)
            binding.verifyButton.isEnabled = false
            binding.ProgressBar.visibility = View.VISIBLE
            repository.errorMutableLiveData.observe(viewLifecycleOwner) {
                if (it == "success") {
                    Data.EMAIL = userEmail
                    Data.PREVIOUS_PAGE = "VerifyEmail"
                    binding.verifyButton.isEnabled = true
                    binding.ProgressBar.visibility = View.INVISIBLE
                    binding.emailForVerifying.text?.clear()
                    findNavController().navigate(R.id.action_verifyEmail_to_otpVerification)
                } else {
                    Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                    binding.verifyButton.isEnabled = true
                    binding.ProgressBar.visibility = View.INVISIBLE
                }
            }
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_verifyEmail_to_login)
            }
        })
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}