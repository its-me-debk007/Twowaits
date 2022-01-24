package com.example.twowaits.homePages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.twowaits.R
import com.example.twowaits.databinding.AskBinding

class Ask : Fragment() {
    private var _binding: AskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AskBinding.inflate(inflater, container, false)

        binding.askQuestions.setOnClickListener {

        }

        binding.createQuiz.setOnClickListener {
            findNavController().navigate(R.id.action_explore_to_createQuiz)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}