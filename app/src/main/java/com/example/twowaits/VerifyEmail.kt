package com.example.twowaits

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.twowaits.databinding.VerifyEmailBinding
import kotlinx.android.synthetic.main.sign_up.*
import kotlinx.android.synthetic.main.sign_up.view.*
import kotlinx.android.synthetic.main.verify_email.*
import kotlinx.android.synthetic.main.verify_email.view.*

class VerifyEmail : Fragment() {
    private var _binding: VerifyEmailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VerifyEmailBinding.inflate(inflater, container, false)

        binding.verifyButton.setOnClickListener {
            val userEmail = emailForVerifying.text.toString().trim()
            if(!Login().isValidEmail(userEmail)){
                emailForVerifying.error="Please enter a valid email"
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