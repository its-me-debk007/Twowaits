package com.example.twowaits

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.twowaits.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView5) as NavHostFragment
        navController = navHostFragment.navController
        binding.apply {
            setSupportActionBar(actionBar)
            if (intent.getIntExtra("Quiz ID", -1) != -1)
                navController.navigate(R.id.quiz)

            actionBar.setNavigationOnClickListener { onBackPressed() }
        }
    }
}