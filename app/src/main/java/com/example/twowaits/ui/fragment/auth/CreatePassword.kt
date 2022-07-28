package com.example.twowaits.ui.fragment.auth

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.twowaits.R
import com.example.twowaits.databinding.CreatePasswordBinding
import com.example.twowaits.repository.authRepository.ResetPasswordRepository
import com.example.twowaits.util.isValidPassword
import com.example.twowaits.util.snackBar

class CreatePassword : Fragment(R.layout.create_password) {
    private lateinit var binding: CreatePasswordBinding
    val repository by lazy { ResetPasswordRepository() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CreatePasswordBinding.bind(view)

        binding.Proceed.setOnClickListener {
            if (binding.EnterYourPassword.text.toString().isValidPassword() != null) {
                binding.textInputLayout2.helperText =
                    binding.EnterYourPassword.text.toString().isValidPassword()
                return@setOnClickListener
            }
            binding.textInputLayout.helperText = ""

            if (binding.ConfirmYourPassword.text.toString() != binding.EnterYourPassword.text.toString()) {
                binding.textInputLayout.helperText = "The two passwords do not match"
                return@setOnClickListener
            }
            binding.textInputLayout2.helperText = ""

            val userEmail = CreatePasswordArgs.fromBundle(
                requireArguments()
            ).userEmail

            repository.resetPassword(userEmail, binding.EnterYourPassword.text.toString())
                .observe(viewLifecycleOwner) {
                    binding.Proceed.isEnabled = true
                    binding.ProgressBar.visibility = View.INVISIBLE
                    binding.EnterYourPassword.text?.clear()
                    binding.ConfirmYourPassword.text?.clear()

                    if (it.data == "success") findNavController().navigate(R.id.action_createPassword2_to_login)
                    else binding.Proceed.snackBar(it.errorMessage!!)
                }

            binding.Proceed.isEnabled = false
            binding.ProgressBar.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_createPassword2_to_login)
            }
        })
    }
}