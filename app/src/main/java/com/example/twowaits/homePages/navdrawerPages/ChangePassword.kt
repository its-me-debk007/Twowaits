package com.example.twowaits.homePages.navdrawerPages

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.twowaits.R
import com.example.twowaits.databinding.FragmentChangePasswordBinding
import com.example.twowaits.sealedClasses.Response
import com.example.twowaits.viewmodels.HomePageViewModel

class ChangePassword : Fragment(R.layout.fragment_change_password) {
    private lateinit var binding: FragmentChangePasswordBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChangePasswordBinding.bind(view)
        val viewModel = ViewModelProvider(this)[HomePageViewModel::class.java]

        binding.btn.setOnClickListener {
            var flagLower = false
            var flagUpper = false
            var flagNumber = false
            var flagSpecialChar = false
            for (i in binding.enterPassword.text.toString().trim().indices) {
                val ch = binding.enterPassword.text.toString()[i]
                if (ch in 'a'..'z') flagLower = true
                if (ch in 'A'..'Z') flagUpper = true
                if (ch in '0'..'9') flagNumber = true
                val asciiCode = ch.code
                if ((asciiCode in 32..47) || (asciiCode in 58..64) || (asciiCode in 91..96) || (asciiCode in 123..126))
                    flagSpecialChar = true
            }
            if (binding.enterPassword.text.toString().length < 8) {
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
            if (binding.enterPassword.text.toString().contains("123")) {
                binding.textInputLayout2.helperText = "Password should not contain 123"
                return@setOnClickListener
            }

            when {
                binding.oldPassword.text.isNullOrEmpty() -> {
                    binding.textInputLayout4.helperText = "Please enter your old password"
                    return@setOnClickListener
                }
                binding.enterPassword.text.toString().trim() == binding.oldPassword.text.toString()
                    .trim() -> {
                    binding.textInputLayout4.helperText = ""
                    binding.textInputLayout2.helperText =
                        "New password must not be same as old password"
                    return@setOnClickListener
                }
                binding.enterPassword.text.toString()
                    .trim() != binding.confirmPassword.text.toString().trim() -> {
                    binding.textInputLayout4.helperText = ""
                    binding.textInputLayout2.helperText = ""
                    binding.textInputLayout.helperText =
                        "Password and confirm password do not match"
                    return@setOnClickListener
                }
            }
            binding.textInputLayout4.helperText = ""
            binding.textInputLayout2.helperText = ""
            binding.textInputLayout.helperText = ""

            viewModel.changePassword(
                ChangePasswordBody(
                    binding.oldPassword.text.toString().trim(),
                    binding.confirmPassword.text.toString().trim()
                )
            )
            binding.btn.isEnabled = false
            binding.ProgressBar.visibility = View.VISIBLE
            viewModel.changePasswordLiveData.observe(viewLifecycleOwner) {
                if (it is Response.Success) {
                    Toast.makeText(context, "New password set successfully", Toast.LENGTH_SHORT)
                        .show()
                    activity?.finish()
                } else {
                    binding.btn.isEnabled = true
                    binding.ProgressBar.visibility = View.INVISIBLE
                    Toast.makeText(context, it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}