package com.example.twowaits

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import com.example.twowaits.databinding.SplashScreenBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: SplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Data.dataStore = createDataStore(name = "AUTH")
        Handler(Looper.getMainLooper()).postDelayed({
            lifecycleScope.launch {
                val logInStatus = Data.readData("log_in_status")
                Log.e("DDDD", logInStatus.toString())
                val intent: Intent
                if (logInStatus?.get(0) == 'F' || logInStatus?.get(0) == 'S') {
                    intent = Intent(this@SplashScreenActivity, HomeActivity::class.java)
                    Data.ACCESS_TOKEN = Data.readData("accessToken").toString()
                    Data.REFRESH_TOKEN = Data.readData("refreshToken").toString()
                    Data.USER_EMAIL = Data.readData("email").toString()
                    Data.USER = logInStatus.toString()
                } else intent = Intent(this@SplashScreenActivity, AuthActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 2000)
    }
}