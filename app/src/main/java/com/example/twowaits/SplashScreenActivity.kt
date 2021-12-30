package com.example.twowaits

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.example.twowaits.databinding.SplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: SplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashScreenBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        binding.GoogleLogo.alpha = 0f
        binding.GoogleLogo.animate().setDuration(2000).alpha(1f).withEndAction{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}