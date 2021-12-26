package com.example.twowaits.AuthPages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.twowaits.R
import com.example.twowaits.databinding.SignUpBinding

class SignUp : Fragment() {
    private var _binding: SignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SignUpBinding.inflate(inflater, container, false)
        binding.loginLink.setOnClickListener{
            Navigation.findNavController(binding.root).navigate(R.id.action_signUp_to_login3)
        }
        binding.VerifyEmailButton.setOnClickListener {
            val userEmail = binding.EnterEmail.text.toString().trim()
            if(binding.EnterName.text.toString().isEmpty()) {
                binding.EnterName.error = "Please enter your name!"
                return@setOnClickListener
            }
            if(!Login().isValidEmail(userEmail)){
                binding.EnterEmail.error="Please enter a valid email!"
                return@setOnClickListener
            }
        }


        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}