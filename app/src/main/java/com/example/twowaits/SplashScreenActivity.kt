package com.example.twowaits

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
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
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

//        binding.GoogleLogo.alpha = 0f
//        binding.GoogleLogo.animate().setDuration(2000).alpha(1f).withEndAction{
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
//            finish()
//        }
        CompanionObjects.dataStore = createDataStore(name = "AUTH")

        Handler(Looper.getMainLooper()).postDelayed({
            lifecycleScope.launch {
                val logInStatus = CompanionObjects.readLoginStatus("log_in_status")
                val intent = if (logInStatus == "FACULTY" || logInStatus == "STUDENT") Intent(this@SplashScreenActivity, HomeActivity::class.java) else Intent(this@SplashScreenActivity, AuthActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 2000)
    }




}