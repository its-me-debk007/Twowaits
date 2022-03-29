package com.example.twowaits.homePages.navdrawerPages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.twowaits.R
import com.example.twowaits.databinding.AboutEducoolBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class AboutEduCool : Fragment(R.layout.about_educool) {
    private lateinit var binding: AboutEducoolBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AboutEducoolBinding.bind(view)
    }
}