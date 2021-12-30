package com.example.twowaits.authPages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.twowaits.authPages.SignUp.Companion.EMAIL
import com.example.twowaits.authPages.SignUp.Companion.PREVIOUS_PAGE
import com.example.twowaits.R
import com.example.twowaits.apiCalls.API
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.databinding.VerifyEmailBinding
import com.example.twowaits.repository.SendOtpRepository

class VerifyEmail : Fragment() {
    private var _binding: VerifyEmailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VerifyEmailBinding.inflate(inflater, container, false)

        binding.verifyButton.setOnClickListener {
            val api = RetrofitClient.getInstance().create(API::class.java)
            val repository = SendOtpRepository(api)

            val userEmail = binding.emailForVerifying.text.toString().trim()
            if(!Login().isValidEmail(userEmail)){
                binding.emailForVerifying.error="Please enter a valid email"
                return@setOnClickListener
            }

            EMAIL = userEmail
            repository.sendOtp(userEmail)
            binding.verifyButton.isEnabled = false
            binding.ProgressBar.visibility = View.VISIBLE
            repository.errorMutableLiveData.observe(viewLifecycleOwner, {
                if (it == "success"){
                    PREVIOUS_PAGE = "VerifyEmail"
                    Navigation.findNavController(binding.root).navigate(R.id.action_verifyEmail_to_otpVerification)
                }
                else{
                    Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                    binding.verifyButton.isEnabled = true
                    binding.ProgressBar.visibility = View.INVISIBLE
                }
            })
        }
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}