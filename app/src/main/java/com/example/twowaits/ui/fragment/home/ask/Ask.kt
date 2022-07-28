package com.example.twowaits.ui.fragment.home.ask

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.twowaits.ui.activity.home.AskActivity
import com.example.twowaits.ui.activity.home.QuizActivity
import com.example.twowaits.R
import com.example.twowaits.databinding.AskBinding
import com.example.twowaits.util.USER
import com.google.android.material.transition.platform.MaterialFadeThrough

class Ask : Fragment(R.layout.ask) {
    private lateinit var binding: AskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialFadeThrough()
        enterTransition = MaterialFadeThrough()
        reenterTransition = MaterialFadeThrough()
        returnTransition = MaterialFadeThrough()

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_explore_to_homePage)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AskBinding.bind(view)
        val intent = Intent(context, AskActivity::class.java)
        if (USER == "STUDENT") {
            binding.createQuiz.visibility = View.GONE
            binding.uploadLectureVideo.visibility = View.GONE
        }
        binding.askQuestions.setOnClickListener {
            intent.putExtra("askActivityFragment", "AskQuestion")
            startActivity(intent)
        }
        binding.createQuiz.setOnClickListener {
            val quizIntent = Intent(context, QuizActivity::class.java)
            quizIntent.putExtra("quizActivityFragment", "CreateQuiz")
            startActivity(quizIntent)
        }
        binding.uploadNotes.setOnClickListener {
            intent.putExtra("askActivityFragment", "UploadNote")
            startActivity(intent)
        }
        binding.uploadLectureVideo.setOnClickListener {
            intent.putExtra("askActivityFragment", "UploadLecture")
            startActivity(intent)
        }
    }
}