package com.example.twowaits.ui.activities.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.twowaits.R
import com.example.twowaits.databinding.ActivityAskBinding
import com.example.twowaits.homePages.*
import com.example.twowaits.homePages.questionsAnswers.AskQuestion
import com.example.twowaits.ui.fragments.home.ShowSearchQuestions
import com.example.twowaits.ui.fragments.home.UploadLecture
import com.example.twowaits.ui.fragments.home.UploadNotes

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
                "UploadNote" -> {
                    title = "Upload a Note"
                    UploadNotes()
                }
                "ShowSearchQ" -> {
                    title = "Search QnA"
                    ShowSearchQuestions()
                }
                "MoreQnA" -> {
                    title = "More Q & A"
                    MoreQnA()
                }
                "FACULTY" -> {
                    title = "Edit Profile"
                    EditProfileFaculty()
                }
                "STUDENT" -> {
                    title = "Edit Profile"
                    EditProfileStudent()
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