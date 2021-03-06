package com.example.twowaits.ui.activities.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.lifecycleScope
import com.example.twowaits.databinding.ActivitySplashScreenBinding
import com.example.twowaits.utils.Utils
import com.example.twowaits.ui.activities.auth.AuthActivity
import com.example.twowaits.utils.Datastore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val dataStore = Datastore(this)

        binding.motionLayout.apply {
            startLayoutAnimation()
            setTransitionListener(object : MotionLayout.TransitionListener{
                override fun onTransitionStarted(motionLayout: MotionLayout?,
                                                 startId: Int,
                                                 endId: Int) {}

                override fun onTransitionChange(motionLayout: MotionLayout?,
                                                startId: Int,
                                                endId: Int,
                                                progress: Float) {}

                override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                    lifecycleScope.launch {
                        val loginStatus = dataStore.readLoginState()
                        val intent: Intent

                        if (!loginStatus.isNullOrEmpty()) {
                            intent = Intent(this@SplashScreenActivity, HomeActivity::class.java)
                            Utils.ACCESS_TOKEN = dataStore.readTokens("accessToken")
                            Utils.REFRESH_TOKEN = dataStore.readTokens("refreshToken")
                            Utils.USER = loginStatus
                        } else
                            intent = Intent(this@SplashScreenActivity, AuthActivity::class.java)

                        startActivity(intent)
                    }
                }

                override fun onTransitionTrigger(motionLayout: MotionLayout?,
                                                 triggerId: Int,
                                                 positive: Boolean,
                                                 progress: Float) {}
            })
        }
    }

    override fun onBackPressed() {}
}