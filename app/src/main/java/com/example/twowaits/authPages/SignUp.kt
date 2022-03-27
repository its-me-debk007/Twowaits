package com.example.twowaits.authPages

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.twowaits.R
import com.example.twowaits.databinding.SignUpBinding
import com.example.twowaits.repository.authRepositories.SignUpRepository
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class SignUp : Fragment(R.layout.sign_up) {
    private lateinit var binding: SignUpBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SignUpBinding.bind(view)
        binding.loginLink.setOnClickListener {
            findNavController().navigate(R.id.action_signUp_to_login3)
        }
        binding.SignUpButton.setOnClickListener {
            hideKeyboard(requireView())
            val repository = SignUpRepository()
            val userEmail = binding.EnterEmail.text.toString().trim()
            if (!Login().isValidEmail(userEmail)) {
                binding.EnterEmail.error = "Please enter a valid email!"
                return@setOnClickListener  }
            var flagLower = false
            var flagUpper = false
            var flagNumber = false
            var flagSpecialChar = false
            for (i in binding.password.text.toString().indices) {
                val ch = binding.password.text.toString()[i]
                if (ch in 'a'..'z') flagLower = true
                if (ch in 'A'..'Z') flagUpper = true
                if (ch in '0'..'9') flagNumber = true
                val asciiCode = ch.code
                if ((asciiCode in 32..47) || (asciiCode in 58..64) || (asciiCode in 91..96)
                    || (asciiCode in 123..126)) flagSpecialChar = true
            }
            if (binding.password.text.toString().length < 8) {
                binding.textInputLayout.helperText = "Password must contain at least 8 characters"
                return@setOnClickListener
            }
            if (!flagLower) {
                binding.textInputLayout.helperText =
                    "Password must contain at least one lower case letter"
                return@setOnClickListener
            }
            if (!flagUpper) {
                binding.textInputLayout.helperText =
                    "Password must contain at least one upper case letter"
                return@setOnClickListener
            }
            if (!flagNumber) {
                binding.textInputLayout.helperText =
                    "Password must contain at least one numeric digit"
                return@setOnClickListener
            }
            if (!flagSpecialChar) {
                binding.textInputLayout.helperText =
                    "Password must contain at least one special character"
                return@setOnClickListener
            }
            if (binding.password.text.toString().contains("123")) {
                binding.textInputLayout.helperText = "Password should not contain 123"
                return@setOnClickListener
            }
            binding.textInputLayout.helperText = ""
            if (binding.confirmPassword.text.toString() != binding.password.text.toString()) {
                binding.textInputLayout3.helperText = "The two passwords do not match"
                return@setOnClickListener
            }
            binding.textInputLayout3.helperText = ""

            repository.signUp(userEmail, binding.confirmPassword.text.toString())
            binding.SignUpButton.isEnabled = false
            binding.ProgressBar.visibility = View.VISIBLE

            var flag = false
            repository.errorMutableLiveData.observe(viewLifecycleOwner) {
                if (it == "success") {
                    val action = SignUpDirections.actionSignUpToOtpVerification(userEmail,
                        binding.confirmPassword.text.toString(), "SignUp")
                    binding.EnterEmail.text?.clear()
                    binding.password.text?.clear()
                    binding.confirmPassword.text?.clear()
                    binding.SignUpButton.isEnabled = true
                    binding.ProgressBar.visibility = View.INVISIBLE
                    findNavController().navigate(action)
                } else {
                    Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                    binding.password.text?.clear()
                    binding.confirmPassword.text?.clear()
                    binding.SignUpButton.isEnabled = true
                    binding.ProgressBar.visibility = View.INVISIBLE
                    flag = true
                }
            }
            if (flag) return@setOnClickListener
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
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