package com.example.twowaits.ui.fragments.auth

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
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
                exitConfirmation()
            }
        })
    }

    private fun exitConfirmation() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.exit)
            .setMessage(R.string.exitConfirmation)
            .setIcon(R.drawable.exit_warning)
            .setPositiveButton("YES") { _, _ ->
                activity?.finishAffinity()
            }
            .setNegativeButton("NO") { _, _ -> }
            .setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.exit_dialog))
            .show()
    }
}