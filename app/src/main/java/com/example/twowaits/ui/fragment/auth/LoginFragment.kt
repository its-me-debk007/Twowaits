package com.example.twowaits.ui.fragment.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.twowaits.R
import com.example.twowaits.databinding.FragmentLoginBinding
import com.example.twowaits.model.auth.LoginBody
import com.example.twowaits.repository.authRepository.LoginRepository
import com.example.twowaits.sealedClass.Response
import com.example.twowaits.ui.activity.home.HomeActivity
import com.example.twowaits.util.*
import com.example.twowaits.viewModel.AuthViewModel
import com.example.twowaits.viewModelFactory.AuthViewModelFactory
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by lazy {
        ViewModelProvider(this, AuthViewModelFactory(LoginRepository()))[AuthViewModel::class.java]
    }
    private val datastore by lazy { Datastore(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        binding.apply {

            signUp.setOnClickListener { findNavController().navigate(R.id.action_login3_to_signUp) }

            forgotPassword.setOnClickListener {
                findNavController().navigate(R.id.action_login3_to_verifyEmail)
            }

            logIn.setOnClickListener {
                textInputLayout2.helperText = ""
                textInputLayout.helperText = ""

                val userEmail = EmailAddress.text.toString().trim()
                val userPassword = password.text.toString()
                if (!userEmail.isValidEmail()) {
                    textInputLayout2.helperText = "Please enter a valid email"
                    return@setOnClickListener
                }
                if (userPassword.isEmpty()) {
                    textInputLayout.helperText = "Please enter your Password!"
                    return@setOnClickListener
                }

                requireView().hideKeyboard(activity)
                binding.progressBar.visibility = View.VISIBLE
                logIn.text = ""
                logIn.isEnabled = false

                viewModel.login(LoginBody(userEmail, userPassword))
                viewModel.loginLiveData.observe(viewLifecycleOwner) { loginResponse ->

                    if (loginResponse is Response.Success) {
                        viewModel.getAuthTokens(userEmail, userPassword)
                        viewModel.tokenLiveData.observe(viewLifecycleOwner) { tokensResponse ->
                            if (tokensResponse is Response.Success) {

                                if (loginResponse.data?.type == "faculty") {
                                    USER = "FACULTY"
                                    lifecycleScope.launch {
                                        datastore.saveLoginData("FACULTY")
                                    }
                                } else {
                                    USER = "STUDENT"
                                    lifecycleScope.launch {
                                        datastore.saveLoginData("STUDENT")
                                    }
                                }

                                lifecycleScope.launch {
                                    datastore.saveAccessToken(tokensResponse.data!!.access)
                                    datastore.saveRefreshToken(tokensResponse.data.refresh!!)
                                    datastore.saveUserDetails("USER_EMAIL", userEmail)
                                }
                                ACCESS_TOKEN = tokensResponse.data!!.access
                                REFRESH_TOKEN = tokensResponse.data.refresh

                                startActivity(Intent(activity, HomeActivity::class.java))
                                activity?.finish()
                            } else {
                                logIn.snackBar(tokensResponse.errorMessage!!)
                                password.text?.clear()
                                logIn.isEnabled = true
                                logIn.text = "Log In"
                                progressBar.visibility = View.INVISIBLE
                            }
                        }
                    } else {
                        logIn.snackBar(loginResponse.errorMessage!!)
                        password.text?.clear()
                        logIn.isEnabled = true
                        logIn.text = "Log In"
                        progressBar.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_login_to_firstAuthPage)
            }
        })
    }
}