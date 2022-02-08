package com.example.twowaits.homePages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.twowaits.Data
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

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_explore_to_homePage)
            }
        })
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}