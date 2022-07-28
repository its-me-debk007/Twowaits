package com.example.twowaits.ui.fragment.auth

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.twowaits.R
import com.example.twowaits.databinding.FragmentSignUpBinding
import com.example.twowaits.repository.authRepository.SignUpRepository
import com.example.twowaits.util.hideKeyboard
import com.example.twowaits.util.isValidEmail
import com.example.twowaits.util.isValidPassword
import com.example.twowaits.util.snackBar
import com.example.twowaits.viewModel.AuthViewModel
import com.example.twowaits.viewModelFactory.AuthViewModelFactory
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {
    private lateinit var binding: FragmentSignUpBinding
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            AuthViewModelFactory(signUpRepository = SignUpRepository())
        )[AuthViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignUpBinding.bind(view)
        binding.loginLink.setOnClickListener {
            findNavController().navigate(R.id.action_signUp_to_login3)
        }
        binding.signUpBtn.setOnClickListener {
            requireView().hideKeyboard(activity)
            val userEmail = binding.EnterEmail.text.toString().trim()

            if (!userEmail.isValidEmail()) {
                binding.EnterEmail.error = "Please enter a valid email!"
                return@setOnClickListener
            }

            if (binding.password.text.toString().isValidPassword() != null) {
                binding.textInputLayout.helperText =
                    binding.password.text.toString().isValidPassword()
                return@setOnClickListener
            }
            binding.textInputLayout.helperText = ""

            if (binding.confirmPassword.text.toString() != binding.password.text.toString()) {
                binding.textInputLayout3.helperText = "The two passwords do not match"
                return@setOnClickListener
            }
            binding.textInputLayout3.helperText = ""

            viewModel.signUp(userEmail, binding.confirmPassword.text.toString())
            binding.signUpBtn.isEnabled = false
            binding.signUpBtn.text = ""
            binding.progressBar.visibility = View.VISIBLE

            viewModel.signUpLiveData.observe(viewLifecycleOwner) {
                if (it.data == "success") {
                    val action = SignUpFragmentDirections.actionSignUpToOtpVerification(
                        userEmail,
                        binding.confirmPassword.text.toString(), "SignUp"
                    )
                    binding.EnterEmail.text?.clear()
                    clearFields()
                    findNavController().navigate(action)
                } else {
                    binding.signUpBtn.snackBar(it.errorMessage!!)
                    clearFields()
                }
            }
        }
    }

    private fun clearFields() {
        binding.password.text?.clear()
        binding.confirmPassword.text?.clear()
        binding.signUpBtn.isEnabled = true
        binding.signUpBtn.text = "Sign Up"
        binding.progressBar.visibility = View.INVISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_signUp_to_firstAuthPage)
            }
        })
    }
}