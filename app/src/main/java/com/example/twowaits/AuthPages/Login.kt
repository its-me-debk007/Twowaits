package com.example.twowaits.AuthPages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.twowaits.HomeActivity
import com.example.twowaits.R
import com.example.twowaits.apiCalls.API
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.databinding.LoginBinding
import com.example.twowaits.repository.BaseRepository
import com.example.twowaits.repository.LoginRepository
import com.example.twowaits.viewmodels.LoginViewModel
import com.example.twowaits.viewmodels.LoginViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

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

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        binding.signUpLink.setOnClickListener{
            Navigation.findNavController(binding.root).navigate(R.id.action_login3_to_signUp)
        }
        binding.ForgotPassword.setOnClickListener{
            Navigation.findNavController(binding.root).navigate(R.id.action_login3_to_verifyEmail)
        }
        binding.LogInButton.setOnClickListener {
            val api = RetrofitClient.getInstance().create(API::class.java)
            val repository = LoginRepository(api)

            val userEmail = binding.EmailAddress.text.toString().trim()
            val userPassword = binding.Password.text.toString()

            if(!isValidEmail(userEmail)){
                binding.EmailAddress.error="Please enter a valid email"
                return@setOnClickListener
            }
            if(userPassword.isEmpty()) {
                binding.textInputLayout.helperText = "Please enter your Password!"
                return@setOnClickListener
            }
            binding.textInputLayout.helperText = ""

            loginViewModel.login(userEmail, userPassword)
            binding.ProgressBar.visibility = View.VISIBLE
            binding.LogInButton.isEnabled = false

            var flag = false
            loginViewModel.errorLiveData.observe(viewLifecycleOwner, {
                if (it == "success"){
                    val intent = Intent(activity, HomeActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                    flag = true
                    binding.Password.text?.clear()
                    binding.LogInButton.isEnabled = true
                    binding.ProgressBar.visibility = View.INVISIBLE
                }
            })
            if (flag)
                return@setOnClickListener
        }
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestEmail()
//            .build()
//        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//        val account = GoogleSignIn.getLastSignedInAccount(this)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}