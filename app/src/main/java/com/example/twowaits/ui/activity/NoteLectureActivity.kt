package com.example.twowaits.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.twowaits.R
import com.example.twowaits.databinding.ActivityMiscellaneousBinding
import com.example.twowaits.ui.fragment.PDFViewer
import com.example.twowaits.ui.fragment.VideoPlayer

class NoteLectureActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMiscellaneousBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMiscellaneousBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            appBarLayout.visibility = View.GONE
            val fragment = when {
                intent.getStringExtra("PAGE TYPE") == "NOTE" &&
                        intent.getStringExtra("PREVIOUS PAGE") != "DOWNLOADS" -> PDFViewer()

                intent.getStringExtra("PAGE TYPE") == "NOTE" -> PDFViewer()

                else -> VideoPlayer()
            }
            supportFragmentManager.beginTransaction().replace(
                R.id.fragmentContainerView4,
                fragment
            ).commit()
        }
    }
}