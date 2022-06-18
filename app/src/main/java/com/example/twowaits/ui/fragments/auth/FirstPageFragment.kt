package com.example.twowaits.ui.fragments.auth

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.twowaits.R
import com.example.twowaits.databinding.FragmentFirstPageBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class FirstPageFragment : Fragment(R.layout.fragment_first_page) {
    private lateinit var binding: FragmentFirstPageBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFirstPageBinding.bind(view)
        binding.apply {
            signUpButton.setOnClickListener {
                findNavController().navigate(R.id.action_firstAuthPage_to_signUp)
            }
            loginButton.setOnClickListener {
                findNavController().navigate(R.id.action_firstAuthPage_to_login)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Exit")
                    .setMessage("Are you sure you want to exit?")
                    .setIcon(R.drawable.exit_warning)
                    .setPositiveButton("Yes") { _, _ -> activity?.finishAffinity() }
                    .setNegativeButton("No") { _, _ -> }
                    .show()
            }
        })
    }
}