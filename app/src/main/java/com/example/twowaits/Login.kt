package com.example.twowaits

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.twowaits.apiCalls.API
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.databinding.LoginBinding
import com.example.twowaits.repository.BaseRepository
import com.example.twowaits.viewmodels.MainViewModel
import com.example.twowaits.viewmodels.MainViewModelFactory

class Login: Fragment() {
    private var _binding: LoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel

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
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]
        mainViewModel.facts.observe(viewLifecycleOwner, Observer {
            Log.d("RETROFIT_IMPLEMENTED", "${it.fact}\nWith a length of ${it.length.toString()}")
        })

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