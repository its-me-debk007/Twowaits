package com.example.twowaits

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.twowaits.databinding.SignUpBinding
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.login.view.*
import kotlinx.android.synthetic.main.sign_up.*
import kotlinx.android.synthetic.main.sign_up.view.*

class SignUp : Fragment() {
    private var _binding: SignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SignUpBinding.inflate(inflater, container, false)
        binding.loginLink.setOnClickListener{
            Navigation.findNavController(binding.root).navigate(R.id.action_signUp_to_login3)
        }
        binding.VerifyEmailButton.setOnClickListener {
            val userEmail = EnterEmail.text.toString().trim()
            if(!Login().isValidEmail(userEmail)){
                EnterEmail.error="Please enter a valid email!"
                return@setOnClickListener
            }
            if(EnterName.text.toString().isEmpty()) {
                EnterName.error = "Please enter your name!"
                return@setOnClickListener
            }
        }


        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}