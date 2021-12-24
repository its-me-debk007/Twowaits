package com.example.twowaits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.twowaits.databinding.LoginBinding
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.login.view.*

class Login: Fragment(R.layout.login) {
    private var _binding: LoginBinding? = null
    private val binding get() = _binding!!

    fun isValidEmail(str: String): Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginBinding.inflate(inflater, container, false)
        binding.signUpLink.setOnClickListener{
            Navigation.findNavController(binding.root).navigate(R.id.action_login3_to_signUp)
        }
        binding.ForgotPassword.setOnClickListener{
            Navigation.findNavController(binding.root).navigate(R.id.action_login3_to_verifyEmail)
        }
        binding.LogInButton.setOnClickListener {
            val userEmail = binding.EmailAddress.text.toString().trim()
            if(!isValidEmail(userEmail)){
                binding.EmailAddress.error="Please enter a valid email"
                return@setOnClickListener
            }
            if(binding.Password.text.toString().isEmpty()) {
                binding.Password.error = "Please enter your Password!"
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