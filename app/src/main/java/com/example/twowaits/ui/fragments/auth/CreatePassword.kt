package com.example.twowaits.ui.fragments.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.twowaits.R
import com.example.twowaits.databinding.CreatePasswordBinding
import com.example.twowaits.repositories.authRepositories.ResetPasswordRepository

class CreatePassword : Fragment(R.layout.create_password) {
    private lateinit var binding: CreatePasswordBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CreatePasswordBinding.bind(view)

        binding.Proceed.setOnClickListener {
            if (SignUp().isValidPassword(binding.EnterYourPassword.text.toString()) != null) {
                binding.textInputLayout2.helperText =
                    SignUp().isValidPassword(binding.EnterYourPassword.text.toString())
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
            val repository = ResetPasswordRepository()
            repository.resetPassword(userEmail, binding.EnterYourPassword.text.toString())
            binding.Proceed.isEnabled = false
            binding.ProgressBar.visibility = View.VISIBLE

            repository.errorMutableLiveData.observe(viewLifecycleOwner) {
                binding.Proceed.isEnabled = true
                binding.ProgressBar.visibility = View.INVISIBLE
                binding.EnterYourPassword.text?.clear()
                binding.ConfirmYourPassword.text?.clear()

                if (it == "success") findNavController().navigate(R.id.action_createPassword2_to_login)
                else Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
            }
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