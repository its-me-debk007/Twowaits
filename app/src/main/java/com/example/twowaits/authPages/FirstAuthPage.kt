package com.example.twowaits.authPages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                android.app.AlertDialog.Builder(context)
                        .setTitle("Exit")
                        .setMessage("Are you sure you want to exit?")
                        .setIcon(R.drawable.exit_warning)
                        .setPositiveButton("Yes", ){
                            _, _ -> activity?.finish()
                        }
                        .setNegativeButton("No"){
                            _, _ ->
                        }
                        .create()
                        .show()
    }
        })
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}