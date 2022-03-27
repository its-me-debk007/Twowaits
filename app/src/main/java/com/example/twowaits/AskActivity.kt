package com.example.twowaits

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.twowaits.databinding.ActivityAskBinding
import com.example.twowaits.homePages.UploadLecture
import com.example.twowaits.homePages.UploadNotes
import com.example.twowaits.homePages.questionsAnswers.AskQuestion
import com.example.twowaits.homePages.quiz.CreateQuiz

class AskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val title: String
        binding.apply {
            setSupportActionBar(actionBar)
            val fragment = when (intent.getStringExtra("askActivityFragment")) {
                "AskQuestion" -> {
                    title = "Ask a Question"
                    AskQuestion()
                }
                "CreateQuiz" -> {
                    title = "Create a Quiz"
                    CreateQuiz()
                }
                "UploadNote" -> {
                    title = "Upload a Note"
                    UploadNotes()
                }
                else -> {
                    title = "Upload a Lecture"
                    UploadLecture()
                }
            }
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView4, fragment)
                .commit()
            supportActionBar?.title = title
            actionBar.setNavigationOnClickListener { onBackPressed() }
        }
    }
}