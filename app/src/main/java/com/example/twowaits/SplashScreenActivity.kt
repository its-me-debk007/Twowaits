package com.example.twowaits

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import com.example.twowaits.databinding.SplashScreenBinding
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: SplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        CompanionObjects.dataStore = createDataStore(name = "AUTH")
        Handler(Looper.getMainLooper()).postDelayed({
            lifecycleScope.launch {
                val logInStatus = CompanionObjects.readData("log_in_status")
                CompanionObjects.ACCESS_TOKEN = CompanionObjects.readData("accessToken").toString()
                CompanionObjects.REFRESH_TOKEN = CompanionObjects.readData("refreshToken").toString()
                CompanionObjects.USER = logInStatus.toString()
                Log.d("Tokens", CompanionObjects.USER)
                Log.d("Tokens", CompanionObjects.ACCESS_TOKEN.toString())
                val intent = if (logInStatus?.get(0) == 'F' || logInStatus?.get(0) == 'S') Intent(this@SplashScreenActivity, HomeActivity::class.java) else Intent(this@SplashScreenActivity, AuthActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 2000)
    }
}