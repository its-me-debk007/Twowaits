package com.example.twowaits

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.twowaits.databinding.ActivityNoteLectureBinding
import com.example.twowaits.homePages.PDFViewer
import com.example.twowaits.homePages.VideoPlayer
import com.example.twowaits.homePages.WebViewPDFViewer

class NoteLectureActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteLectureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteLectureBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            val fragment = when {
                intent.getStringExtra("PAGE TYPE") == "NOTE" &&
                        intent.getStringExtra("PREVIOUS PAGE") != "DOWNLOADS" -> WebViewPDFViewer()

                intent.getStringExtra("PAGE TYPE") == "NOTE" -> PDFViewer()

                else -> VideoPlayer()
            }
            supportFragmentManager.beginTransaction().replace(R.id.noteLectureFragmentContainerView,
            fragment).commit()
        }
    }
}