package com.example.twowaits

import android.content.Intent
import android.os.Bundle
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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Data.dataStore = createDataStore(name = "AUTH")
        lifecycleScope.launch {
            val logInStatus = Data.readData("log_in_status")
            val intent: Intent
            if (logInStatus?.get(0) == 'F' || logInStatus?.get(0) == 'S') {
                intent = Intent(this@SplashScreenActivity, HomeActivity::class.java)
                    Data.ACCESS_TOKEN = Data.readData("accessToken").toString()
                    Data.REFRESH_TOKEN = Data.readData("refreshToken").toString()
                    Data.EMAIL = Data.readData("email").toString()
                    Data.USER = logInStatus.toString()
            } else intent = Intent(this@SplashScreenActivity, AuthActivity::class.java)
            startActivity(intent)
        }
    }
}