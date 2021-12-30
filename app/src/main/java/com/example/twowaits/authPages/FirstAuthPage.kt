package com.example.twowaits.authPages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.twowaits.R
import com.example.twowaits.databinding.FirstAuthPageBinding

class FirstAuthPage: Fragment() {
    private var _binding: FirstAuthPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FirstAuthPageBinding.inflate(inflater, container, false)

        binding.signUpButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_firstAuthPage_to_signUp)
        }
        binding.loginButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_firstAuthPage_to_login)
        }

        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}