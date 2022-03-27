package com.example.twowaits.authPages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.twowaits.R
import com.example.twowaits.databinding.CreatePasswordBinding
import com.example.twowaits.repository.authRepositories.ResetPasswordRepository

class CreatePassword : Fragment() {
    private var _binding: CreatePasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CreatePasswordBinding.inflate(inflater, container, false)

        binding.Proceed.setOnClickListener {
            val repository = ResetPasswordRepository()
            var flagLower = false
            var flagUpper = false
            var flagNumber = false
            var flagSpecialChar = false
            for (i in binding.EnterYourPassword.text.toString().indices) {
                val ch = binding.EnterYourPassword.text.toString()[i]
                if (ch in 'a'..'z') {
                    flagLower = true
                }
                if (ch in 'A'..'Z') {
                    flagUpper = true
                }
                if (ch in '0'..'9') {
                    flagNumber = true
                }
                val asciiCode = ch.code
                if ((asciiCode in 32..47) || (asciiCode in 58..64) || (asciiCode in 91..96) || (asciiCode in 123..126)) {
                    flagSpecialChar = true
                }
            }
            if (binding.EnterYourPassword.text.toString().length < 8) {
                binding.textInputLayout2.helperText = "Password must contain at least 8 characters"
                return@setOnClickListener
            }
            if (!flagLower) {
                binding.textInputLayout2.helperText =
                    "Password must contain at least one lower case letter"
                return@setOnClickListener
            }
            if (!flagUpper) {
                binding.textInputLayout2.helperText =
                    "Password must contain at least one upper case letter"
                return@setOnClickListener
            }
            if (!flagNumber) {
                binding.textInputLayout2.helperText =
                    "Password must contain at least one numeric digit"
                return@setOnClickListener
            }
            if (!flagSpecialChar) {
                binding.textInputLayout2.helperText =
                    "Password must contain at least one special character"
                return@setOnClickListener
            }
            if (binding.EnterYourPassword.text.toString().contains("123")) {
                binding.textInputLayout2.helperText = "Password should not contain 123"
                return@setOnClickListener
            }
            binding.textInputLayout2.helperText = ""
            if (binding.ConfirmYourPassword.text.toString() != binding.EnterYourPassword.text.toString()) {
                binding.textInputLayout.helperText = "The two passwords do not match"
                return@setOnClickListener
            }
            binding.textInputLayout.helperText = ""

            val userEmail = CreatePasswordArgs.fromBundle(requireArguments()).userEmail
            repository.resetPassword(userEmail, binding.EnterYourPassword.text.toString())
            binding.Proceed.isEnabled = false
            binding.ProgressBar.visibility = View.VISIBLE

            repository.errorMutableLiveData.observe(viewLifecycleOwner) {
                if (it == "success") {
                    binding.Proceed.isEnabled = true
                    binding.ProgressBar.visibility = View.INVISIBLE
                    binding.EnterYourPassword.text?.clear()
                    binding.ConfirmYourPassword.text?.clear()
                    findNavController().navigate(R.id.action_createPassword2_to_login)
                } else {
                    Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                    binding.Proceed.isEnabled = true
                    binding.ProgressBar.visibility = View.INVISIBLE
                    binding.EnterYourPassword.text?.clear()
                    binding.ConfirmYourPassword.text?.clear()
                }
            }
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_createPassword2_to_login)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}