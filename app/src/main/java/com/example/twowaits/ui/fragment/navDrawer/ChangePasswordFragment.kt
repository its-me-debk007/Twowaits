package com.example.twowaits.ui.fragment.navDrawer

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.twowaits.R
import com.example.twowaits.databinding.FragmentChangePasswordBinding
import com.example.twowaits.sealedClass.Response
import com.example.twowaits.utils.isValidPassword
import com.example.twowaits.viewModel.HomePageViewModel

class ChangePasswordFragment : Fragment(R.layout.fragment_change_password) {
    private lateinit var binding: FragmentChangePasswordBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChangePasswordBinding.bind(view)
        val viewModel = ViewModelProvider(this)[HomePageViewModel::class.java]

        binding.btn.setOnClickListener {
            if (binding.enterPassword.text.toString().isValidPassword() != null) {
                binding.textInputLayout.helperText =
                    binding.enterPassword.text.toString().isValidPassword()
                return@setOnClickListener
            }
            binding.textInputLayout.helperText = ""

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
            binding.btn.text = ""
            binding.progressBar.visibility = View.VISIBLE
            viewModel.changePasswordLiveData.observe(viewLifecycleOwner) {
                if (it is Response.Success) {
                    Toast.makeText(context, "New password set successfully", Toast.LENGTH_SHORT)
                        .show()
                    activity?.finish()
                } else {
                    binding.btn.isEnabled = true
                    binding.btn.text = "Change Password"
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(context, it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}