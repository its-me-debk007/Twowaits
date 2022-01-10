package com.example.twowaits.homePages

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.example.twowaits.R
import com.example.twowaits.databinding.HomePageBinding

class HomePage : Fragment() {
    private var _binding: HomePageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomePageBinding.inflate(inflater, container, false)

//        val callback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                exitConfirmation()
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun exitConfirmation() {
        AlertDialog.Builder(context)
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
}