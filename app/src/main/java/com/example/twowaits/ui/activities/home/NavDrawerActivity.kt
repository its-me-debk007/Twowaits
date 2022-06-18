package com.example.twowaits.ui.activities.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.twowaits.R
import com.example.twowaits.databinding.ActivityNavDrawerBinding
import com.example.twowaits.homePages.navdrawerPages.AboutEduCool
import com.example.twowaits.homePages.navdrawerPages.ChangePassword
import com.example.twowaits.homePages.navdrawerPages.Feedback
import com.example.twowaits.homePages.navdrawerPages.PrivacyPolicy

class NavDrawerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNavDrawerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            setSupportActionBar(actionBar)
            val title: String
            val fragment = when (intent.getStringExtra("navDrawerFragment")) {
                "About us" -> {
                    title = "About Educool"
                    AboutEduCool()
                }
                "Privacy Policy" -> {
                    title = "Privacy Policy"
                    PrivacyPolicy()
                }
                "Feedback" -> {
                    title = "Feedback"
                    Feedback()
                }
                else -> {
                    title = "Change Password"
                    ChangePassword()
                }
            }
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView3, fragment)
                .commit()
            supportActionBar?.title = title
            actionBar.setNavigationOnClickListener { onBackPressed() }
        }
    }
}