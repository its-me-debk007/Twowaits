package com.example.twowaits.ui.fragment.auth

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.twowaits.R
import com.example.twowaits.databinding.FragmentSignUpBinding
import com.example.twowaits.repository.authRepository.SignUpRepository
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {
    private lateinit var binding: FragmentSignUpBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignUpBinding.bind(view)
        binding.loginLink.setOnClickListener {
            findNavController().navigate(R.id.action_signUp_to_login3)
        }
        binding.signUpBtn.setOnClickListener {
            hideKeyboard(requireView())
            val userEmail = binding.EnterEmail.text.toString().trim()

            if (!LoginFragment().isValidEmail(userEmail)) {
                binding.EnterEmail.error = "Please enter a valid email!"
                return@setOnClickListener
            }

            if (isValidPassword(binding.password.text.toString()) != null) {
                binding.textInputLayout.helperText =
                    isValidPassword(binding.password.text.toString())
                return@setOnClickListener
            }
            binding.textInputLayout.helperText = ""

            if (binding.confirmPassword.text.toString() != binding.password.text.toString()) {
                binding.textInputLayout3.helperText = "The two passwords do not match"
                return@setOnClickListener
            }
            binding.textInputLayout3.helperText = ""

            val repository = SignUpRepository()
            repository.signUp(userEmail, binding.confirmPassword.text.toString())
            binding.signUpBtn.isEnabled = false
            binding.signUpBtn.text = ""
            binding.progressBar.visibility = View.VISIBLE

            var flag = false
            repository.errorMutableLiveData.observe(viewLifecycleOwner) {
                if (it == "success") {
                    val action = SignUpFragmentDirections.actionSignUpToOtpVerification(
                        userEmail,
                        binding.confirmPassword.text.toString(), "SignUp"
                    )
                    binding.EnterEmail.text?.clear()
                    clearFields()
                    findNavController().navigate(action)
                } else {
                    Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                    clearFields()
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

    fun isValidPassword(password: String): String? {
        return when {
            password.length < 8 -> "Must contain at least 8 characters"

            !password.matches(".*[A-Z].*".toRegex()) -> "Must contain at least 1 uppercase letter"

            !password.matches(".*[a-z].*".toRegex()) -> "Must contain at least 1 lowercase letter"

            !password.matches(".*[\$#%@&*/+_=?^!].*".toRegex()) -> "Must contain at least 1 special character"

            !password.matches(".*[0-9].*".toRegex()) -> "Must contain at least 1 numeric digit"

            password.contains("123") -> "Must not contain 123"

            else -> null
        }
    }
}