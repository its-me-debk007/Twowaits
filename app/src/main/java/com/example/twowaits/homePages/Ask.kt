package com.example.twowaits.homePages

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.twowaits.Data
import com.example.twowaits.R
import com.example.twowaits.databinding.AskBinding

class Ask : Fragment(R.layout.ask) {
    private lateinit var binding: AskBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AskBinding.bind(view)
        Log.e("DDDD", Data.USER)
        if (Data.USER == "STUDENT") {
            binding.createQuiz.visibility = View.GONE
            binding.uploadLectureVideo.visibility = View.GONE
        }
        binding.askQuestions.setOnClickListener {
            findNavController().navigate(R.id.action_explore_to_askQuestion)
        }
        binding.createQuiz.setOnClickListener {
            findNavController().navigate(R.id.action_explore_to_createQuiz)
        }
        binding.uploadNotes.setOnClickListener {
            findNavController().navigate(R.id.action_explore_to_uploadNotes2)
        }
        binding.uploadLectureVideo.setOnClickListener {
            findNavController().navigate(R.id.action_explore_to_uploadLecture)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_explore_to_homePage)
            }
        })
    }
}