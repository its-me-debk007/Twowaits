package com.example.twowaits.ui.activity.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.twowaits.R
import com.example.twowaits.databinding.ActivityMiscellaneousBinding
import com.example.twowaits.ui.fragment.home.MoreQnA
import com.example.twowaits.ui.fragment.ask.AskQuestion
import com.example.twowaits.ui.fragment.editProfile.EditProfileFaculty
import com.example.twowaits.ui.fragment.editProfile.EditProfileStudent
import com.example.twowaits.ui.fragment.home.ShowSearchQuestions
import com.example.twowaits.ui.fragment.home.ask.UploadLecture
import com.example.twowaits.ui.fragment.home.ask.UploadNotes
import com.google.android.material.transition.platform.MaterialSharedAxis

class AskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMiscellaneousBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMiscellaneousBinding.inflate(layoutInflater)
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