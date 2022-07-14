package com.example.twowaits.ui.fragment.navDrawer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.twowaits.R
import com.example.twowaits.databinding.AboutEducoolBinding

class AboutEduCool : Fragment(R.layout.about_educool) {
    private lateinit var binding: AboutEducoolBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AboutEducoolBinding.bind(view)
    }
}