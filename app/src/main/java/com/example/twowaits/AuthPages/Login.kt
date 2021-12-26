package com.example.twowaits.AuthPages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.twowaits.R
import com.example.twowaits.apiCalls.API
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.databinding.LoginBinding
import com.example.twowaits.repository.BaseRepository
import com.example.twowaits.viewmodels.LoginViewModel
import com.example.twowaits.viewmodels.LoginViewModelFactory

class Login: Fragment() {
    private var _binding: LoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var loginViewModel: LoginViewModel

    fun isValidEmail(str: String): Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginBinding.inflate(inflater, container, false)
        val api = RetrofitClient.getInstance().create(API::class.java)
        val repository = BaseRepository(api)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(repository))[LoginViewModel::class.java]

        binding.signUpLink.setOnClickListener{
            Navigation.findNavController(binding.root).navigate(R.id.action_login3_to_signUp)
        }
        binding.ForgotPassword.setOnClickListener{
            Navigation.findNavController(binding.root).navigate(R.id.action_login3_to_verifyEmail)
        }
        binding.LogInButton.setOnClickListener {
            val userEmail = binding.EmailAddress.text.toString().trim()
            val userPassword = binding.Password.text.toString()

            if(!isValidEmail(userEmail)){
                binding.EmailAddress.error="Please enter a valid email"
                return@setOnClickListener
            }
            if(userPassword.isEmpty()) {
                binding.Password.error = "Please enter your Password!"
                return@setOnClickListener
            }

            loginViewModel.gettingFacts()
            loginViewModel.facts.observe(viewLifecycleOwner, {
                Log.d("RETROFIT_IMPLEMENTED", "Implemented")
                binding.Heading.text = "${it.fact}\nWith a length of ${it.length}"
            })
        }
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}