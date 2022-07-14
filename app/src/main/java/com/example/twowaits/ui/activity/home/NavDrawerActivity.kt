package com.example.twowaits.ui.activity.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.twowaits.R
import com.example.twowaits.databinding.ActivityNavDrawerBinding
import com.example.twowaits.ui.fragment.navDrawer.AboutEduCool
import com.example.twowaits.ui.fragment.navDrawer.ChangePasswordFragment
import com.example.twowaits.ui.fragment.navDrawer.FeedbackFragment
import com.example.twowaits.ui.fragment.navDrawer.PrivacyPolicy

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
                    FeedbackFragment()
                }
                else -> {
                    title = "Change Password"
                    ChangePasswordFragment()
                }
            }
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView3, fragment)
                .commit()
            supportActionBar?.title = title
            actionBar.setNavigationOnClickListener { onBackPressed() }
        }
    }
}