package com.example.twowaits

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.twowaits.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            setSupportActionBar(actionBar)
            if (intent.getStringExtra("Quiz ID") != null) findNavController(
                R.id.fragmentContainerView5).navigate(R.id.quiz)
            actionBar.setNavigationOnClickListener { onBackPressed() }
        }
    }
}