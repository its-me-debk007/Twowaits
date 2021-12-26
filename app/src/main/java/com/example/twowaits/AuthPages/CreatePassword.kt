package com.example.twowaits.AuthPages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.twowaits.databinding.CreatePasswordBinding

class CreatePassword : Fragment() {
    private var _binding: CreatePasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CreatePasswordBinding.inflate(inflater, container, false)

        binding.SignUpButton.setOnClickListener {
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
//                if(ch == '#' || ch == '%' || ch == '@' || ch == '$' || ch == '^' || ch == '&' || ch == '+' || ch == '='){
//                    flagSpecialChar = true
//                }
                val asciiCode = ch.code
                if((asciiCode in 32..47)||(asciiCode in 58..64)||(asciiCode in 91..96)||(asciiCode in 123..126)){
                    flagSpecialChar = true
                }
                }
            if(binding.EnterYourPassword.text.toString().length < 8){
                binding.textInputLayout2.helperText = "Password must contain at least 8 characters"
                return@setOnClickListener
            }
            if(!flagLower){
                binding.textInputLayout2.helperText = "Password must contain at least one lower case letter"
                return@setOnClickListener
            }
            if(!flagUpper){
                binding.textInputLayout2.helperText = "Password must contain at least one upper case letter"
                return@setOnClickListener
            }
            if(!flagNumber){
                binding.textInputLayout2.helperText = "Password must contain at least one numeric digit"
                return@setOnClickListener
            }
            if(!flagSpecialChar){
                binding.textInputLayout2.helperText = "Password must contain at least one special character"
                return@setOnClickListener
            }
            binding.textInputLayout2.helperText = ""
            if (binding.ConfirmYourPassword.text.toString() != binding.EnterYourPassword.text.toString()){
                binding.textInputLayout.helperText = "The two passwords do not match"
                return@setOnClickListener
            }
            binding.textInputLayout.helperText = ""
            }
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}