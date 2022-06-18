package com.example.twowaits.ui.activities.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.twowaits.databinding.ActivityMainBinding

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}