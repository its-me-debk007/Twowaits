package com.example.twowaits

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.twowaits.databinding.ActivityNoteLectureBinding
import com.example.twowaits.ui.fragment.PDFViewer
import com.example.twowaits.ui.fragment.home.VideoPlayer

class NoteLectureActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteLectureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteLectureBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            val fragment = when {
                intent.getStringExtra("PAGE TYPE") == "NOTE" &&
                        intent.getStringExtra("PREVIOUS PAGE") != "DOWNLOADS" -> PDFViewer()

                intent.getStringExtra("PAGE TYPE") == "NOTE" -> PDFViewer()

                else -> VideoPlayer()
            }
            supportFragmentManager.beginTransaction().replace(R.id.noteLectureFragmentContainerView,
            fragment).commit()
        }
    }
}